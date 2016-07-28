package uk.cooperca.lodge.website.mvc.command;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Command object that represents an attempt to update/create a review.
 *
 * @author Charlie Cooper
 */
public class ReviewCommand {

    @NotBlank
    @Length(max = 150)
    private String review;

    @Min(1)
    @Max(5)
    private int score;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
