package com.bustracking.service;

import com.bustracking.dto.StopDto;
import com.bustracking.model.Stop;
import com.bustracking.repository.StopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StopService {

    private final StopRepository stopRepository;

    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<StopDto> getAllStops() {
        return stopRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<StopDto> getStopById(Long id) {
        return stopRepository.findById(id).map(this::toDto);
    }

    public StopDto createStop(StopDto dto) {
        Stop stop = new Stop();
        stop.setName(dto.getName());
        stop.setLandmark(dto.getLandmark());
        stop.setLatitude(dto.getLatitude());
        stop.setLongitude(dto.getLongitude());
        Stop saved = stopRepository.save(stop);
        return toDto(saved);
    }

    public Optional<StopDto> updateStop(Long id, StopDto dto) {
        return stopRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setLandmark(dto.getLandmark());
            existing.setLatitude(dto.getLatitude());
            existing.setLongitude(dto.getLongitude());
            return toDto(stopRepository.save(existing));
        });
    }

    public boolean deleteStop(Long id) {
        if (stopRepository.existsById(id)) {
            stopRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public StopDto toDto(Stop stop) {
        return new StopDto(stop.getId(), stop.getName(), stop.getLandmark(), stop.getLatitude(), stop.getLongitude());
    }
}
