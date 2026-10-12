package com.bustracking.controller;

import com.bustracking.dto.BusDto;
import com.bustracking.dto.BusLocationDto;
import com.bustracking.dto.EtaInfoDto;
import com.bustracking.service.BusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping
    public ResponseEntity<List<BusDto>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @GetMapping("/active")
    public ResponseEntity<List<BusDto>> getActiveBuses() {
        return ResponseEntity.ok(busService.getActiveBuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDto> getBusById(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/locations")
    public ResponseEntity<List<BusLocationDto>> getAllBusLocations() {
        return ResponseEntity.ok(busService.getAllBusLocations());
    }

    @GetMapping("/{id}/location")
    public ResponseEntity<BusLocationDto> getBusLocation(@PathVariable Long id) {
        return busService.getBusLocation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/eta")
    public ResponseEntity<List<EtaInfoDto>> getBusEtas(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBusEtas(id));
    }

    @PostMapping
    public ResponseEntity<BusDto> createBus(@Valid @RequestBody BusDto dto) {
        BusDto created = busService.createBus(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusDto> updateBus(@PathVariable Long id, @Valid @RequestBody BusDto dto) {
        return busService.updateBus(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        if (busService.deleteBus(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
