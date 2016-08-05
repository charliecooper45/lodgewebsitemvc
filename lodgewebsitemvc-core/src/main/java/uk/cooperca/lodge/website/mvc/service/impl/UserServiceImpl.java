package uk.cooperca.lodge.website.mvc.service.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.Role;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;
import uk.cooperca.lodge.website.mvc.messaging.NotificationMessageProducer;
import uk.cooperca.lodge.website.mvc.repository.RoleRepository;
import uk.cooperca.lodge.website.mvc.repository.UserRepository;
import uk.cooperca.lodge.website.mvc.token.TokenManager;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_USER;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.EMAIL_UPDATE;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.NEW_USER;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.PASSWORD_UPDATE;

/**
 * Implementation of the {@link UserService} interface.
 *
 * @author Charlie Cooper
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NotificationMessageProducer producer;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(UserCommand command, Locale locale) {
        // we default users to the user role
        Role role = roleRepository.findByRoleName(ROLE_USER);
        Language language = Language.valueOf(locale.getLanguage().toUpperCase());
        User user = new User(command.getEmail(), encoder.encode(command.getPassword()), command.getFirstName(),
                command.getLastName(), role, language, false, DateTime.now());
        user = userRepository.save(user);
        if (user != null) {
            producer.sendMessage(NEW_USER, user.getId());
        }
        return user;
    }

    @Override
    public void requestVerificationEmail(int id) {
        producer.sendMessage(EMAIL_UPDATE, id);
    }

    @Override
    public User verifyUser(String token) {
        Map<String, String> body = (Map) tokenManager.decodeToken(token).getBody();
        int id = Integer.valueOf(body.get("sub"));
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            if (!user.isVerified() && userRepository.updateVerified(true, id) > 0) {
                return userRepository.findById(id).get();
            }
        }
        return null;
    }

    @Override
    public void updateEmail(String email, int id) {
        handleUpdate(userRepository.updateEmail(email, id));
        userRepository.updateVerified(false, id);
        producer.sendMessage(EMAIL_UPDATE, id);
    }

    @Override
    public void updatePassword(String password, int id) {
        handleUpdate(userRepository.updatePassword(encoder.encode(password), id));
    }

    @Override
    public void updateFirstName(String firstName, int id) {
        handleUpdate(userRepository.updateFirstName(firstName, id));
    }

    @Override
    public void updateLastName(String lastName, int id) {
        handleUpdate(userRepository.updateLastName(lastName, id));
    }

    @Override
    public void updateLanguage(String language, int id) {
        Language userLanguage = Language.valueOf(language.toUpperCase().trim());
        handleUpdate(userRepository.updateLanguage(userLanguage, id));
    }

    /**
     * Handles the case where the repository methods return 0.
     *
     * @param value the value returned by the repository method
     *
     * @throws IllegalArgumentException if the value is zero
     */
    protected void handleUpdate(int value) {
        if (value == 0) {
            throw new IllegalArgumentException("repository returned 0");
        }
    }
}
