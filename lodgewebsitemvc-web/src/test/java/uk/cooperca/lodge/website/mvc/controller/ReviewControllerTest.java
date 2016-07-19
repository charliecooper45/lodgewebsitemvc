package uk.cooperca.lodge.website.mvc.controller;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ReviewControllerTest extends AbstractControllerTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void testShowReviews() throws Exception {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Review 1", 3, DateTime.now()));
        reviews.add(new Review("Review 1", 5, DateTime.now()));
        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdAt"));
        PageImpl<Review> page = new PageImpl<>(reviews, pageRequest, 2);
        when(reviewService.getReviews(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/reviews").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("reviews"))
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attribute("reviews", page));
    }
}
