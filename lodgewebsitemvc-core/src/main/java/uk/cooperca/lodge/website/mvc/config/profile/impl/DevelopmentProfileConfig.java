package uk.cooperca.lodge.website.mvc.config.profile.impl;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import uk.cooperca.lodge.website.mvc.config.profile.ProfileConfig;

import javax.sql.DataSource;

/**
 * {@link ProfileConfig} implementation for development.
 *
 * @author Charlie Cooper
 */
@Profile("dev")
@Configuration
public class DevelopmentProfileConfig implements ProfileConfig {

    @Autowired
    private Environment env;

    @Override
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        driver.setUrl(env.getProperty("jdbc.url"));
        driver.setUsername(env.getProperty("jdbc.username"));
        driver.setPassword(env.getProperty("jdbc.password"));
        return driver;
    }

    @Override
    @Bean(initMethod = "migrate", destroyMethod = "clean")
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setLocations(env.getProperty("flyway.locations"));
        flyway.setDataSource(dataSource());
        return flyway;
    }
}
