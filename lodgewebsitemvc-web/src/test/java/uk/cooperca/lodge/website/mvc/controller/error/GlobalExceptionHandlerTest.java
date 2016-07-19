package uk.cooperca.lodge.website.mvc.controller.error;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.cooperca.lodge.website.mvc.controller.AbstractControllerTest;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

import java.io.UncheckedIOException;
import java.net.ConnectException;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class GlobalExceptionHandlerTest extends AbstractControllerTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void testHandleError() throws Exception {
        Pageable pageable = new PageRequest(10, 2, Sort.Direction.DESC, "createdAt");
        when(reviewService.getReviews(pageable)).thenThrow(
                new UncheckedIOException(new ConnectException())
        );

        mockMvc.perform(get("/reviews?page=10").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("error"));
    }
}
