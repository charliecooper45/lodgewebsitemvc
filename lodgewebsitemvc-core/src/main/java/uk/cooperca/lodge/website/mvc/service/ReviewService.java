package uk.cooperca.lodge.website.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.cooperca.lodge.website.mvc.command.ReviewCommand;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.entity.User;

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
     * Adds a new review to the platform.
     *
     * @param command the command object holding the new review
     * @param user the user who is adding the review
     *
     * @return the review
     */
    public Review addReview(ReviewCommand command, User user);
}
