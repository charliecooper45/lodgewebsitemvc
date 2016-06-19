package uk.cooperca.lodge.website.mvc.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.config.CoreConfig;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;

import java.util.Scanner;

import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.EMAIL_UPDATE;

/**
 * Sends messages to the notification backend module.
 *
 * @author Charlie Cooper
 */
@Component
public class NotificationMessageProducer {

    @Autowired
    private RabbitTemplate template;

    public void sendMessage(NotificationMessage.Type type, int id) {
        template.convertAndSend(new NotificationMessage(type, id));
    }

    // TODO: remove
    // test code
    public static void main(String[] args)throws Exception {
        ApplicationContext context =
            new AnnotationConfigApplicationContext(CoreConfig.class);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while(!exit) {
            String value = scanner.next();
            if (!value.equals("exit")) {
                AmqpTemplate template = context.getBean(AmqpTemplate.class);
                template.convertAndSend(new NotificationMessage(EMAIL_UPDATE, 1));
                System.out.println("Message Sent!");
            } else {
                exit = true;
            }
        }
        System.out.println("Exited application!");
        ((ConfigurableApplicationContext) context).close();
    }
}
