package uk.cooperca.lodge.website.mvc.service;

import org.junit.Test;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.entity.User;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ReviewServiceImplTest extends AbstractServiceTest {

    @Test
    public void testDeleteReview() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1);
        Review review = mock(Review.class);
        when(review.getUser()).thenReturn(user);
        when(reviewRepository.findOne(1)).thenReturn(review);

        // incorrect user
        try {
            reviewService.deleteReview(1, 2);
            fail();
        } catch(SecurityException e) {}
        verify(reviewRepository, never()).delete(anyInt());

        // review deleted
        reviewService.deleteReview(1, 1);
        verify(reviewRepository).delete(1);
    }
}
