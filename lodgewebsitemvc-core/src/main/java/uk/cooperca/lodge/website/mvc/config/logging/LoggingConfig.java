package uk.cooperca.lodge.website.mvc.config.logging;

import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Configures logging.
 *
 * @author Charlie Cooper
 */
@Configuration
public class LoggingConfig {

    @Autowired
    private Environment env;

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder ();
    }

    @Bean
    public LoggerContext loggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Bean(name = "consoleEncoder", initMethod = "start", destroyMethod = "stop")
    public PatternLayoutEncoder consoleEncoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern(env.getProperty("logging.console.pattern"));
        return encoder;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ConsoleAppender consoleAppender(LoggerContext ctx, @Qualifier("consoleEncoder") Encoder encoder) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setContext(ctx);
        appender.setEncoder(encoder);
        return appender;
    }

    @Configuration
    @Profile("prod")
    public static class ProductionLoggingConfig {

        @Autowired
        private Environment env;

        @Bean(name = "fileEncoder", initMethod = "start", destroyMethod = "stop")
        public PatternLayoutEncoder fileEncoder(LoggerContext ctx) {
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(ctx);
            encoder.setPattern(env.getProperty("logging.file.pattern"));
            return encoder;
        }

        @Bean(initMethod = "start", destroyMethod = "stop")
        public RollingFileAppender fileAppender(LoggerContext context, @Qualifier("fileEncoder") Encoder encoder) {
            RollingFileAppender appender = new RollingFileAppender();
            appender.setContext(context);
            appender.setEncoder(encoder);
            appender.setAppend(env.getProperty("logging.file.append", Boolean.class));
            appender.setFile(env.getProperty("logging.file.directory") + "application.log");
            return appender;
        }

        @Bean(initMethod = "start", destroyMethod = "stop")
        public TimeBasedRollingPolicy timeBasedRollingPolicy(LoggerContext context, RollingFileAppender appender) {
            TimeBasedRollingPolicy timeBasedRollingPolicy = new TimeBasedRollingPolicy();
            timeBasedRollingPolicy.setContext(context);
            timeBasedRollingPolicy.setParent(appender);
            timeBasedRollingPolicy.setFileNamePattern(env.getProperty("logging.file.directory") + "application_%d{yyyy-MM-dd}.%i.log");
            timeBasedRollingPolicy.setMaxHistory(env.getProperty("logging.file.maxHistory", Integer.class));
            appender.setRollingPolicy(timeBasedRollingPolicy);
            return timeBasedRollingPolicy;
        }
    }
}
