package uk.cooperca.lodge.website.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.cooperca.lodge.website.mvc.command.ReviewCommand;
import uk.cooperca.lodge.website.mvc.entity.Review;
import uk.cooperca.lodge.website.mvc.service.ReviewService;

import java.util.List;
import java.util.Locale;

/**
 * Controller for all review related operations.
 *
 * @author Charlie Cooper
 */
@Controller
@RequestMapping("/reviews")
public class ReviewController extends AbstractController {

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<String>> addReview(@Validated @RequestBody ReviewCommand command, BindingResult result, Locale locale) {
        if (result.hasErrors()) {
            return errorResponse(result, locale);
        }
        reviewService.addReview(command, getCurrentUser());
        return successResponse("reviews.added", new String[]{}, locale);
    }
}
