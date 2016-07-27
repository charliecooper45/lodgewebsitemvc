package uk.cooperca.lodge.website.mvc.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A review of the lodge by a user.
 *
 * @author Charlie Cooper
 */
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    private static final long serialVersionUID = -7336266513743395625L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    @SequenceGenerator(name = "reviews_seq", sequenceName = "reviews_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "review", nullable = false)
    private String review;

    @Column(name = "score", nullable = false)
    private int score;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_at", nullable = false)
    private DateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Review() {
        // for Hibernate
    }

    public Review(String review, int score, DateTime createdAt, User user) {
        this.review = review;
        this.score = score;
        this.createdAt = createdAt;
        this.user = user;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
