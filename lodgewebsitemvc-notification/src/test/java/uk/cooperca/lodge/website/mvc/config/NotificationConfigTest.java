package uk.cooperca.lodge.website.mvc.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.service.impl.EmailNotificationService;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { NotificationConfig.class })
@TestPropertySource(properties = {"spring.profiles.active=dev", "jasypt.encryptor.password=dFt83FfMm33WR72DQJhPjs"})
@DirtiesContext
public class NotificationConfigTest {

    @Autowired
    private EmailNotificationService notifier;

    @Test
    public void test() {
        assertNotNull(notifier);
    }
}
