package uk.cooperca.lodge.website.mvc.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.cooperca.lodge.website.mvc.config.logging.LoggingConfig;
import uk.cooperca.lodge.website.mvc.config.util.CoreConfigImportSelector;

/**
 * The main configuration class for the core module. Imports other configuration classes.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableScheduling
@Import({CoreConfigImportSelector.class, LoggingConfig.class})
public class CoreConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
