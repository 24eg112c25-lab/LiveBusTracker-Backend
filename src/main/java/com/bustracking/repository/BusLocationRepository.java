package com.bustracking.repository;

import com.bustracking.model.BusLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusLocationRepository extends JpaRepository<BusLocation, Long> {
    Optional<BusLocation> findByBusId(Long busId);
    void deleteByBusId(Long busId);
}
