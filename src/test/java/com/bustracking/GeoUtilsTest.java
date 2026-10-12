package com.bustracking;

import com.bustracking.service.GeoUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoUtilsTest {

    @Test
    @DisplayName("Haversine formula should calculate accurate distance between campus points")
    void testCalculateDistanceKm() {
        // Point 1: Campus Main Gate (12.9716, 77.59456)
        // Point 2: Central Library (12.9754, 77.5998)
        double distance = GeoUtils.calculateDistanceKm(12.9716, 77.59456, 12.9754, 77.5998);
        assertTrue(distance > 0.5 && distance < 1.0, "Distance should be approximately 0.7 km");

        // Distance between identical points should be 0
        double zeroDist = GeoUtils.calculateDistanceKm(12.9716, 77.59456, 12.9716, 77.59456);
        assertEquals(0.0, zeroDist, 0.001);
    }

    @Test
    @DisplayName("Bearing calculation should yield compass direction in range 0-360")
    void testCalculateBearing() {
        double bearing = GeoUtils.calculateBearing(12.9716, 77.59456, 12.9754, 77.5998);
        assertTrue(bearing >= 0.0 && bearing <= 360.0, "Bearing must be between 0 and 360 degrees");
    }

    @Test
    @DisplayName("Interpolation should accurately compute midpoint when t = 0.5")
    void testInterpolate() {
        double midpoint = GeoUtils.interpolate(10.0, 20.0, 0.5);
        assertEquals(15.0, midpoint, 0.001);

        double start = GeoUtils.interpolate(10.0, 20.0, 0.0);
        assertEquals(10.0, start, 0.001);

        double end = GeoUtils.interpolate(10.0, 20.0, 1.0);
        assertEquals(20.0, end, 0.001);
    }

    @Test
    @DisplayName("ETA calculation should return at least 1 minute for positive distances")
    void testCalculateEtaMinutes() {
        int eta = GeoUtils.calculateEtaMinutes(1.5, 30.0); // 1.5 km at 30 km/h = 3 minutes
        assertEquals(3, eta);

        int minEta = GeoUtils.calculateEtaMinutes(0.05, 30.0);
        assertEquals(1, minEta, "Minimum ETA should be 1 minute");
    }
}
