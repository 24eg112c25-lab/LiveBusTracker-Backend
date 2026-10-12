package com.bustracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Route number is required")
    @Size(max = 50, message = "Route number must not exceed 50 characters")
    @Column(name = "route_number", nullable = false, unique = true, length = 50)
    private String routeNumber;

    @NotBlank(message = "Route name is required")
    @Size(max = 150, message = "Route name must not exceed 150 characters")
    @Column(name = "route_name", nullable = false, length = 150)
    private String routeName;

    @NotBlank(message = "Origin location is required")
    @Size(max = 150, message = "Origin location must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String origin;

    @NotBlank(message = "Destination location is required")
    @Size(max = 150, message = "Destination location must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String destination;

    @Column(name = "total_distance_km")
    private Double totalDistanceKm = 0.0;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("stopOrder ASC")
    @JsonIgnoreProperties("route")
    private List<RouteStop> routeStops = new ArrayList<>();

    public Route() {
    }

    public Route(String routeNumber, String routeName, String origin, String destination, Double totalDistanceKm, Boolean active) {
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.totalDistanceKm = totalDistanceKm;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<RouteStop> getRouteStops() {
        return routeStops;
    }

    public void setRouteStops(List<RouteStop> routeStops) {
        this.routeStops = routeStops;
    }
}
