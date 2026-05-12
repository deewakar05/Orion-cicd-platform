package com.devops.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * ApplicationContextTest
 * Verifies the Spring application context loads without errors.
 */
@SpringBootTest
@TestPropertySource(properties = {"APP_ENV=test"})
class JavaAnalyticsServiceApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, the Spring context started correctly
    }
}
