package uk.cooperca.lodge.website.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.cooperca.lodge.website.mvc.config.util.CoreConfigImportSelector;

/**
 * The main configuration class for the core module. Imports other configuration classes.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableScheduling
@Import(CoreConfigImportSelector.class)
public class CoreConfig {}
