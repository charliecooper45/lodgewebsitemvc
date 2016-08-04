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

    // TODO: handle 0 for update methods
    @Override
    public int updateEmail(String email, int id) {
        int value = userRepository.updateEmail(email, id);
        if (value > 0) {
            userRepository.updateVerified(false, id);
            producer.sendMessage(EMAIL_UPDATE, id);
        }
        return value;
    }

    @Override
    public int updatePassword(String password, int id) {
        int value = userRepository.updatePassword(encoder.encode(password), id);
        if (value > 0) {
            producer.sendMessage(PASSWORD_UPDATE, id);
        }
        return value;
    }

    @Override
    public int updateFirstName(String firstName, int id) {
        return userRepository.updateFirstName(firstName, id);
    }

    @Override
    public int updateLastName(String lastName, int id) {
        return userRepository.updateLastName(lastName, id);
    }

    @Override
    public int updateLanguage(String language, int id) {
        Language userLanguage = Language.valueOf(language.toUpperCase().trim());
        return userRepository.updateLanguage(userLanguage, id);
    }
}
