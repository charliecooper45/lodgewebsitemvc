package uk.cooperca.lodge.website.mvc.db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Migration to create the reviews table.
 *
 * @author Charlie Cooper
 */
public class V1__Create_reviews_table implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute(
            "CREATE TABLE reviews (" +
                "id SERIAL PRIMARY KEY, " +
                "review VARCHAR(500) NOT NULL," +
                "score INTEGER NOT NULL," +
                "created_at TIMESTAMP NOT NULL" +
            ");"
        );
    }
}
