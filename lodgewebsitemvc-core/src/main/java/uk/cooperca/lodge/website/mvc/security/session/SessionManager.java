package uk.cooperca.lodge.website.mvc.security.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import uk.cooperca.lodge.website.mvc.service.UserService;

/**
 * Manager that allows us to retrieve information about current user sessions on the website.
 *
 * @author Charlie Cooper
 */
public class SessionManager {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService userService;

    /**
     * Expires the session (we only allow one per user) for the given user.
     *
     * @param id the id of the user
     */
    public void expireUserSession(int id) {
        sessionRegistry.getAllSessions(userService.getUserById(id).get(), false).stream()
                .forEach(SessionInformation::expireNow);
    }
}
