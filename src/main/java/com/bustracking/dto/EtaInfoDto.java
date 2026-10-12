package com.bustracking.dto;

public class EtaInfoDto {
    private Long stopId;
    private String stopName;
    private String landmark;
    private Integer stopOrder;
    private Double distanceKm;
    private Integer etaMinutes;
    private String status; // "PASSED", "CURRENT_STOP", "NEXT_STOP", "UPCOMING"

    public EtaInfoDto() {
    }

    public EtaInfoDto(Long stopId, String stopName, String landmark, Integer stopOrder, Double distanceKm, Integer etaMinutes, String status) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.landmark = landmark;
        this.stopOrder = stopOrder;
        this.distanceKm = distanceKm;
        this.etaMinutes = etaMinutes;
        this.status = status;
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

    public Integer getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(Integer stopOrder) {
        this.stopOrder = stopOrder;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEtaMinutes() {
        return etaMinutes;
    }

    public void setEtaMinutes(Integer etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
