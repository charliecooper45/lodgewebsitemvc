package uk.cooperca.lodge.website.mvc.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.repository.ReviewRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=dev", "jasypt.encryptor.password=password"})
public class DataConfigTest {

    @Autowired
    private ReviewRepository repository;

    @Test
    public void test() {
        assertNotNull(repository);

        Iterable<Review> reviews = repository.findAll();
        assertNotNull(reviews);
        long count = repository.count();
        assertEquals(1, count);
    }
}
