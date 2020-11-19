package org.ak.project.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "simple")
@EnableCaching
public class SimpleCacheConfiguration {
}
