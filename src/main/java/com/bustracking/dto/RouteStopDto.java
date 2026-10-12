package com.bustracking.dto;

public class RouteStopDto {
    private Long id;
    private Long stopId;
    private String stopName;
    private String landmark;
    private Double latitude;
    private Double longitude;
    private Integer stopOrder;
    private Double distanceFromStartKm;

    public RouteStopDto() {
    }

    public RouteStopDto(Long id, Long stopId, String stopName, String landmark, Double latitude, Double longitude, Integer stopOrder, Double distanceFromStartKm) {
        this.id = id;
        this.stopId = stopId;
        this.stopName = stopName;
        this.landmark = landmark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopOrder = stopOrder;
        this.distanceFromStartKm = distanceFromStartKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
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
