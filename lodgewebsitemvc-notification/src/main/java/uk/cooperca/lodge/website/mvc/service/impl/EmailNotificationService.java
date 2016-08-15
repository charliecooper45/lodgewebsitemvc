package uk.cooperca.lodge.website.mvc.service.impl;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.entity.User.Language;
import uk.cooperca.lodge.website.mvc.link.LinkBuilder;
import uk.cooperca.lodge.website.mvc.service.NotificationService;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

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

    private final String templateFolder = "/template/";
    private final String velocitySuffix = ".vm";

    // TODO: Russian emails
    // TODO: remove duplication
    @Override
    public void handleNewUserEvent(int userId) {
        User user = getUser(userId);
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            // TODO: externalise subject
            message.setSubject("Lodge Website - Welcome!");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
            String text = mergeTemplateIntoString(velocityEngine, getTemplate("welcome", user.getLanguage()), "UTF-8",
                    model);
            message.setText(text, true);
        });
    }

    @Override
    public void handleEmailUpdateEvent(int userId) {
        User user = getUser(userId);
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject("Lodge Website - Email Update");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
            String text = mergeTemplateIntoString(velocityEngine, getTemplate("verify", user.getLanguage()), "UTF-8",
                    model);
            message.setText(text, true);
        });
    }

    @Override
    public void handlePasswordUpdateEvent(int userId) {
        User user = getUser(userId);
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject("Lodge Website - Password Update");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            String text = mergeTemplateIntoString(velocityEngine, getTemplate("password_update", user.getLanguage()),
                    "UTF-8", model);
            message.setText(text, true);
        });
    }

    @Override
    public void handleVerificationReminderEvent(int userId) {
        User user = getUser(userId);
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject("Lodge Website - Verification Reminder");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            model.put("accountLink", linkBuilder.getAccountLink());
            String text = mergeTemplateIntoString(velocityEngine, getTemplate("verify_reminder", user.getLanguage()),
                    "UTF-8", model);
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