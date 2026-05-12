package com.devops.platform.controller;

import com.devops.platform.model.ApiResponse;
import com.devops.platform.model.ReportDTO;
import com.devops.platform.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReportController
 *
 * Exposes REST endpoints for report generation and retrieval.
 * This is the primary analytics endpoint consumed by the Node.js API Gateway.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /reports
     * Returns a list of all available analytics reports.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportDTO>>> getAllReports() {
        log.info("Fetching all reports — request received from API gateway");
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(ApiResponse.success("Reports retrieved successfully", reports));
    }

    /**
     * GET /reports/{id}
     * Returns a single report by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportDTO>> getReportById(@PathVariable String id) {
        log.info("Fetching report with id: {}", id);
        return reportService.findById(id)
                .map(report -> ResponseEntity.ok(ApiResponse.success("Report found", report)))
                .orElse(ResponseEntity.status(404)
                        .body(ApiResponse.error("Report not found with id: " + id)));
    }

    /**
     * GET /reports/summary
     * Returns an aggregated summary of all report statistics.
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Object>> getReportSummary() {
        log.info("Generating report summary");
        Object summary = reportService.getSummary();
        return ResponseEntity.ok(ApiResponse.success("Summary generated", summary));
    }
}
