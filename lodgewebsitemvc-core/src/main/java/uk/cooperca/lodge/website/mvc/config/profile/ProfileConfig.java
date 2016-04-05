package uk.cooperca.lodge.website.mvc.config.profile;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * Marks profile specific beans, implementations create these beans for use in the main configuration class.
 *
 * @author Charlie Cooper
 */
public interface ProfileConfig {
    public DataSource dataSource();

    public Flyway flyway();
}
