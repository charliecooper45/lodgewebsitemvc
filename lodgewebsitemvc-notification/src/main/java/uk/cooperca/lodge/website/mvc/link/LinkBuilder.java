package uk.cooperca.lodge.website.mvc.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment environment;

    public String getVerificationLink(int userId) {
        return environment.getProperty("domain") + "/user/verify?token=" + tokenManager.generateVerificationToken(userId);
    }

    public String getAccountLink() {
        return environment.getProperty("domain") + "/account";
    }
}
