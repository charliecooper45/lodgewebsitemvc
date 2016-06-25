package uk.cooperca.lodge.website.mvc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.cooperca.lodge.website.mvc.config.NotificationConfig;
import uk.cooperca.lodge.website.mvc.email.EmailService;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;

/**
 * The entry point to the notification module, starts the Spring Application Context.
 *
 * @author Charlie Cooper
 */
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(NotificationConfig.class);
        EmailService service = context.getBean(EmailService.class);
        service.sendEmail(NotificationMessage.Type.EMAIL_UPDATE, 1);
        context.close();
    }
}
