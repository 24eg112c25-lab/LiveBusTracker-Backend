package com.bustracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class RouteDto {
    private Long id;

    @NotBlank(message = "Route number is required")
    @Size(max = 50, message = "Route number must not exceed 50 characters")
    private String routeNumber;

    @NotBlank(message = "Route name is required")
    @Size(max = 150, message = "Route name must not exceed 150 characters")
    private String routeName;

    @NotBlank(message = "Origin location is required")
    @Size(max = 150, message = "Origin location must not exceed 150 characters")
    private String origin;

    @NotBlank(message = "Destination location is required")
    @Size(max = 150, message = "Destination location must not exceed 150 characters")
    private String destination;

    private Double totalDistanceKm;
    private Boolean active = true;
    private List<RouteStopDto> stops = new ArrayList<>();

    public RouteDto() {
    }

    public RouteDto(Long id, String routeNumber, String routeName, String origin, String destination, Double totalDistanceKm, Boolean active, List<RouteStopDto> stops) {
        this.id = id;
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.totalDistanceKm = totalDistanceKm;
        this.active = active;
        this.stops = stops;
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

    public List<RouteStopDto> getStops() {
        return stops;
    }

    public void setStops(List<RouteStopDto> stops) {
        this.stops = stops;
    }
}
