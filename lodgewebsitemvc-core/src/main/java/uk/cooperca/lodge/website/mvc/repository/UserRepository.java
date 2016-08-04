package uk.cooperca.lodge.website.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}s.
 *
 * @author Charlie Cooper
 */
@SuppressWarnings("JpaQlInspection")
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves the user with the given identifier.
     *
     * @param id the identifier of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> findById(int id);

    /**
     * Retrieves the user with the given email address.
     *
     * @param email the email address of the user
     *
     * @return an optional containing the user if present
     */
    public Optional<User> findByEmail(String email);

    /**
     * Updates the given user's verification status.
     *
     * @param verified whether to update the user as verified or not
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.verified = ?1 where u.id = ?2")
    public int updateVerified(boolean verified, int id);

    /**
     * Updates the given user's email address.
     *
     * @param email the new email address for the user
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.email = ?1 where u.id = ?2")
    public int updateEmail(String email, int id);

    /**
     * Updates the given user's password.
     *
     * @param password the new password for the user
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.password = ?1 where u.id = ?2")
    public int updatePassword(String password, int id);

    /**
     * Updates the given user's first name.
     *
     * @param firstName the new first name for the user
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.firstName = ?1 where u.id = ?2")
    public int updateFirstName(String firstName, int id);

    /**
     * Updates the given user's last name.
     *
     * @param lastName the new last name for the user
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.lastName = ?1 where u.id = ?2")
    public int updateLastName(String lastName, int id);


    /**
     * Updates the given user's language preference.
     *
     * @param language the new language for the user
     * @param id the id of the user to update
     *
     * @return an int holding the number of rows updated
     */
    @Modifying
    @Transactional
    @Query("update User u set u.language = ?1 where u.id = ?2")
    public int updateLanguage(Language language, int id);
}
