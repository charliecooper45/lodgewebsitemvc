package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

/**
 * Controller for all review related operations.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final int PAGE_SIZE = 2;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping
    public String showReviews(Model model,
                              @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, value = PAGE_SIZE) Pageable pageable) {
        Page<Review> reviews = reviewService.getReviews(pageable);
        model.addAttribute("reviews", reviews);
        return "reviews";
    }
}
