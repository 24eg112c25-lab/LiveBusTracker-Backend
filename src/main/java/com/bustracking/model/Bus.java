package com.bustracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bus identifier is required")
    @Size(max = 50, message = "Bus identifier must not exceed 50 characters")
    @Column(name = "bus_number", nullable = false, unique = true, length = 50)
    private String busNumber;

    @NotBlank(message = "Registration / license plate number is required")
    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    @Column(name = "reg_number", nullable = false, length = 50)
    private String regNumber;

    @NotNull(message = "Passenger capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column
    private Integer capacity = 40;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "driver_phone", length = 20)
    private String driverPhone;

    @NotNull(message = "Bus operational status is required")
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private BusStatus status = BusStatus.IDLE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_route_id")
    @JsonIgnoreProperties("routeStops")
    private Route assignedRoute;

    @OneToOne(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("bus")
    private BusLocation currentLocation;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Bus() {
    }

    public Bus(String busNumber, String regNumber, Integer capacity, String driverName, String driverPhone, Route assignedRoute) {
        this.busNumber = busNumber;
        this.regNumber = regNumber;
        this.capacity = capacity;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.assignedRoute = assignedRoute;
        this.status = BusStatus.IDLE;
        this.lastUpdated = LocalDateTime.now();
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

    public Route getAssignedRoute() {
        return assignedRoute;
    }

    public void setAssignedRoute(Route assignedRoute) {
        this.assignedRoute = assignedRoute;
    }

    public BusLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(BusLocation currentLocation) {
        this.currentLocation = currentLocation;
        if (currentLocation != null) {
            currentLocation.setBus(this);
        }
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
