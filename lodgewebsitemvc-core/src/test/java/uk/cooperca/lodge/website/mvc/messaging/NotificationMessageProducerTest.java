package uk.cooperca.lodge.website.mvc.messaging;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificationMessageProducerTest {

    private NotificationMessageProducer producer;
    private RabbitTemplate template;

    @Before
    public void setup() {
        producer = new NotificationMessageProducer();
        template = mock(RabbitTemplate.class);
        ReflectionTestUtils.setField(producer, "template", template);
        doThrow(new AmqpException("error")).when(template).convertAndSend(any(NotificationMessage.class));
    }

    @Test(expected = AmqpException.class)
    public void testSendMessage() {
        producer.sendMessage(NotificationMessage.Type.PASSWORD_RESET, 1);
    }

    @Test
    public void testSendMessageHandleError() {
        producer.sendMessageHandleError(NotificationMessage.Type.PASSWORD_RESET, 1);
        verify(template).convertAndSend(any(NotificationMessage.class));
    }
}
