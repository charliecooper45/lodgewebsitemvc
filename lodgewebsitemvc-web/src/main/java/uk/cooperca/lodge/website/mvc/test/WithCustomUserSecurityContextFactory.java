package uk.cooperca.lodge.website.mvc.test;

import org.joda.time.DateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import uk.cooperca.lodge.website.mvc.entity.Role;
import uk.cooperca.lodge.website.mvc.entity.User;

/**
 * {@link WithSecurityContextFactory} implementation that logs in a user for test methods annotated with the
 * {@link WithCustomUser} annotation.
 *
 * @author Charlie Cooper
 */
public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Role userRole = new Role(Role.RoleName.ROLE_USER, DateTime.now());
        User user = new User("bill@gmail.com", "23asds232dwaDsa", "Bill", "Smith", userRole, User.Language.EN, true, DateTime.now());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
