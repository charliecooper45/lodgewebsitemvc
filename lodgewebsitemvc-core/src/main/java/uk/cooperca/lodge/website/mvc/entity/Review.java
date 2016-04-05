package uk.cooperca.lodge.website.mvc.entity;

import javax.persistence.*;

/**
 * Represents a review of the lodge by a user.
 *
 * @author Charlie Cooper
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq", sequenceName = "reviews_id_seq")
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "review", nullable = false)
    private String review;

    public Review() {
        // for Hibernate
    }

    public Review(String review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
