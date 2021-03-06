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
public class V1_2__Populate_users_table implements SpringJdbcMigration {

    private static final String INSERT_STATEMENT = "INSERT INTO users (email, password, first_name, last_name, role_id, " +
            "language_preference, verified, created_at, verification_request_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String ROLE_STATEMENT = "SELECT id FROM roles WHERE role_name = ?";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<User> users = Arrays.asList(
                new User("testcc45@gmail.com", new BCryptPasswordEncoder().encode("1Password"), "Bob", "Smith", null,
                        Language.EN, true, DateTime.now().minusDays(3)),
                new User("testcc46@gmail.com", new BCryptPasswordEncoder().encode("2Password"), "Anton", "Igniski", null,
                        Language.RU, false, DateTime.now().minusDays(6))
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
                statement.setBoolean(7, user.isVerified());
                statement.setTimestamp(8, new Timestamp(user.getCreatedAt().getMillis()));
                statement.setTimestamp(9, new Timestamp(user.getVerificationRequestAt().getMillis()));
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}
