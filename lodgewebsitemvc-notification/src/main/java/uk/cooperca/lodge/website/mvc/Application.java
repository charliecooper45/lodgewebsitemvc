package uk.cooperca.lodge.website.mvc;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.cooperca.lodge.website.mvc.config.NotificationConfig;

/**
 * The entry point to the notification module, starts the Spring Application Context.
 *
 * @author Charlie Cooper
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.logger(Application.class);

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(NotificationConfig.class);
        LOGGER.info("Started notification application...");
    }
}
