package org.ak.project.cache;

import com.hazelcast.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
@ActiveProfiles("simple-cache")
/**
 * Verify that if spring.cache.type is simple we will not configure a hazelcast Config
 */
class SpringSimpleCacheConfigurationTest {

    @Autowired(required = false)
    Config hazelcastConfig;

    @Test
    void testThatCacheManagerExists() {
        assertNull(hazelcastConfig);
    }

}
