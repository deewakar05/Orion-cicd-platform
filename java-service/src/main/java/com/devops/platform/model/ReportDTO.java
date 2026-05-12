package com.devops.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReportDTO — Data Transfer Object for analytics reports.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private String id;
    private String title;
    private String type;           // e.g., DEPLOYMENT, BUILD, TEST
    private String status;         // e.g., SUCCESS, FAILED, PENDING
    private String service;        // Which service generated this report
    private String generatedAt;
    private Object data;           // Flexible payload
    private String description;
}
