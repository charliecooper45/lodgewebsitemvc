package uk.cooperca.lodge.website.mvc.consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;
import uk.cooperca.lodge.website.mvc.config.test.TestConfig;
import uk.cooperca.lodge.website.mvc.service.impl.EmailNotificationService;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static uk.cooperca.lodge.website.mvc.config.messaging.NotificationMessagingConfig.NOTIFICATION_QUEUE;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.EMAIL_UPDATE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=dev"})
public class NotificationMessageConsumersTest {

    @Autowired
    private RabbitListenerTestHarness harness;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private EmailNotificationService service;

    @Before
    public void setup() {
        reset(service);
    }

    @Test
    public void test() throws InterruptedException {
        NotificationMessageConsumers consumers = harness.getSpy(NOTIFICATION_QUEUE);
        assertNotNull(consumers);

        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(1);
        doAnswer(answer).when(consumers).receiveMessage(any(NotificationMessage.class));

        template.convertAndSend(NOTIFICATION_QUEUE, new NotificationMessage(EMAIL_UPDATE, 1));

        assertTrue(answer.getLatch().await(10, TimeUnit.SECONDS));
        verify(consumers).receiveMessage(any(NotificationMessage.class));
        verify(service).handleEmailUpdateEvent(1);
    }
}
