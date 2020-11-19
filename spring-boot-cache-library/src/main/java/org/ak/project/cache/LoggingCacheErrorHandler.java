package org.ak.project.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class LoggingCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        log.error("Unable to get entry from cache", e);
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        log.error("Unable to put entry in cache", e);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        log.error("Unable to evict entry from cache", e);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        log.error("Unable to clear cache", e);
    }
}
