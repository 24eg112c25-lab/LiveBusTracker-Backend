package com.bustracking.service;

public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculates distance between two points in kilometers using Haversine formula.
     */
    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(rLat1) * Math.cos(rLat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(EARTH_RADIUS_KM * c * 1000.0) / 1000.0; // Rounded to 3 decimal places
    }

    /**
     * Calculates initial bearing (heading) in degrees (0 - 360) from point 1 to point 2.
     */
    public static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);
        double dLon = Math.toRadians(lon2 - lon1);

        double y = Math.sin(dLon) * Math.cos(rLat2);
        double x = Math.cos(rLat1) * Math.sin(rLat2) -
                   Math.sin(rLat1) * Math.cos(rLat2) * Math.cos(dLon);

        double bearingRad = Math.atan2(y, x);
        double bearingDeg = Math.toDegrees(bearingRad);
        return (bearingDeg + 360.0) % 360.0;
    }

    /**
     * Linearly interpolates between two coordinates based on factor t [0.0, 1.0].
     */
    public static double interpolate(double start, double end, double t) {
        return start + t * (end - start);
    }

    /**
     * Calculates ETA in minutes given distance in km and average speed in km/h.
     */
    public static int calculateEtaMinutes(double distanceKm, double speedKmph) {
        if (speedKmph <= 0.0) {
            speedKmph = 20.0; // default assumed urban bus speed
        }
        double hours = distanceKm / speedKmph;
        int minutes = (int) Math.ceil(hours * 60.0);
        return Math.max(1, minutes);
    }
}
