package com.bustracking.simulator;

import com.bustracking.dto.SimulationStatusDto;
import com.bustracking.model.*;
import com.bustracking.repository.BusLocationRepository;
import com.bustracking.repository.BusRepository;
import com.bustracking.repository.RouteStopRepository;
import com.bustracking.service.GeoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BusMovementSimulator {

    private static final Logger log = LoggerFactory.getLogger(BusMovementSimulator.class);

    private final BusRepository busRepository;
    private final BusLocationRepository busLocationRepository;
    private final RouteStopRepository routeStopRepository;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private volatile double speedMultiplier = 2.0;
    private final AtomicLong tickCounter = new AtomicLong(0);

    // Per-bus in-memory simulation tracking state
    private final Map<Long, BusProgress> busProgressMap = new ConcurrentHashMap<>();

    public BusMovementSimulator(BusRepository busRepository,
                                BusLocationRepository busLocationRepository,
                                RouteStopRepository routeStopRepository,
                                @Value("${bus.simulation.enabled:true}") boolean enabled,
                                @Value("${bus.simulation.default-speed-multiplier:2.0}") double defaultMultiplier) {
        this.busRepository = busRepository;
        this.busLocationRepository = busLocationRepository;
        this.routeStopRepository = routeStopRepository;
        this.running.set(enabled);
        this.speedMultiplier = defaultMultiplier;
    }

    private static class BusProgress {
        int segmentIndex = 0;       // Segment between stop i and stop i+1
        double progress = 0.0;      // 0.0 to 1.0
        int dwellTicks = 0;         // Ticks to pause at stop
    }

    @Scheduled(fixedRateString = "${bus.simulation.interval-ms:2000}")
    @Transactional
    public void simulationTick() {
        if (!running.get()) {
            return;
        }

        tickCounter.incrementAndGet();
        List<Bus> activeBuses = busRepository.findByAssignedRouteIsNotNull();

        for (Bus bus : activeBuses) {
            if (bus.getStatus() == BusStatus.OUT_OF_SERVICE) {
                continue;
            }

            simulateBusStep(bus);
        }
    }

    private void simulateBusStep(Bus bus) {
        if (bus.getAssignedRoute() == null) {
            return;
        }

        List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(bus.getAssignedRoute().getId());
        if (stops.size() < 2) {
            return;
        }

        BusProgress progress = busProgressMap.computeIfAbsent(bus.getId(), id -> {
            BusProgress bp = new BusProgress();
            bp.segmentIndex = 0;
            bp.progress = 0.0;
            bp.dwellTicks = 2;
            return bp;
        });

        BusLocation loc = bus.getCurrentLocation();
        if (loc == null) {
            Stop initialStop = stops.get(0).getStop();
            loc = new BusLocation(bus, initialStop.getLatitude(), initialStop.getLongitude(), 0.0, 0.0);
            loc.setCurrentStop(initialStop);
            bus.setCurrentLocation(loc);
        }

        // Check if currently dwelling/paused at a bus stop
        if (progress.dwellTicks > 0) {
            progress.dwellTicks--;
            bus.setStatus(BusStatus.STOPPED_AT_STOP);
            loc.setSpeedKmph(0.0);
            loc.setTimestamp(LocalDateTime.now());
            bus.setLastUpdated(LocalDateTime.now());
            busRepository.save(bus);
            return;
        }

        int numSegments = stops.size() - 1;
        if (progress.segmentIndex >= numSegments) {
            progress.segmentIndex = 0;
            progress.progress = 0.0;
        }

        Stop fromStop = stops.get(progress.segmentIndex).getStop();
        Stop toStop = stops.get(progress.segmentIndex + 1).getStop();

        double legDistanceKm = GeoUtils.calculateDistanceKm(
                fromStop.getLatitude(), fromStop.getLongitude(),
                toStop.getLatitude(), toStop.getLongitude()
        );

        if (legDistanceKm <= 0.001) {
            legDistanceKm = 0.1;
        }

        double currentSpeed = 25.0 * speedMultiplier;
        double stepDistanceKm = (currentSpeed / 3600.0) * 2.0;
        double deltaProgress = stepDistanceKm / legDistanceKm;

        progress.progress += deltaProgress;

        if (progress.progress >= 1.0) {
            // Bus arrived at toStop
            loc.setLatitude(toStop.getLatitude());
            loc.setLongitude(toStop.getLongitude());
            loc.setCurrentStop(toStop);
            loc.setSpeedKmph(0.0);
            bus.setStatus(BusStatus.STOPPED_AT_STOP);
            progress.dwellTicks = 3; // Pause for ~6 seconds at the stop

            progress.segmentIndex++;
            progress.progress = 0.0;

            if (progress.segmentIndex >= numSegments) {
                // Reached final stop -> Loop back to start
                progress.segmentIndex = 0;
                Stop nextStop = stops.get(1).getStop();
                loc.setNextStop(nextStop);
                loc.setNextStopName(nextStop.getName());
            } else {
                Stop nextStop = stops.get(progress.segmentIndex + 1).getStop();
                loc.setNextStop(nextStop);
                loc.setNextStopName(nextStop.getName());
            }

            double distToNext = GeoUtils.calculateDistanceKm(
                    loc.getLatitude(), loc.getLongitude(),
                    toStop.getLatitude(), toStop.getLongitude()
            );
            loc.setDistanceToNextStopKm(Math.round(distToNext * 100.0) / 100.0);
            loc.setEtaNextStopMinutes(GeoUtils.calculateEtaMinutes(distToNext, currentSpeed));
        } else {
            // Moving along segment
            double newLat = GeoUtils.interpolate(fromStop.getLatitude(), toStop.getLatitude(), progress.progress);
            double newLng = GeoUtils.interpolate(fromStop.getLongitude(), toStop.getLongitude(), progress.progress);
            double bearing = GeoUtils.calculateBearing(fromStop.getLatitude(), fromStop.getLongitude(), toStop.getLatitude(), toStop.getLongitude());

            loc.setLatitude(Math.round(newLat * 1000000.0) / 1000000.0);
            loc.setLongitude(Math.round(newLng * 1000000.0) / 1000000.0);
            loc.setHeading(Math.round(bearing * 10.0) / 10.0);
            loc.setSpeedKmph(Math.round(currentSpeed * 10.0) / 10.0);
            loc.setCurrentStop(null);
            loc.setNextStop(toStop);
            loc.setNextStopName(toStop.getName());
            bus.setStatus(BusStatus.IN_TRANSIT);

            double remainingDist = GeoUtils.calculateDistanceKm(
                    newLat, newLng,
                    toStop.getLatitude(), toStop.getLongitude()
            );
            loc.setDistanceToNextStopKm(Math.round(remainingDist * 100.0) / 100.0);
            loc.setEtaNextStopMinutes(GeoUtils.calculateEtaMinutes(remainingDist, currentSpeed));
        }

        loc.setTimestamp(LocalDateTime.now());
        bus.setLastUpdated(LocalDateTime.now());
        busRepository.save(bus);
    }

    public void startSimulation() {
        this.running.set(true);
        log.info("Bus GPS Simulation started");
    }

    public void pauseSimulation() {
        this.running.set(false);
        log.info("Bus GPS Simulation paused");
    }

    public void resetSimulation() {
        this.busProgressMap.clear();
        this.tickCounter.set(0);
        List<Bus> activeBuses = busRepository.findByAssignedRouteIsNotNull();
        for (Bus bus : activeBuses) {
            List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(bus.getAssignedRoute().getId());
            if (!stops.isEmpty()) {
                Stop firstStop = stops.get(0).getStop();
                BusLocation loc = bus.getCurrentLocation();
                if (loc == null) {
                    loc = new BusLocation(bus, firstStop.getLatitude(), firstStop.getLongitude(), 0.0, 0.0);
                    bus.setCurrentLocation(loc);
                } else {
                    loc.setLatitude(firstStop.getLatitude());
                    loc.setLongitude(firstStop.getLongitude());
                    loc.setSpeedKmph(0.0);
                    loc.setHeading(0.0);
                }
                loc.setCurrentStop(firstStop);
                bus.setStatus(BusStatus.STOPPED_AT_STOP);

                if (stops.size() > 1) {
                    Stop nextStop = stops.get(1).getStop();
                    loc.setNextStop(nextStop);
                    loc.setNextStopName(nextStop.getName());
                    double dist = GeoUtils.calculateDistanceKm(firstStop.getLatitude(), firstStop.getLongitude(), nextStop.getLatitude(), nextStop.getLongitude());
                    loc.setDistanceToNextStopKm(Math.round(dist * 100.0) / 100.0);
                    loc.setEtaNextStopMinutes(GeoUtils.calculateEtaMinutes(dist, 25.0));
                }
                loc.setTimestamp(LocalDateTime.now());
                bus.setLastUpdated(LocalDateTime.now());
                busRepository.save(bus);
            }
        }
        log.info("Bus GPS Simulation reset to origin stops");
    }

    public void setSpeedMultiplier(double multiplier) {
        if (multiplier > 0.0 && multiplier <= 20.0) {
            this.speedMultiplier = multiplier;
            log.info("Simulation speed multiplier set to: {}", multiplier);
        }
    }

    public void teleportBus(Long busId, Long stopId) {
        busRepository.findById(busId).ifPresent(bus -> {
            if (bus.getAssignedRoute() == null) return;
            List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(bus.getAssignedRoute().getId());
            for (int i = 0; i < stops.size(); i++) {
                if (stops.get(i).getStop().getId().equals(stopId)) {
                    Stop stop = stops.get(i).getStop();
                    BusLocation loc = bus.getCurrentLocation();
                    if (loc == null) {
                        loc = new BusLocation(bus, stop.getLatitude(), stop.getLongitude(), 0.0, 0.0);
                        bus.setCurrentLocation(loc);
                    } else {
                        loc.setLatitude(stop.getLatitude());
                        loc.setLongitude(stop.getLongitude());
                        loc.setSpeedKmph(0.0);
                    }
                    loc.setCurrentStop(stop);
                    bus.setStatus(BusStatus.STOPPED_AT_STOP);

                    BusProgress bp = busProgressMap.computeIfAbsent(bus.getId(), k -> new BusProgress());
                    bp.segmentIndex = (i < stops.size() - 1) ? i : 0;
                    bp.progress = 0.0;
                    bp.dwellTicks = 2;

                    int nextIdx = (i < stops.size() - 1) ? i + 1 : 0;
                    Stop next = stops.get(nextIdx).getStop();
                    loc.setNextStop(next);
                    loc.setNextStopName(next.getName());
                    loc.setTimestamp(LocalDateTime.now());
                    bus.setLastUpdated(LocalDateTime.now());
                    busRepository.save(bus);
                    break;
                }
            }
        });
    }

    public SimulationStatusDto getStatus() {
        int activeCount = (int) busRepository.findByAssignedRouteIsNotNull().stream()
                .filter(b -> b.getStatus() != BusStatus.OUT_OF_SERVICE)
                .count();

        String message = running.get()
                ? "Simulation is actively running (Speed: " + speedMultiplier + "x)"
                : "Simulation is currently paused";

        return new SimulationStatusDto(
                running.get(),
                speedMultiplier,
                activeCount,
                tickCounter.get(),
                message
        );
    }
}
