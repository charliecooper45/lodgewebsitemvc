package uk.cooperca.lodge.website.mvc.service.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.cooperca.lodge.website.mvc.command.ReviewCommand;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.repository.ReviewRepository;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

/**
 * Implementation of the {@link ReviewService} interface.
 *
 * @author Charlie Cooper
 */
@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Override
    @Cacheable
    public Page<Review> getReviews(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Review addReview(ReviewCommand command, User user) {
        return repository.save(new Review(command.getReview(), command.getScore(), DateTime.now(), user));
    }
}
