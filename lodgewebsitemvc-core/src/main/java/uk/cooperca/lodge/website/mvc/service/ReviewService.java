package uk.cooperca.lodge.website.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.cooperca.lodge.website.mvc.entity.Review;

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
    // TODO: sort
    public Page<Review> getReviews(Pageable pageable);
}
