package uk.cooperca.lodge.website.mvc.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A configuration class that configures our caching layer.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // TODO: TTL, add/delete from cache
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("reviews");
    }
}
