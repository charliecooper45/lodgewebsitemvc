package uk.cooperca.lodge.website.mvc.db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Migration to create the users table.
 *
 * @author Charlie Cooper
 */
public class V2__Create_users_and_roles_tables implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute(
            "CREATE TABLE roles (" +
                "id SERIAL PRIMARY KEY," +
                "role_name VARCHAR(500) NOT NULL," +
                "created_at TIMESTAMP NOT NULL" +
            ");"
        );

        jdbcTemplate.execute(
            "CREATE TABLE users (" +
                "id SERIAL PRIMARY KEY, " +
                "email VARCHAR(500) NOT NULL," +
                "password VARCHAR(500) NOT NULL," +
                "first_name VARCHAR(500) NOT NULL," +
                "last_name VARCHAR(500) NOT NULL," +
                "role_id INTEGER REFERENCES roles (id) NOT NULL," +
                "language_preference VARCHAR(500) NOT NULL," +
                "verified BOOLEAN NOT NULL," +
                "created_at TIMESTAMP NOT NULL" +
            ");"
        );
    }
}
