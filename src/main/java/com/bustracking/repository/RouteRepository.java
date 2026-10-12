package com.bustracking.repository;

import com.bustracking.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByRouteNumber(String routeNumber);
    List<Route> findByActiveTrue();

    @Query("SELECT DISTINCT r FROM Route r JOIN r.routeStops rs1 JOIN r.routeStops rs2 " +
           "WHERE rs1.stop.id = :originStopId AND rs2.stop.id = :destStopId " +
           "AND rs1.stopOrder < rs2.stopOrder AND r.active = true")
    List<Route> findRoutesBetweenStops(@Param("originStopId") Long originStopId, 
                                      @Param("destStopId") Long destStopId);
}
