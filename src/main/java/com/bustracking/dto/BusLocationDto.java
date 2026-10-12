package com.bustracking.dto;

import java.time.LocalDateTime;

public class BusLocationDto {
    private Long id;
    private Long busId;
    private String busNumber;
    private Double latitude;
    private Double longitude;
    private Double speedKmph;
    private Double heading;
    private Long currentStopId;
    private Long nextStopId;
    private String nextStopName;
    private Double distanceToNextStopKm;
    private Integer etaNextStopMinutes;
    private LocalDateTime timestamp;

    public BusLocationDto() {
    }

    public BusLocationDto(Long id, Long busId, String busNumber, Double latitude, Double longitude,
                          Double speedKmph, Double heading, Long currentStopId, Long nextStopId,
                          String nextStopName, Double distanceToNextStopKm, Integer etaNextStopMinutes,
                          LocalDateTime timestamp) {
        this.id = id;
        this.busId = busId;
        this.busNumber = busNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKmph = speedKmph;
        this.heading = heading;
        this.currentStopId = currentStopId;
        this.nextStopId = nextStopId;
        this.nextStopName = nextStopName;
        this.distanceToNextStopKm = distanceToNextStopKm;
        this.etaNextStopMinutes = etaNextStopMinutes;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
