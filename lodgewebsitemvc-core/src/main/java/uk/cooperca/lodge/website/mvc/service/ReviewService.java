package uk.cooperca.lodge.website.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.cooperca.lodge.website.mvc.command.ReviewCommand;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.List;

/**
 * Service that allows the management of {@link Review}s.
 *
 * @author Charlie Cooper
 */
public interface ReviewService {

    /**
     * Returns a page of reviews.
     *
     * @param pageable specifies the page to return
     *
     * @return the reviews
     */
    public Page<Review> getReviews(Pageable pageable);

    /**
     * Returns the reviews for the given user.
     *
     * @param id the user's id
     *
     * @return the reviews for the user
     */
    public List<Review> getReviewsForUser(int id);

    /**
     * Adds a new review to the platform.
     *
     * @param command the command object holding the new review
     * @param user the user who is adding the review
     *
     * @return the review
     */
    public Review addReview(ReviewCommand command, User user);

    /**
     * Deletes the review with the given id.
     *
     * @param id the id of the review to delete
     * @param userId the id of the user deleting the review
     */
    public void deleteReview(int id, int userId);
}
