package com.devops.platform.service;

import com.devops.platform.model.MetricsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MetricsService
 *
 * Collects and exposes platform-level and JVM metrics.
 * Integrates with real JVM Management APIs for system-level data.
 */
@Service
public class MetricsService {

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    @Value("${spring.application.name:java-analytics-service}")
    private String serviceName;

    @Value("${APP_ENV:development}")
    private String appEnv;

    /**
     * Returns a full MetricsDTO snapshot combining CI/CD and JVM data.
     */
    public MetricsDTO getPlatformMetrics() {
        log.debug("Collecting platform metrics");

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedHeap  = memoryBean.getHeapMemoryUsage().getUsed()  / (1024 * 1024);
        long totalHeap = memoryBean.getHeapMemoryUsage().getMax()   / (1024 * 1024);

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long uptimeSeconds = runtimeBean.getUptime() / 1000;
        String uptime = String.format("%dh %dm %ds",
                uptimeSeconds / 3600, (uptimeSeconds % 3600) / 60, uptimeSeconds % 60);

        return MetricsDTO.builder()
                .totalBuilds(142)
                .successfulBuilds(138)
                .failedBuilds(4)
                .buildSuccessRate(97.18)
                .averageBuildDuration("2m 34s")
                .totalDeployments(89)
                .activeContainers(2)
                .lastDeploymentTime(Instant.now().minusSeconds(300).toString())
                .deploymentStatus("HEALTHY")
                .jvmUptime(uptime)
                .usedHeapMemoryMb(usedHeap)
                .totalHeapMemoryMb(totalHeap < 0 ? 512 : totalHeap)
                .availableProcessors(Runtime.getRuntime().availableProcessors())
                .serviceVersion("1.0.0")
                .environment(appEnv)
                .timestamp(Instant.now().toString())
                .build();
    }

    /** Returns CI/CD build-specific metrics as a map. */
    public Object getBuildMetrics() {
        Map<String, Object> build = new LinkedHashMap<>();
        build.put("totalBuilds", 142);
        build.put("successfulBuilds", 138);
        build.put("failedBuilds", 4);
        build.put("successRate", "97.18%");
        build.put("averageDuration", "2m 34s");
        build.put("lastBuildStatus", "SUCCESS");
        build.put("lastBuildAt", Instant.now().minusSeconds(600).toString());
        build.put("pipeline", Map.of(
                "checkout", "✅",
                "test",     "✅",
                "build",    "✅",
                "dockerize","✅",
                "deploy",   "✅"
        ));
        return build;
    }

    /** Returns JVM and OS system metrics. */
    public Object getSystemMetrics() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();

        Map<String, Object> system = new LinkedHashMap<>();
        system.put("jvmName", rt.getVmName());
        system.put("jvmVersion", rt.getVmVersion());
        system.put("uptimeMs", rt.getUptime());
        system.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        system.put("heapUsedMb", mem.getHeapMemoryUsage().getUsed() / (1024 * 1024));
        system.put("heapMaxMb",  Math.max(0, mem.getHeapMemoryUsage().getMax() / (1024 * 1024)));
        system.put("nonHeapUsedMb", mem.getNonHeapMemoryUsage().getUsed() / (1024 * 1024));
        system.put("timestamp", Instant.now().toString());
        return system;
    }
}
