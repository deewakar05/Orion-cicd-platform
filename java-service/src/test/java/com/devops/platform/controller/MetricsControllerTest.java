package com.devops.platform.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MetricsControllerTest
 * Integration tests for the /metrics REST endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPlatformMetrics_shouldReturn200() throws Exception {
        mockMvc.perform(get("/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalBuilds").isNumber())
                .andExpect(jsonPath("$.data.buildSuccessRate").isNumber())
                .andExpect(jsonPath("$.data.availableProcessors").isNumber());
    }

    @Test
    void getBuildMetrics_shouldReturn200() throws Exception {
        mockMvc.perform(get("/metrics/build"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.successRate").exists());
    }

    @Test
    void getSystemMetrics_shouldReturn200WithJvmData() throws Exception {
        mockMvc.perform(get("/metrics/system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.jvmName").exists())
                .andExpect(jsonPath("$.data.heapUsedMb").isNumber());
    }
}
