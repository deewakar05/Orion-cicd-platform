package com.devops.platform.controller;

import com.devops.platform.model.ApiResponse;
import com.devops.platform.model.LogEntryDTO;
import com.devops.platform.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LogController
 *
 * Exposes REST endpoints for application log retrieval.
 * Logs captured from both Node.js and Java services are surfaced here.
 */
@RestController
@RequestMapping("/logs")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    private final LoggingService loggingService;

    public LogController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    /**
     * GET /logs
     * Returns the most recent application log entries.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LogEntryDTO>>> getLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "INFO") String level) {

        log.info("Log request — limit={}, level={}", limit, level);
        List<LogEntryDTO> logs = loggingService.getRecentLogs(limit, level);
        return ResponseEntity.ok(ApiResponse.success("Logs retrieved", logs));
    }

    /**
     * GET /logs/errors
     * Returns only ERROR-level log entries.
     */
    @GetMapping("/errors")
    public ResponseEntity<ApiResponse<List<LogEntryDTO>>> getErrorLogs() {
        log.info("Fetching error logs");
        List<LogEntryDTO> errorLogs = loggingService.getErrorLogs();
        return ResponseEntity.ok(ApiResponse.success("Error logs retrieved", errorLogs));
    }

    /**
     * GET /logs/services/{serviceName}
     * Returns logs filtered by service name.
     */
    @GetMapping("/services/{serviceName}")
    public ResponseEntity<ApiResponse<List<LogEntryDTO>>> getLogsByService(
            @PathVariable String serviceName) {
        log.info("Fetching logs for service: {}", serviceName);
        List<LogEntryDTO> serviceLogs = loggingService.getLogsByService(serviceName);
        return ResponseEntity.ok(ApiResponse.success("Service logs retrieved", serviceLogs));
    }
}
