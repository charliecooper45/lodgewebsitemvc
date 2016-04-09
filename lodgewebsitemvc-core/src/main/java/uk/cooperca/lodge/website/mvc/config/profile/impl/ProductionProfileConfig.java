package uk.cooperca.lodge.website.mvc.config.profile.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import uk.cooperca.lodge.website.mvc.config.profile.ProfileConfig;

import javax.sql.DataSource;

/**
 * {@link ProfileConfig} implementation for production.
 *
 * @author Charlie Cooper
 */
@Profile("prod")
@Configuration
public class ProductionProfileConfig implements ProfileConfig {

    @Autowired
    private Environment env;

    @Bean
    @Override
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        config.setJdbcUrl(env.getProperty("jdbc.url"));
        config.setUsername(env.getProperty("jdbc.username"));
        config.setPassword(env.getProperty("jdbc.password"));
        config.setMaximumPoolSize(env.getProperty("jdbc.maximumPoolSize", Integer.class));
        config.setPoolName(env.getProperty("jdbc.poolName"));
        return new HikariDataSource(config);
    }

    @Bean(initMethod = "migrate")
    @Override
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setLocations(env.getProperty("flyway.locations"));
        flyway.setDataSource(dataSource());
        return flyway;
    }
}
