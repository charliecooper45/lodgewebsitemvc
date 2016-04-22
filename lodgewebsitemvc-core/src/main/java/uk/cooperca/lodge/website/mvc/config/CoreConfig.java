package uk.cooperca.lodge.website.mvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The main configuration class for the core module. Imports other configuration classes.
 *
 * @author Charlie Cooper
 */
@Configuration
@ComponentScan(basePackages = { "uk.cooperca.lodge.website.mvc.service" })
@Import({CacheConfig.class, DataConfig.class})
public class CoreConfig {
}
