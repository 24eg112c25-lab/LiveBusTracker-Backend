package com.bustracking.repository;

import com.bustracking.model.Bus;
import com.bustracking.model.BusStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByBusNumber(String busNumber);
    List<Bus> findByAssignedRouteId(Long routeId);
    List<Bus> findByStatus(BusStatus status);
    List<Bus> findByStatusNot(BusStatus status);
    List<Bus> findByAssignedRouteIsNotNull();
}
