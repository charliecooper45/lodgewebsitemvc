package uk.cooperca.lodge.website.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.cooperca.lodge.website.mvc.entity.User;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}s.
 *
 * @author Charlie Cooper
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves the user with the given email address.
     *
     * @param email the email address of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> findByEmail(String email);
}
