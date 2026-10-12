package com.bustracking.service;

import com.bustracking.dto.RouteDto;
import com.bustracking.dto.RouteStopDto;
import com.bustracking.model.Route;
import com.bustracking.model.RouteStop;
import com.bustracking.model.Stop;
import com.bustracking.repository.RouteRepository;
import com.bustracking.repository.RouteStopRepository;
import com.bustracking.repository.StopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final StopRepository stopRepository;

    public RouteService(RouteRepository routeRepository,
                        RouteStopRepository routeStopRepository,
                        StopRepository stopRepository) {
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.stopRepository = stopRepository;
    }

    public List<RouteDto> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RouteDto> getRouteById(Long id) {
        return routeRepository.findById(id).map(this::toDto);
    }

    public RouteDto createRoute(RouteDto dto) {
        Route route = new Route();
        route.setRouteNumber(dto.getRouteNumber());
        route.setRouteName(dto.getRouteName());
        route.setOrigin(dto.getOrigin());
        route.setDestination(dto.getDestination());
        route.setTotalDistanceKm(dto.getTotalDistanceKm() != null ? dto.getTotalDistanceKm() : 0.0);
        route.setActive(dto.getActive() != null ? dto.getActive() : true);
        Route saved = routeRepository.save(route);
        return toDto(saved);
    }

    public Optional<RouteDto> updateRoute(Long id, RouteDto dto) {
        return routeRepository.findById(id).map(existing -> {
            existing.setRouteNumber(dto.getRouteNumber());
            existing.setRouteName(dto.getRouteName());
            existing.setOrigin(dto.getOrigin());
            existing.setDestination(dto.getDestination());
            if (dto.getActive() != null) {
                existing.setActive(dto.getActive());
            }
            return toDto(routeRepository.save(existing));
        });
    }

    public boolean deleteRoute(Long id) {
        if (routeRepository.existsById(id)) {
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<RouteDto> addStopToRoute(Long routeId, Long stopId, Integer stopOrder) {
        Optional<Route> routeOpt = routeRepository.findById(routeId);
        Optional<Stop> stopOpt = stopRepository.findById(stopId);

        if (routeOpt.isEmpty() || stopOpt.isEmpty()) {
            return Optional.empty();
        }

        Route route = routeOpt.get();
        Stop stop = stopOpt.get();

        // Check if stop already exists in route
        Optional<RouteStop> existing = routeStopRepository.findByRouteIdAndStopId(routeId, stopId);
        RouteStop routeStop = existing.orElseGet(() -> new RouteStop(route, stop, stopOrder, 0.0));
        
        if (stopOrder != null) {
            routeStop.setStopOrder(stopOrder);
        } else {
            int maxOrder = routeStopRepository.findByRouteIdOrderByStopOrderAsc(routeId).stream()
                    .mapToInt(RouteStop::getStopOrder)
                    .max().orElse(0);
            routeStop.setStopOrder(maxOrder + 1);
        }

        routeStopRepository.save(routeStop);
        recalculateRouteDistances(routeId);

        return routeRepository.findById(routeId).map(this::toDto);
    }

    public Optional<RouteDto> removeStopFromRoute(Long routeId, Long stopId) {
        if (routeRepository.existsById(routeId)) {
            routeStopRepository.deleteByRouteIdAndStopId(routeId, stopId);
            recalculateRouteDistances(routeId);
            return routeRepository.findById(routeId).map(this::toDto);
        }
        return Optional.empty();
    }

    public void recalculateRouteDistances(Long routeId) {
        Route route = routeRepository.findById(routeId).orElse(null);
        if (route == null) return;

        List<RouteStop> orderedStops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(routeId);
        double cumulativeKm = 0.0;
        Stop prevStop = null;

        for (int i = 0; i < orderedStops.size(); i++) {
            RouteStop rs = orderedStops.get(i);
            rs.setStopOrder(i + 1);
            if (prevStop != null) {
                double legKm = GeoUtils.calculateDistanceKm(
                        prevStop.getLatitude(), prevStop.getLongitude(),
                        rs.getStop().getLatitude(), rs.getStop().getLongitude()
                );
                cumulativeKm += legKm;
            }
            rs.setDistanceFromStartKm(Math.round(cumulativeKm * 100.0) / 100.0);
            routeStopRepository.save(rs);
            prevStop = rs.getStop();
        }

        route.setTotalDistanceKm(Math.round(cumulativeKm * 100.0) / 100.0);
        routeRepository.save(route);
    }

    public List<RouteDto> searchRoutes(Long originStopId, Long destStopId) {
        return routeRepository.findRoutesBetweenStops(originStopId, destStopId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RouteDto toDto(Route route) {
        List<RouteStop> routeStops = routeStopRepository.findByRouteIdOrderByStopOrderAsc(route.getId());
        List<RouteStopDto> stopDtos = routeStops.stream().map(rs -> new RouteStopDto(
                rs.getId(),
                rs.getStop().getId(),
                rs.getStop().getName(),
                rs.getStop().getLandmark(),
                rs.getStop().getLatitude(),
                rs.getStop().getLongitude(),
                rs.getStopOrder(),
                rs.getDistanceFromStartKm()
        )).collect(Collectors.toList());

        return new RouteDto(
                route.getId(),
                route.getRouteNumber(),
                route.getRouteName(),
                route.getOrigin(),
                route.getDestination(),
                route.getTotalDistanceKm(),
                route.getActive(),
                stopDtos
        );
    }
}
