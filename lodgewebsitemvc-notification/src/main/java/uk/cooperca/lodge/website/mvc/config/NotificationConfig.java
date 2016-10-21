package uk.cooperca.lodge.website.mvc.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import uk.cooperca.lodge.website.mvc.password.PasswordGenerator;
import uk.cooperca.lodge.website.mvc.password.impl.RandomPasswordGenerator;

import java.io.IOException;
import java.util.Properties;

/**
 * Main configuration class for the notification module.
 *
 * @author Charlie Cooper
 */
@ComponentScan(basePackages = {"uk.cooperca.lodge.website.mvc.consumer", "uk.cooperca.lodge.website.mvc.service",
        "uk.cooperca.lodge.website.mvc.link", "uk.cooperca.lodge.website.mvc.scheduling"})
@Import(value = {CoreConfig.class})
public class NotificationConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(env.getProperty("mail.port", Integer.class));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.socketFactory.class", env.getProperty("mail.smtp.socketFactory.class"));
        properties.setProperty("mail.smtp.socketFactory.fallback", env.getProperty("mail.smtp.socketFactory.fallback"));
        properties.setProperty("mail.smtp.socketFactory.port", env.getProperty("mail.port"));
        properties.put("mail.smtp.startssl.enable", env.getProperty("mail.smtp.startssl.enable"));
        mailSender.setJavaMailProperties(properties);
        return mailSender;
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

    @Bean
    public PasswordGenerator passwordGenerator() {
        return new RandomPasswordGenerator();
    }
}
