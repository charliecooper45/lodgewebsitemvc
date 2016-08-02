package uk.cooperca.lodge.website.mvc.controller;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.cooperca.lodge.website.mvc.command.ReviewCommand;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.test.WithCustomUser;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReviewControllerTest extends AbstractControllerTest {

    @Test
    public void testShowReviews() throws Exception {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Review 1", 3, DateTime.now(), mock(User.class)));
        reviews.add(new Review("Review 1", 5, DateTime.now(), mock(User.class)));
        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdAt"));
        PageImpl<Review> page = new PageImpl<>(reviews, pageRequest, 2);
        when(reviewService.getReviews(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/reviews").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("reviews"))
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attribute("reviews", page));
    }

    @Test
    @WithCustomUser
    public void testAddReview() throws Exception {
        ReviewCommand command = new ReviewCommand();
        command.setReview("Great time had by all!");
        command.setScore(6);

        // score too large
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Score cannot be greater than 5")));
        verifyZeroInteractions(reviewService);

        // correct update
        command.setScore(5);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Review Added")));
        verify(reviewService).addReview(any(ReviewCommand.class), any(User.class));

        // database down
        when(reviewService.addReview(any(ReviewCommand.class), any(User.class))).thenThrow(mock(UncheckedIOException.class));
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Unable to add review")));
        verify(reviewService, times(2)).addReview(any(ReviewCommand.class), any(User.class));
    }

    @Test
    public void testValidation() throws Exception {
        ReviewCommand command = new ReviewCommand();

        // no review
        command.setReview(null);
        command.setScore(3);
        String data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Review is required")));

        // blank review
        command.setReview("");
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Review is required")));

        // review too long
        command.setReview(StringUtils.repeat("*", 151));
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Review must be between 0 and 150 characters long")));

        // score too low
        command.setReview(StringUtils.repeat("*", 15));
        command.setScore(0);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Score cannot be less than 1")));

        // score too high
        command.setScore(6);
        data = getObjectWriter().writeValueAsString(command);
        mockMvc.perform(post("/reviews").with(csrf()).contentType(APPLICATION_JSON).content(data))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Score cannot be greater than 5")));
    }

    @Test
    @WithCustomUser
    public void testDeleteReview() throws Exception {
        // correct update
        mockMvc.perform(delete("/reviews/1").with(csrf()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Review Deleted")));
        verify(reviewService, times(1)).deleteReview(1, 0);

        // wrong user
        doThrow(mock(SecurityException.class)).when(reviewService).deleteReview(1, 0);
        mockMvc.perform(delete("/reviews/1").with(csrf()).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("You are not the owner of this review")));
        verify(reviewService, times(2)).deleteReview(1, 0);

        // database down
        doThrow(mock(UncheckedIOException.class)).when(reviewService).deleteReview(1, 0);
        mockMvc.perform(delete("/reviews/1").with(csrf()).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*]", contains("Unable to delete review")));
        verify(reviewService, times(3)).deleteReview(1, 0);
    }
}
