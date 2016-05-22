package uk.cooperca.lodge.website.mvc.db.seed;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName;

/**
 * Seeds the users table with data for local development.
 *
 * @author Charlie Cooper
 */
public class V2_2__Populate_users_table implements SpringJdbcMigration {

    private static final String INSERT_STATEMENT = "INSERT INTO users (email, password, first_name, last_name, role_id, language_preference, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String ROLE_STATEMENT = "SELECT id FROM roles WHERE role_name = ?";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<User> users = Arrays.asList(
                new User("bob@gmail.com", new BCryptPasswordEncoder().encode("1Password"), "Bob", "Smith", null,
                        Language.EN, DateTime.now())
        );

        Integer adminId = jdbcTemplate.queryForObject(ROLE_STATEMENT, new Object[]{ RoleName.ROLE_ADMIN.name() }, Integer.class);
        Integer userId = jdbcTemplate.queryForObject(ROLE_STATEMENT, new Object[]{ RoleName.ROLE_USER.name() }, Integer.class);

        jdbcTemplate.batchUpdate(INSERT_STATEMENT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement statement, int index) throws SQLException {
                User user = users.get(index);
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getFirstName());
                statement.setString(4, user.getLastName());
                statement.setInt(5, userId);
                statement.setString(6, user.getLanguage().name());
                statement.setTimestamp(7, new Timestamp(user.getCreatedAt().getMillis()));
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}
