package com.bustracking.controller;

import com.bustracking.dto.RouteDto;
import com.bustracking.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDto>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RouteDto> createRoute(@Valid @RequestBody RouteDto dto) {
        RouteDto created = routeService.createRoute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteDto> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteDto dto) {
        return routeService.updateRoute(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        if (routeService.deleteRoute(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/stops")
    public ResponseEntity<RouteDto> addStopToRoute(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        if (!body.containsKey("stopId") || body.get("stopId") == null) {
            return ResponseEntity.badRequest().build();
        }
        Long stopId = Long.valueOf(body.get("stopId").toString());
        Integer stopOrder = body.containsKey("stopOrder") && body.get("stopOrder") != null
                ? Integer.valueOf(body.get("stopOrder").toString()) : null;

        return routeService.addStopToRoute(id, stopId, stopOrder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/stops/{stopId}")
    public ResponseEntity<RouteDto> removeStopFromRoute(@PathVariable Long id, @PathVariable Long stopId) {
        return routeService.removeStopFromRoute(id, stopId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RouteDto>> searchRoutes(@RequestParam Long originStopId,
                                                        @RequestParam Long destStopId) {
        return ResponseEntity.ok(routeService.searchRoutes(originStopId, destStopId));
    }
}
