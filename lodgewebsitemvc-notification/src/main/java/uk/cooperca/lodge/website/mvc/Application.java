package uk.cooperca.lodge.website.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.cooperca.lodge.website.mvc.config.NotificationConfig;

/**
 * The entry point to the notification module, starts the Spring Application Context.
 *
 * @author Charlie Cooper
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(NotificationConfig.class);
        LOGGER.info("Started notification application...");
    }
}
