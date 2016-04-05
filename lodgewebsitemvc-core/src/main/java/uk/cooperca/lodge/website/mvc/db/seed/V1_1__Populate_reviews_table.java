package uk.cooperca.lodge.website.mvc.db.seed;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.cooperca.lodge.website.mvc.entity.Review;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Seeds the reviews table with data for local development.
 *
 * @author Charlie Cooper
 */
public class V1_1__Populate_reviews_table implements SpringJdbcMigration {

    private static final String INSERT_STATEMENT = "INSERT INTO reviews (review) VALUES (?)";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Review> reviews = Arrays.asList(new Review("A really nice lodge, we loved our time there."));

        jdbcTemplate.batchUpdate(INSERT_STATEMENT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement statement, int index) throws SQLException {
                Review review = reviews.get(index);
                statement.setString(1, review.getReview());
            }

            @Override
            public int getBatchSize() {
                return reviews.size();
            }
        });
    }
}
