package com.devops.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LogEntryDTO — Data Transfer Object for application log entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntryDTO {

    private String id;
    private String level;       // INFO, WARN, ERROR, DEBUG
    private String message;
    private String service;     // Source service (node-service / java-service)
    private String timestamp;
    private String traceId;     // Optional distributed trace ID
}
