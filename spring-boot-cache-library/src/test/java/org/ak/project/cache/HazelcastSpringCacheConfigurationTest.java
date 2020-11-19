package org.ak.project.cache;

import com.hazelcast.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
@ActiveProfiles("redis-cache")
class HazelcastSpringCacheConfigurationTest {

    @Autowired
    Config hazelcastConfig;

    @Test
    void testThatCacheManagerExists() {
        assertNotNull(hazelcastConfig);
    }

}
