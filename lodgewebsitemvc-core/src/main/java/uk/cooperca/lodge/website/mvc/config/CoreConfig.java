package uk.cooperca.lodge.website.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.cooperca.lodge.website.mvc.config.util.CoreConfigImportSelector;

/**
 * The main configuration class for the core module. Imports other configuration classes.
 *
 * @author Charlie Cooper
 */
@Configuration
@Import(CoreConfigImportSelector.class)
public class CoreConfig {}
