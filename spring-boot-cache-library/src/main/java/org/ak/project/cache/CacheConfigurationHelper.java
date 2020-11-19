package org.ak.project.cache;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "hazelcast")
public class CacheConfigurationHelper {
    private final HazelcastInstance instance;

    public void addCache(final String cacheName, final Integer cacheTtl, final int size) {
        final EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE);
        evictionConfig.setSize(size);
        instance.getConfig().addMapConfig(
                new MapConfig()
                        .setName(cacheName)
                        .setStatisticsEnabled(true)
                        .setEvictionConfig(evictionConfig)
                        .setMaxIdleSeconds(cacheTtl)
                        .setTimeToLiveSeconds(cacheTtl));
    }
}