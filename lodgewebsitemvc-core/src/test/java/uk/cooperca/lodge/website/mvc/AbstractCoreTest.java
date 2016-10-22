package uk.cooperca.lodge.website.mvc;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;
import uk.cooperca.lodge.website.mvc.config.SecurityConfig;

/**
 * Abstract base class that loads the {@link CoreConfig} configuration class in the dev Spring profile.
 *
 * @author Charlie Cooper
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, SecurityConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=dev", "jasypt.encryptor.password=dFt83FfMm33WR72DQJhPjs"})
public abstract class AbstractCoreTest {}
