package com.bustracking.dto;

public class SimulationStatusDto {
    private boolean running;
    private double speedMultiplier;
    private int activeBusesCount;
    private long totalTicks;
    private String statusMessage;

    public SimulationStatusDto() {
    }

    public SimulationStatusDto(boolean running, double speedMultiplier, int activeBusesCount, long totalTicks, String statusMessage) {
        this.running = running;
        this.speedMultiplier = speedMultiplier;
        this.activeBusesCount = activeBusesCount;
        this.totalTicks = totalTicks;
        this.statusMessage = statusMessage;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public int getActiveBusesCount() {
        return activeBusesCount;
    }

    public void setActiveBusesCount(int activeBusesCount) {
        this.activeBusesCount = activeBusesCount;
    }

    public long getTotalTicks() {
        return totalTicks;
    }

    public void setTotalTicks(long totalTicks) {
        this.totalTicks = totalTicks;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
