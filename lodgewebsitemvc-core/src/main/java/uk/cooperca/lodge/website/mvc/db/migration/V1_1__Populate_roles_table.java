package uk.cooperca.lodge.website.mvc.db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.cooperca.lodge.website.mvc.entity.Role;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName;

/**
 * Seeds the roles table with our supported roles.
 *
 * @author Charlie Cooper
 */
public class V1_1__Populate_roles_table implements SpringJdbcMigration {

    private static final String INSERT_STATEMENT = "INSERT INTO roles (role_name, created_at) VALUES (?, ?)";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Role> roles = new ArrayList<>();
        for (RoleName roleName : RoleName.values()) {
            roles.add(new Role(roleName, DateTime.now()));
        }

        jdbcTemplate.batchUpdate(INSERT_STATEMENT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement statement, int index) throws SQLException {
                Role role = roles.get(index);
                statement.setString(1, role.getRoleName().name());
                statement.setTimestamp(2, new Timestamp(role.getCreatedAt().getMillis()));
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }
}
