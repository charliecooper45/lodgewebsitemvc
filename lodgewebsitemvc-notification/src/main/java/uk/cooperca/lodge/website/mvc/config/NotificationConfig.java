package uk.cooperca.lodge.website.mvc.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import uk.cooperca.lodge.website.mvc.config.messaging.MessagingConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * Configures messaging using RabbitMQ for the notification module.
 *
 * @author Charlie Cooper
 */
@Import(value = {MessagingConfig.class, DataConfig.class})
@ComponentScan(basePackages = {"uk.cooperca.lodge.website.mvc.consumer", "uk.cooperca.lodge.website.mvc.email"})
public class NotificationConfig {

    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public VelocityEngine velocityEngine() throws VelocityException, IOException {
        VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);
        return factory.createVelocityEngine();
    }
}
