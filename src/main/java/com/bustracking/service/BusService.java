package com.bustracking.service;

import com.bustracking.dto.BusDto;
import com.bustracking.dto.BusLocationDto;
import com.bustracking.dto.EtaInfoDto;
import com.bustracking.model.*;
import com.bustracking.repository.BusLocationRepository;
import com.bustracking.repository.BusRepository;
import com.bustracking.repository.RouteRepository;
import com.bustracking.repository.RouteStopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BusService {

    private final BusRepository busRepository;
    private final BusLocationRepository busLocationRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public BusService(BusRepository busRepository,
                      BusLocationRepository busLocationRepository,
                      RouteRepository routeRepository,
                      RouteStopRepository routeStopRepository) {
        this.busRepository = busRepository;
        this.busLocationRepository = busLocationRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
    }

    public List<BusDto> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BusDto> getActiveBuses() {
        return busRepository.findByStatusNot(BusStatus.OUT_OF_SERVICE).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<BusDto> getBusById(Long id) {
        return busRepository.findById(id).map(this::toDto);
    }

    public Optional<BusLocationDto> getBusLocation(Long busId) {
        return busRepository.findById(busId).map(bus -> {
            if (bus.getCurrentLocation() != null) {
                return toLocationDto(bus.getCurrentLocation(), bus);
            }
            return null;
        });
    }

    public List<BusLocationDto> getAllBusLocations() {
        return busRepository.findAll().stream()
                .filter(b -> b.getCurrentLocation() != null)
                .map(b -> toLocationDto(b.getCurrentLocation(), b))
                .collect(Collectors.toList());
    }

    public BusDto createBus(BusDto dto) {
        Bus bus = new Bus();
        bus.setBusNumber(dto.getBusNumber());
        bus.setRegNumber(dto.getRegNumber());
        bus.setCapacity(dto.getCapacity() != null ? dto.getCapacity() : 40);
        bus.setDriverName(dto.getDriverName());
        bus.setDriverPhone(dto.getDriverPhone());
        bus.setStatus(dto.getStatus() != null ? dto.getStatus() : BusStatus.IDLE);
        bus.setLastUpdated(LocalDateTime.now());

        if (dto.getRouteId() != null) {
            routeRepository.findById(dto.getRouteId()).ifPresent(route -> {
                bus.setAssignedRoute(route);
                List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(route.getId());
                if (!stops.isEmpty()) {
                    Stop firstStop = stops.get(0).getStop();
                    BusLocation loc = new BusLocation(bus, firstStop.getLatitude(), firstStop.getLongitude(), 0.0, 0.0);
                    loc.setCurrentStop(firstStop);
                    if (stops.size() > 1) {
                        Stop secondStop = stops.get(1).getStop();
                        loc.setNextStop(secondStop);
                        loc.setNextStopName(secondStop.getName());
                        double dist = GeoUtils.calculateDistanceKm(firstStop.getLatitude(), firstStop.getLongitude(),
                                secondStop.getLatitude(), secondStop.getLongitude());
                        loc.setDistanceToNextStopKm(Math.round(dist * 100.0) / 100.0);
                        loc.setEtaNextStopMinutes(GeoUtils.calculateEtaMinutes(dist, 25.0));
                    }
                    bus.setCurrentLocation(loc);
                }
            });
        }

        if (bus.getCurrentLocation() == null && dto.getCurrentLatitude() != null && dto.getCurrentLongitude() != null) {
            BusLocation loc = new BusLocation(bus, dto.getCurrentLatitude(), dto.getCurrentLongitude(),
                    dto.getSpeedKmph() != null ? dto.getSpeedKmph() : 0.0,
                    dto.getHeading() != null ? dto.getHeading() : 0.0);
            bus.setCurrentLocation(loc);
        }

        Bus saved = busRepository.save(bus);
        return toDto(saved);
    }

    public Optional<BusDto> updateBus(Long id, BusDto dto) {
        return busRepository.findById(id).map(existing -> {
            existing.setBusNumber(dto.getBusNumber());
            existing.setRegNumber(dto.getRegNumber());
            if (dto.getCapacity() != null) existing.setCapacity(dto.getCapacity());
            existing.setDriverName(dto.getDriverName());
            existing.setDriverPhone(dto.getDriverPhone());
            if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

            if (dto.getRouteId() != null) {
                routeRepository.findById(dto.getRouteId()).ifPresent(existing::setAssignedRoute);
            } else if (dto.getRouteId() == null && dto.getRouteNumber() == null) {
                existing.setAssignedRoute(null);
            }

            existing.setLastUpdated(LocalDateTime.now());
            return toDto(busRepository.save(existing));
        });
    }

    public boolean deleteBus(Long id) {
        if (busRepository.existsById(id)) {
            busRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<EtaInfoDto> getBusEtas(Long busId) {
        Bus bus = busRepository.findById(busId).orElse(null);
        if (bus == null || bus.getAssignedRoute() == null || bus.getCurrentLocation() == null) {
            return new ArrayList<>();
        }

        List<RouteStop> routeStops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(bus.getAssignedRoute().getId());
        List<EtaInfoDto> etas = new ArrayList<>();
        BusLocation loc = bus.getCurrentLocation();

        int nextStopOrder = 1;
        if (loc.getNextStop() != null) {
            for (RouteStop rs : routeStops) {
                if (rs.getStop().getId().equals(loc.getNextStop().getId())) {
                    nextStopOrder = rs.getStopOrder();
                    break;
                }
            }
        }

        double cumulativeDistance = 0.0;
        double speed = loc.getSpeedKmph() != null && loc.getSpeedKmph() > 5.0 ? loc.getSpeedKmph() : 25.0;

        for (RouteStop rs : routeStops) {
            Stop stop = rs.getStop();
            String status;
            double legDist;
            int etaMin;

            if (rs.getStopOrder() < nextStopOrder) {
                status = "PASSED";
                legDist = 0.0;
                etaMin = 0;
            } else if (rs.getStopOrder() == nextStopOrder) {
                status = "NEXT_STOP";
                legDist = GeoUtils.calculateDistanceKm(
                        loc.getLatitude(), loc.getLongitude(),
                        stop.getLatitude(), stop.getLongitude()
                );
                cumulativeDistance += legDist;
                etaMin = GeoUtils.calculateEtaMinutes(cumulativeDistance, speed);
            } else {
                status = "UPCOMING";
                RouteStop prevRs = routeStops.get(rs.getStopOrder() - 2);
                double stepDist = GeoUtils.calculateDistanceKm(
                        prevRs.getStop().getLatitude(), prevRs.getStop().getLongitude(),
                        stop.getLatitude(), stop.getLongitude()
                );
                cumulativeDistance += stepDist;
                legDist = Math.round(cumulativeDistance * 100.0) / 100.0;
                etaMin = GeoUtils.calculateEtaMinutes(cumulativeDistance, speed);
            }

            etas.add(new EtaInfoDto(
                    stop.getId(),
                    stop.getName(),
                    stop.getLandmark(),
                    rs.getStopOrder(),
                    Math.round(legDist * 100.0) / 100.0,
                    etaMin,
                    status
            ));
        }

        return etas;
    }

    public BusDto toDto(Bus bus) {
        BusDto dto = new BusDto();
        dto.setId(bus.getId());
        dto.setBusNumber(bus.getBusNumber());
        dto.setRegNumber(bus.getRegNumber());
        dto.setCapacity(bus.getCapacity());
        dto.setDriverName(bus.getDriverName());
        dto.setDriverPhone(bus.getDriverPhone());
        dto.setStatus(bus.getStatus());
        dto.setLastUpdated(bus.getLastUpdated());

        if (bus.getAssignedRoute() != null) {
            dto.setRouteId(bus.getAssignedRoute().getId());
            dto.setRouteNumber(bus.getAssignedRoute().getRouteNumber());
            dto.setRouteName(bus.getAssignedRoute().getRouteName());
        }

        if (bus.getCurrentLocation() != null) {
            BusLocation loc = bus.getCurrentLocation();
            dto.setCurrentLatitude(loc.getLatitude());
            dto.setCurrentLongitude(loc.getLongitude());
            dto.setSpeedKmph(loc.getSpeedKmph());
            dto.setHeading(loc.getHeading());
            dto.setCurrentStopId(loc.getCurrentStop() != null ? loc.getCurrentStop().getId() : null);
            dto.setNextStopId(loc.getNextStop() != null ? loc.getNextStop().getId() : null);
            dto.setNextStopName(loc.getNextStopName());
            dto.setDistanceToNextStopKm(loc.getDistanceToNextStopKm());
            dto.setEtaNextStopMinutes(loc.getEtaNextStopMinutes());
            dto.setLocation(toLocationDto(loc, bus));
        }

        return dto;
    }

    public BusLocationDto toLocationDto(BusLocation loc, Bus bus) {
        return new BusLocationDto(
                loc.getId(),
                bus.getId(),
                bus.getBusNumber(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getSpeedKmph(),
                loc.getHeading(),
                loc.getCurrentStop() != null ? loc.getCurrentStop().getId() : null,
                loc.getNextStop() != null ? loc.getNextStop().getId() : null,
                loc.getNextStopName(),
                loc.getDistanceToNextStopKm(),
                loc.getEtaNextStopMinutes(),
                loc.getTimestamp()
        );
    }
}
