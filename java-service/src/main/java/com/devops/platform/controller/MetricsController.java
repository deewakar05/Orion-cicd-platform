package com.devops.platform.controller;

import com.devops.platform.model.ApiResponse;
import com.devops.platform.model.MetricsDTO;
import com.devops.platform.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MetricsController
 *
 * Exposes REST endpoints for platform performance metrics.
 * Returns CI/CD pipeline metrics, build statistics, and system info.
 */
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static final Logger log = LoggerFactory.getLogger(MetricsController.class);

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * GET /metrics
     * Returns an aggregated platform metrics snapshot.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<MetricsDTO>> getMetrics() {
        log.info("Platform metrics requested");
        MetricsDTO metrics = metricsService.getPlatformMetrics();
        return ResponseEntity.ok(ApiResponse.success("Metrics retrieved", metrics));
    }

    /**
     * GET /metrics/build
     * Returns CI/CD build-specific metrics.
     */
    @GetMapping("/build")
    public ResponseEntity<ApiResponse<Object>> getBuildMetrics() {
        log.info("Build metrics requested");
        return ResponseEntity.ok(ApiResponse.success("Build metrics", metricsService.getBuildMetrics()));
    }

    /**
     * GET /metrics/system
     * Returns JVM and system-level metrics.
     */
    @GetMapping("/system")
    public ResponseEntity<ApiResponse<Object>> getSystemMetrics() {
        log.info("System metrics requested");
        return ResponseEntity.ok(ApiResponse.success("System metrics", metricsService.getSystemMetrics()));
    }
}
