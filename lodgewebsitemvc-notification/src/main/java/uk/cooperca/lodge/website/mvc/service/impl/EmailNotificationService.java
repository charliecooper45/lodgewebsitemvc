package uk.cooperca.lodge.website.mvc.service.impl;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;
import uk.cooperca.lodge.website.mvc.link.LinkBuilder;
import uk.cooperca.lodge.website.mvc.service.NotificationService;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.*;

/**
 * {@link NotificationService} implementation to send emails to users.
 *
 * @author Charlie Cooper
 */
@Component
public class EmailNotificationService implements NotificationService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkBuilder linkBuilder;

    @Autowired
    private MessageSource messageSource;

    private final String templateFolder = "/template/";
    private final String velocitySuffix = ".vm";

    // TODO: Russian emails
    @Override
    public void handleNewUserEvent(int userId) {
        sendEmail(userId, NEW_USER);
    }

    @Override
    public void handleEmailUpdateEvent(int userId) {
        sendEmail(userId, EMAIL_UPDATE);
    }

    @Override
    public void handlePasswordUpdateEvent(int userId) {
        sendEmail(userId, PASSWORD_UPDATE);
    }

    @Override
    public void handleVerificationRequestEvent(int userId) {
        sendEmail(userId, VERIFICATION_REQUEST);
    }

    @Override
    public void handleVerificationReminderEvent(int userId) {
        sendEmail(userId, VERIFICATION_REMINDER);
    }

    private void sendEmail(int userId, Type type) {
        User user = getUser(userId);
        final String template;
        final String code;
        Map<String, Object> model = new HashMap();
        model.put("user", user);
        switch (type) {
            case NEW_USER:
                template = "welcome";
                code = "email.newUser.subject";
                model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
                break;
            case EMAIL_UPDATE:
                template = "verify";
                code = "email.emailUpdate.subject";
                model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
                break;
            case PASSWORD_UPDATE:
                template = "password_update";
                code = "email.passwordUpdate.subject";
                break;
            case VERIFICATION_REQUEST:
                template = "verification_request";
                code = "email.verificationRequest.subject";
                model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
                break;
            case VERIFICATION_REMINDER:
                template = "verify_reminder";
                code = "email.verificationReminder.subject";
                model.put("accountLink", linkBuilder.getAccountLink());
                break;
            default:
                throw new IllegalArgumentException("unknown verification type " + type);
        }

        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject(messageSource.getMessage(code, null, new Locale(user.getLanguage().name())));
            String text = mergeTemplateIntoString(velocityEngine, getTemplate(template, user.getLanguage()), "UTF-8", model);
            message.setText(text, true);
        });
    }

    private User getUser(int id) {
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("user with ID " + id + " does not exist");
        }
        return user.get();
    }

    private String getTemplate(String templateName, Language language) {
        return templateFolder + language.name().toLowerCase() + "/" + templateName + velocitySuffix;
    }
}