package uk.cooperca.lodge.website.mvc.config.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;
import uk.cooperca.lodge.website.mvc.config.test.TestConfig;

/**
 * Conditionally imports configuration classes based on the current environment. This allows us to
 * load mock objects during testing.
 *
 * @author Charlie Cooper
 */
public class WebMvcConfigImportSelector implements ImportSelector, EnvironmentAware {

    private static final String TEST_PROFILE = "test";

    private Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String profile = environment.getActiveProfiles()[0];
        if (profile.equals(TEST_PROFILE)) {
            return new String[]{TestConfig.class.getName()};
        }
        return new String[]{CoreConfig.class.getName()};
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
