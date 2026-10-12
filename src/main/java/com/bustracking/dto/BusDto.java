package com.bustracking.dto;

import com.bustracking.model.BusStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class BusDto {
    private Long id;

    @NotBlank(message = "Bus number is required")
    @Size(max = 50, message = "Bus number must not exceed 50 characters")
    private String busNumber;

    @NotBlank(message = "Registration number is required")
    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String regNumber;

    @NotNull(message = "Passenger capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity = 40;

    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    @Size(max = 20, message = "Driver phone must not exceed 20 characters")
    private String driverPhone;

    @NotNull(message = "Bus status is required")
    private BusStatus status = BusStatus.IDLE;

    private Double currentLatitude;
    private Double currentLongitude;
    private Double speedKmph = 0.0;
    private Double heading = 0.0;
    private Long routeId;
    private String routeNumber;
    private String routeName;
    private Long currentStopId;
    private Long nextStopId;
    private String nextStopName;
    private Double distanceToNextStopKm = 0.0;
    private Integer etaNextStopMinutes = 0;
    private LocalDateTime lastUpdated;
    private BusLocationDto location;

    public BusDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public BusStatus getStatus() {
        return status;
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getSpeedKmph() {
        return speedKmph;
    }

    public void setSpeedKmph(Double speedKmph) {
        this.speedKmph = speedKmph;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
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

    public Long getCurrentStopId() {
        return currentStopId;
    }

    public void setCurrentStopId(Long currentStopId) {
        this.currentStopId = currentStopId;
    }

    public Long getNextStopId() {
        return nextStopId;
    }

    public void setNextStopId(Long nextStopId) {
        this.nextStopId = nextStopId;
    }

    public String getNextStopName() {
        return nextStopName;
    }

    public void setNextStopName(String nextStopName) {
        this.nextStopName = nextStopName;
    }

    public Double getDistanceToNextStopKm() {
        return distanceToNextStopKm;
    }

    public void setDistanceToNextStopKm(Double distanceToNextStopKm) {
        this.distanceToNextStopKm = distanceToNextStopKm;
    }

    public Integer getEtaNextStopMinutes() {
        return etaNextStopMinutes;
    }

    public void setEtaNextStopMinutes(Integer etaNextStopMinutes) {
        this.etaNextStopMinutes = etaNextStopMinutes;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BusLocationDto getLocation() {
        return location;
    }

    public void setLocation(BusLocationDto location) {
        this.location = location;
    }
}
