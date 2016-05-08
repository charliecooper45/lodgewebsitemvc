package uk.cooperca.lodge.website.mvc.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Optional;

/**
 * Looks up a {@link User} using their username (in our case the email address) so they can be authenticated.
 *
 * @author Charlie Cooper
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = service.getUserByEmail(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("email address not registered");
        }
        return optional.get();
    }
}
