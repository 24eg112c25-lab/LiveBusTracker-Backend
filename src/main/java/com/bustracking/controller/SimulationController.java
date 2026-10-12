package com.bustracking.controller;

import com.bustracking.dto.SimulationStatusDto;
import com.bustracking.simulator.BusMovementSimulator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulator")
public class SimulationController {

    private final BusMovementSimulator simulator;

    public SimulationController(BusMovementSimulator simulator) {
        this.simulator = simulator;
    }

    @GetMapping("/status")
    public ResponseEntity<SimulationStatusDto> getStatus() {
        return ResponseEntity.ok(simulator.getStatus());
    }

    @PostMapping("/start")
    public ResponseEntity<SimulationStatusDto> startSimulation() {
        simulator.startSimulation();
        return ResponseEntity.ok(simulator.getStatus());
    }

    @PostMapping("/pause")
    public ResponseEntity<SimulationStatusDto> pauseSimulation() {
        simulator.pauseSimulation();
        return ResponseEntity.ok(simulator.getStatus());
    }

    @PostMapping("/reset")
    public ResponseEntity<SimulationStatusDto> resetSimulation() {
        simulator.resetSimulation();
        return ResponseEntity.ok(simulator.getStatus());
    }

    @PostMapping("/speed")
    public ResponseEntity<SimulationStatusDto> setSpeed(@RequestParam double multiplier) {
        simulator.setSpeedMultiplier(multiplier);
        return ResponseEntity.ok(simulator.getStatus());
    }

    @PostMapping("/teleport")
    public ResponseEntity<SimulationStatusDto> teleportBus(@RequestParam Long busId, @RequestParam Long stopId) {
        simulator.teleportBus(busId, stopId);
        return ResponseEntity.ok(simulator.getStatus());
    }
}
