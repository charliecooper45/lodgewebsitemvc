package uk.cooperca.lodge.website.mvc.config.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import uk.cooperca.lodge.website.mvc.config.CacheConfig;
import uk.cooperca.lodge.website.mvc.config.DataConfig;
import uk.cooperca.lodge.website.mvc.config.test.TestConfig;

/**
 * Conditionally imports configuration classes based on the current environment. This ensures we do not load the data
 * layer during testing.
 *
 * @author Charlie Cooper
 */
public class CoreConfigImportSelector implements ImportSelector, EnvironmentAware {

    private static final String TEST_PROFILE = "test";

    private Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String profile = environment.getActiveProfiles()[0];
        if (!profile.equals(TEST_PROFILE)) {
            return new String[]{CacheConfig.class.getName(), DataConfig.class.getName()};
        }
        return new String[]{CacheConfig.class.getName(), TestConfig.class.getName()};
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
