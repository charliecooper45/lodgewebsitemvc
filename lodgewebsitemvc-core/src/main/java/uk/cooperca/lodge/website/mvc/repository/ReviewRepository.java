package uk.cooperca.lodge.website.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.cooperca.lodge.website.mvc.entity.Review;

import java.util.List;

/**
 * Spring Data repository for {@link Review}s.
 *
 * @author Charlie Cooper
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Returns the reviews for the given user.
     *
     * @param id the user's id
     *
     * @return the reviews for the user
     */
    public List<Review> findByUserId(int id);
}
