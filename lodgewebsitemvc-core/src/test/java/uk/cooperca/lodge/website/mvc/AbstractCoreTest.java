package uk.cooperca.lodge.website.mvc;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;

/**
 * Abstract base class for all test classes that load the {@link uk.cooperca.lodge.website.mvc.config.CoreConfig}
 * configuration class.
 *
 * @author Charlie Cooper
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=password"})
public abstract class AbstractCoreTest {}
