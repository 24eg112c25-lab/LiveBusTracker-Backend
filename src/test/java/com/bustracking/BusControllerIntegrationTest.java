package com.bustracking;

import com.bustracking.dto.BusDto;
import com.bustracking.dto.StopDto;
import com.bustracking.model.BusStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/buses should return HTTP 200 and JSON array")
    void testGetAllBuses() throws Exception {
        mockMvc.perform(get("/api/buses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/buses/locations should return HTTP 200")
    void testGetBusLocations() throws Exception {
        mockMvc.perform(get("/api/buses/locations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/buses with invalid payload should return HTTP 400 Bad Request with validation errors")
    void testCreateBusValidationFailure() throws Exception {
        BusDto invalidBus = new BusDto();
        invalidBus.setBusNumber(""); // Invalid blank
        invalidBus.setRegNumber(""); // Invalid blank
        invalidBus.setCapacity(0);   // Invalid min 1

        mockMvc.perform(post("/api/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBus)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.busNumber").exists())
                .andExpect(jsonPath("$.errors.regNumber").exists())
                .andExpect(jsonPath("$.errors.capacity").exists());
    }

    @Test
    @DisplayName("POST /api/buses with valid payload should return HTTP 201 Created")
    void testCreateBusSuccess() throws Exception {
        BusDto validBus = new BusDto();
        validBus.setBusNumber("BUS-INT-99");
        validBus.setRegNumber("KA-05-ZZ-9999");
        validBus.setCapacity(45);
        validBus.setDriverName("Test Driver");
        validBus.setStatus(BusStatus.IDLE);

        mockMvc.perform(post("/api/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBus)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.busNumber").value("BUS-INT-99"));
    }

    @Test
    @DisplayName("POST /api/stops with out-of-range coordinates should return HTTP 400")
    void testCreateStopInvalidCoordinates() throws Exception {
        StopDto invalidStop = new StopDto();
        invalidStop.setName("Invalid Coordinate Stop");
        invalidStop.setLatitude(120.0); // Out of bounds > 90
        invalidStop.setLongitude(77.5);

        mockMvc.perform(post("/api/stops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStop)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.latitude").exists());
    }

    @Test
    @DisplayName("GET /api/routes should return HTTP 200")
    void testGetAllRoutes() throws Exception {
        mockMvc.perform(get("/api/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
