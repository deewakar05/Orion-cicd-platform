package com.devops.platform.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ReportControllerTest
 * Integration tests for the /reports REST endpoints using MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllReports_shouldReturn200WithReportList() throws Exception {
        mockMvc.perform(get("/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getReportById_existingId_shouldReturn200() throws Exception {
        mockMvc.perform(get("/reports/RPT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("RPT-001"))
                .andExpect(jsonPath("$.data.title").exists());
    }

    @Test
    void getReportById_nonExistentId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/reports/RPT-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getReportSummary_shouldReturn200WithSummary() throws Exception {
        mockMvc.perform(get("/reports/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.successRate").isNumber());
    }
}
