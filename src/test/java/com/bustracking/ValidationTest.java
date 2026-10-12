package com.bustracking;

import com.bustracking.dto.BusDto;
import com.bustracking.dto.RouteDto;
import com.bustracking.dto.StopDto;
import com.bustracking.model.BusStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("BusDto with blank busNumber or capacity 0 should fail validation")
    void testBusDtoValidationFailure() {
        BusDto invalidBus = new BusDto();
        invalidBus.setBusNumber(""); // Blank
        invalidBus.setRegNumber("KA-01-1234");
        invalidBus.setCapacity(0); // Min is 1
        invalidBus.setStatus(BusStatus.IDLE);

        Set<ConstraintViolation<BusDto>> violations = validator.validate(invalidBus);
        assertFalse(violations.isEmpty(), "Violations must be present for blank busNumber and 0 capacity");

        boolean hasBusNumberError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("busNumber"));
        boolean hasCapacityError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("capacity"));

        assertTrue(hasBusNumberError, "busNumber must have validation error");
        assertTrue(hasCapacityError, "capacity must have validation error");
    }

    @Test
    @DisplayName("BusDto with valid attributes should pass validation")
    void testBusDtoValidationSuccess() {
        BusDto validBus = new BusDto();
        validBus.setBusNumber("BUS-TEST-1");
        validBus.setRegNumber("KA-01-9999");
        validBus.setCapacity(40);
        validBus.setStatus(BusStatus.IDLE);

        Set<ConstraintViolation<BusDto>> violations = validator.validate(validBus);
        assertTrue(violations.isEmpty(), "Valid bus should have zero violations");
    }

    @Test
    @DisplayName("StopDto with invalid latitude > 90 should fail validation")
    void testStopDtoInvalidCoordinates() {
        StopDto invalidStop = new StopDto();
        invalidStop.setName("Out of Bounds Stop");
        invalidStop.setLatitude(95.0); // Invalid lat > 90
        invalidStop.setLongitude(77.5);

        Set<ConstraintViolation<StopDto>> violations = validator.validate(invalidStop);
        assertFalse(violations.isEmpty(), "Latitude > 90 should fail validation");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("latitude")));
    }

    @Test
    @DisplayName("RouteDto with blank routeNumber should fail validation")
    void testRouteDtoBlankFields() {
        RouteDto invalidRoute = new RouteDto();
        invalidRoute.setRouteNumber("");
        invalidRoute.setRouteName("Campus Shuttle");
        invalidRoute.setOrigin("Gate 1");
        invalidRoute.setDestination("Gate 2");

        Set<ConstraintViolation<RouteDto>> violations = validator.validate(invalidRoute);
        assertFalse(violations.isEmpty(), "Blank routeNumber should fail validation");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("routeNumber")));
    }
}
