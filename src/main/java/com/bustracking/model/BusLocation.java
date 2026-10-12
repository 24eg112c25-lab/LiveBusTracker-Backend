package com.bustracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_locations")
public class BusLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false, unique = true)
    @JsonIgnoreProperties("currentLocation")
    private Bus bus;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(nullable = false)
    private Double longitude;

    @Column(name = "speed_kmph")
    private Double speedKmph = 0.0;

    @Column(name = "heading")
    private Double heading = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_stop_id")
    private Stop currentStop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "next_stop_id")
    private Stop nextStop;

    @Column(name = "next_stop_name", length = 150)
    private String nextStopName;

    @Column(name = "distance_to_next_stop_km")
    private Double distanceToNextStopKm = 0.0;

    @Column(name = "eta_next_stop_minutes")
    private Integer etaNextStopMinutes = 0;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public BusLocation() {
    }

    public BusLocation(Bus bus, Double latitude, Double longitude, Double speedKmph, Double heading) {
        this.bus = bus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKmph = speedKmph;
        this.heading = heading;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public Stop getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(Stop currentStop) {
        this.currentStop = currentStop;
    }

    public Stop getNextStop() {
        return nextStop;
    }

    public void setNextStop(Stop nextStop) {
        this.nextStop = nextStop;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
