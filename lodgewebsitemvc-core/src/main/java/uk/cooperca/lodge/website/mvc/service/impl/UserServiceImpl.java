package uk.cooperca.lodge.website.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.repository.UserRepository;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface.
 *
 * @author Charlie Cooper
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}
