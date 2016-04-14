package uk.cooperca.lodge.website.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.cooperca.lodge.website.mvc.entity.Review;

/**
 * Spring Data repository for {@link Review}s.
 *
 * @author Charlie Cooper
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
