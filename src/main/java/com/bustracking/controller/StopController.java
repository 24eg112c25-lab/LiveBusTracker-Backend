package com.bustracking.controller;

import com.bustracking.dto.StopDto;
import com.bustracking.service.StopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopController {

    private final StopService stopService;

    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public ResponseEntity<List<StopDto>> getAllStops() {
        return ResponseEntity.ok(stopService.getAllStops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StopDto> getStopById(@PathVariable Long id) {
        return stopService.getStopById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StopDto> createStop(@Valid @RequestBody StopDto dto) {
        StopDto created = stopService.createStop(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StopDto> updateStop(@PathVariable Long id, @Valid @RequestBody StopDto dto) {
        return stopService.updateStop(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStop(@PathVariable Long id) {
        if (stopService.deleteStop(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
