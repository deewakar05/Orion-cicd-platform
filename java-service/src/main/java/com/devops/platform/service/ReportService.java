package com.devops.platform.service;

import com.devops.platform.model.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

/**
 * ReportService
 *
 * Business logic layer for generating and managing analytics reports.
 * In production, this would query a database or external reporting system.
 */
@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    // Simulated in-memory report store
    private final List<ReportDTO> reportStore;

    public ReportService() {
        this.reportStore = generateSeedReports();
    }

    /** Returns all stored reports. */
    public List<ReportDTO> getAllReports() {
        log.debug("Retrieving {} reports", reportStore.size());
        return Collections.unmodifiableList(reportStore);
    }

    /** Finds a single report by ID. */
    public Optional<ReportDTO> findById(String id) {
        return reportStore.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    /** Returns an aggregated summary of all reports. */
    public Object getSummary() {
        long successCount = reportStore.stream()
                .filter(r -> "SUCCESS".equals(r.getStatus())).count();
        long failedCount = reportStore.stream()
                .filter(r -> "FAILED".equals(r.getStatus())).count();

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", reportStore.size());
        summary.put("successful", successCount);
        summary.put("failed", failedCount);
        summary.put("successRate", reportStore.isEmpty() ? 0.0
                : Math.round((double) successCount / reportStore.size() * 10000.0) / 100.0);
        summary.put("generatedAt", Instant.now().toString());

        return summary;
    }

    // ─── Seed Data ─────────────────────────────────────────────────────────────

    private List<ReportDTO> generateSeedReports() {
        List<ReportDTO> reports = new ArrayList<>();

        reports.add(ReportDTO.builder()
                .id("RPT-001")
                .title("GitHub Actions Build Report")
                .type("BUILD")
                .status("SUCCESS")
                .service("java-service")
                .generatedAt(Instant.now().minusSeconds(3600).toString())
                .description("Maven build completed successfully. All 12 tests passed.")
                .data(Map.of("duration", "1m 52s", "testsPassed", 12, "testsFailed", 0))
                .build());

        reports.add(ReportDTO.builder()
                .id("RPT-002")
                .title("Docker Image Build Report")
                .type("DEPLOYMENT")
                .status("SUCCESS")
                .service("node-service")
                .generatedAt(Instant.now().minusSeconds(1800).toString())
                .description("Docker image built and pushed to registry.")
                .data(Map.of("imageTag", "latest", "layers", 8, "compressedSize", "187MB"))
                .build());

        reports.add(ReportDTO.builder()
                .id("RPT-003")
                .title("Integration Test Report")
                .type("TEST")
                .status("SUCCESS")
                .service("node-service")
                .generatedAt(Instant.now().minusSeconds(900).toString())
                .description("Node.js integration tests passed with 94% coverage.")
                .data(Map.of("coverage", "94%", "duration", "34s", "totalTests", 8))
                .build());

        reports.add(ReportDTO.builder()
                .id("RPT-004")
                .title("Deployment Verification Report")
                .type("DEPLOYMENT")
                .status("SUCCESS")
                .service("docker-compose")
                .generatedAt(Instant.now().minusSeconds(300).toString())
                .description("All containers started and health checks passed.")
                .data(Map.of("containersUp", 2, "healthChecksPassed", 2))
                .build());

        return reports;
    }
}
