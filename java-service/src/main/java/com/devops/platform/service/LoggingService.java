package com.devops.platform.service;

import com.devops.platform.model.LogEntryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LoggingService
 *
 * Manages and retrieves application log entries.
 * In production, logs would be stored in Elasticsearch or a log aggregator.
 */
@Service
public class LoggingService {

    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    private final List<LogEntryDTO> logStore;

    public LoggingService() {
        this.logStore = generateSeedLogs();
    }

    /**
     * Returns the most recent log entries, filtered by level.
     *
     * @param limit max number of entries to return
     * @param level minimum log level (INFO, WARN, ERROR, DEBUG)
     */
    public List<LogEntryDTO> getRecentLogs(int limit, String level) {
        log.debug("Retrieving logs — limit={}, level={}", limit, level);

        return logStore.stream()
                .filter(l -> isLevelAtLeast(l.getLevel(), level))
                .sorted(Comparator.comparing(LogEntryDTO::getTimestamp).reversed())
                .limit(Math.max(1, Math.min(limit, 200)))
                .collect(Collectors.toList());
    }

    /** Returns only ERROR-level entries. */
    public List<LogEntryDTO> getErrorLogs() {
        return getRecentLogs(50, "ERROR");
    }

    /** Returns logs for a specific service. */
    public List<LogEntryDTO> getLogsByService(String serviceName) {
        return logStore.stream()
                .filter(l -> serviceName.equalsIgnoreCase(l.getService()))
                .sorted(Comparator.comparing(LogEntryDTO::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private boolean isLevelAtLeast(String entryLevel, String minLevel) {
        Map<String, Integer> levelOrder = Map.of(
                "DEBUG", 0, "INFO", 1, "WARN", 2, "ERROR", 3);
        int entry = levelOrder.getOrDefault(entryLevel.toUpperCase(), 1);
        int min   = levelOrder.getOrDefault(minLevel.toUpperCase(), 1);
        return entry >= min;
    }

    // ─── Seed Data ─────────────────────────────────────────────────────────────

    private List<LogEntryDTO> generateSeedLogs() {
        List<LogEntryDTO> entries = new ArrayList<>();
        String[] services = {"node-service", "java-service", "docker"};
        String[] levels   = {"INFO", "INFO", "INFO", "WARN", "ERROR", "DEBUG"};
        String[] messages = {
            "Application started successfully",
            "Incoming request: GET /health",
            "Build triggered by push to main branch",
            "Dependency version mismatch detected — using fallback",
            "Container health check failed — retrying in 30s",
            "JWT token issued for user: dev@example.com",
            "Maven build completed in 1m 52s",
            "Docker image pushed to registry: ghcr.io/org/java-service:latest",
            "GitHub Actions workflow completed with status: success",
            "Analytics data refreshed from upstream service",
        };

        for (int i = 0; i < 20; i++) {
            entries.add(LogEntryDTO.builder()
                    .id("LOG-" + String.format("%03d", i + 1))
                    .level(levels[i % levels.length])
                    .message(messages[i % messages.length])
                    .service(services[i % services.length])
                    .timestamp(Instant.now().minusSeconds((long) i * 180).toString())
                    .traceId("trace-" + UUID.randomUUID().toString().substring(0, 8))
                    .build());
        }

        return entries;
    }
}
