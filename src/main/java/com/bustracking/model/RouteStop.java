package com.bustracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "route_stops", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"route_id", "stop_order"})
})
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    @JsonIgnoreProperties("routeStops")
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stop_id", nullable = false)
    private Stop stop;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(name = "distance_from_start_km")
    private Double distanceFromStartKm = 0.0;

    public RouteStop() {
    }

    public RouteStop(Route route, Stop stop, Integer stopOrder, Double distanceFromStartKm) {
        this.route = route;
        this.stop = stop;
        this.stopOrder = stopOrder;
        this.distanceFromStartKm = distanceFromStartKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public Integer getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(Integer stopOrder) {
        this.stopOrder = stopOrder;
    }

    public Double getDistanceFromStartKm() {
        return distanceFromStartKm;
    }

    public void setDistanceFromStartKm(Double distanceFromStartKm) {
        this.distanceFromStartKm = distanceFromStartKm;
    }
}
