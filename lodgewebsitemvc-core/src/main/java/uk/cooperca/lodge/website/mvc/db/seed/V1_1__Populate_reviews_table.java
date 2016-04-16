package uk.cooperca.lodge.website.mvc.db.seed;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.cooperca.lodge.website.mvc.entity.Review;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Seeds the reviews table with data for local development.
 *
 * @author Charlie Cooper
 */
public class V1_1__Populate_reviews_table implements SpringJdbcMigration {

    private static final String INSERT_STATEMENT = "INSERT INTO reviews (review, score, created_at) " +
            "VALUES (?, ?, ?)";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<Review> reviews = Arrays.asList(
            new Review("A really nice lodge, we loved our time there.", 4, DateTime.now()),
            new Review("Good location. Shower didn't work", 2, DateTime.now().minusDays(1)),
            new Review("Spent a weekend here with friends, we all enjoyed the fresh coastal air. " +
                    "Couple of great local pubs.", 5, DateTime.now().minusDays(1)),
            new Review("Rained non stop, no one told us it would rain in Cornwall in December.", 1,
                    DateTime.now().minusWeeks(2)),
            new Review("A great area of the country. Our seventh visit and every one has been perfect. We take our two " +
                    "dogs who absolutely love it! Will be back again as soon as we can afford it!", 5,
                    DateTime.now().minusWeeks(8))
        );

        jdbcTemplate.batchUpdate(INSERT_STATEMENT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement statement, int index) throws SQLException {
                Review review = reviews.get(index);
                statement.setString(1, review.getReview());
                statement.setInt(2, review.getScore());
                statement.setTimestamp(3, new Timestamp(review.getCreatedAt().getMillis()));
            }

            @Override
            public int getBatchSize() {
                return reviews.size();
            }
        });
    }
}
