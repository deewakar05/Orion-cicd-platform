package com.devops.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MetricsDTO — Aggregated platform performance and CI/CD metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsDTO {

    // Build statistics
    private int totalBuilds;
    private int successfulBuilds;
    private int failedBuilds;
    private double buildSuccessRate;
    private String averageBuildDuration;

    // Deployment statistics
    private int totalDeployments;
    private int activeContainers;
    private String lastDeploymentTime;
    private String deploymentStatus;

    // System metrics
    private String jvmUptime;
    private long usedHeapMemoryMb;
    private long totalHeapMemoryMb;
    private int availableProcessors;

    // Service info
    private String serviceVersion;
    private String environment;
    private String timestamp;
}
