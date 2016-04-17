package uk.cooperca.lodge.website.mvc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.cooperca.lodge.website.mvc.config.DataConfig;
import uk.cooperca.lodge.website.mvc.entity.Review;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=dev", "jasypt.encryptor.password=password"})
public class CachingTest {

    @Autowired
    private ReviewService service;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testGetReviews() {
        String cacheName = "reviews";
        Cache cache = cacheManager.getCache(cacheName);

        assertNotNull(service);
        assertNotNull(cacheManager);
        assertNotNull(cache);

        PageRequest request1 = new PageRequest(0, 2);
        Page<Review> reviews1 = service.getReviews(request1);
        Page<Review> entry1 = (Page<Review>) cache.get(request1).get();
        assertEquals(reviews1, entry1);

        PageRequest request2 = new PageRequest(1, 2);
        Page<Review> reviews2 = service.getReviews(request2);
        Page<Review> entry2 = (Page<Review>) cache.get(request2).get();
        assertEquals(reviews2, entry2);
        assertNotEquals(reviews1, entry2);

        Page<Review> reviews3 = service.getReviews(request1);
        assertEquals(reviews3, entry1);

        Page<Review> reviews4 = service.getReviews(request2);
        assertEquals(reviews4, entry2);

        PageRequest request3 = new PageRequest(0, 2, new Sort(Sort.Direction.ASC, "createdAt"));
        Page<Review> reviews5 = service.getReviews(request3);
        Page<Review> entry3 = (Page<Review>) cache.get(request3).get();
        assertEquals(reviews5, entry3);
        assertNotEquals(reviews1, entry3);

        Page<Review> reviews6 = service.getReviews(request3);
        assertEquals(reviews6, entry3);
    }


}
