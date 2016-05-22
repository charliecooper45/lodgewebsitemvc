package uk.cooperca.lodge.website.mvc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Our custom {@link AuthenticationSuccessHandler} implementation.
 *
 * @author Charlie Cooper
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private LocaleResolver localeResolver;

    public CustomAuthenticationSuccessHandler(LocaleResolver localeResolver) {
        if (localeResolver == null) {
            throw new IllegalArgumentException("localeResolver cannot be null");
        }
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        // set the user's language using their preference
        Language language = user.getLanguage();
        if (language != null) {
            Locale locale = new Locale(language.name());
            localeResolver.setLocale(request, response, locale);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("account");
    }
}
