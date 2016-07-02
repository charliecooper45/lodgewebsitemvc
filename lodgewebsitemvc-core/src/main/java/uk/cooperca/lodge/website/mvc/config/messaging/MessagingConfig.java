package uk.cooperca.lodge.website.mvc.config.messaging;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import static uk.cooperca.lodge.website.mvc.config.messaging.NotificationMessagingConfig.NOTIFICATION_QUEUE;

/**
 * Configuration class that configures messaging using the AMQP protocol. This class is imported by other modules to set
 * up messaging.
 *
 * @author Charlie Cooper
 */
@Configuration
@ComponentScan(basePackages = "uk.cooperca.lodge.website.mvc.messaging")
@EnableRabbit
@EnableEncryptableProperties
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Import(NotificationMessagingConfig.class)
public class MessagingConfig {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(env.getProperty("messaging.hostname"));
        connectionFactory.setUsername(env.getProperty("messaging.username"));
        connectionFactory.setPassword(env.getProperty("messaging.password"));
        connectionFactory.setVirtualHost(env.getProperty("messaging.virtualHost"));
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(env.getProperty("messaging.concurrentConsumers", Integer.class));
        factory.setMaxConcurrentConsumers(env.getProperty("messaging.maxConcurrentConsumers", Integer.class));
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate emailUpdateTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(getRoutingKey(NOTIFICATION_QUEUE));
        template.setMessageConverter(messageConverter());
        return template;
    }

    public static String getRoutingKey(String queue) {
        return queue.replaceAll("-", ".");
    }
}
