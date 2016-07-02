package uk.cooperca.lodge.website.mvc.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.token.TokenManager;

/**
 * Class that builds links for use in notifications.
 *
 * @author Charlie Cooper
 */
@Component
public class LinkBuilder {

    @Autowired
    private TokenManager tokenManager;

    public String getVerificationLink(int userId) {
        return "http://localhost:8080/user/verify?token=" + tokenManager.generateVerificationToken(userId);
    }
}
