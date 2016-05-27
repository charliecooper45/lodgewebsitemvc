package uk.cooperca.lodge.website.mvc.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import uk.cooperca.lodge.website.mvc.AbstractCoreTest;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.repository.ReviewRepository;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CoreConfigTest extends AbstractCoreTest {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private ReviewService service;

    @Test
    public void test() {
        assertNotNull(repository);
        Iterable<Review> reviews = repository.findAll();
        assertNotNull(reviews);
        long count = repository.count();
        assertEquals(5, count);

        assertNotNull(service);
        reviews = service.getReviews(new PageRequest(0, 1));
        assertNotNull(reviews);
    }
}
