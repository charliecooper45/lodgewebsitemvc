package uk.cooperca.lodge.website.mvc.email;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.link.LinkBuilder;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.ui.velocity.VelocityEngineUtils.mergeTemplateIntoString;

/**
 * Service to send emails to users.
 *
 * @author Charlie Cooper
 */
@Component
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkBuilder linkBuilder;

    public void sendEmail(Type type, int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            switch(type) {
                case NEW_USER:
                    sendNewUserEmail(user.get());
                    break;
                case VERIFY_EMAIL:
                    sendEmailUpdateEmail(user.get());
                    break;
            }
        }
    }

    private void sendNewUserEmail(User user) {
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject("Lodge Website - Welcome!");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
            String text = mergeTemplateIntoString(velocityEngine, "/template/welcome.vm", "UTF-8", model);
            message.setText(text, true);
        });
    }

    private void sendEmailUpdateEmail(User user) {
        sender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(user.getEmail());
            message.setSubject("Lodge Website - Email Update");
            Map<String, Object> model = new HashMap();
            model.put("user", user);
            model.put("verificationLink", linkBuilder.getVerificationLink(user.getId()));
            String text = mergeTemplateIntoString(velocityEngine, "/template/verify.vm", "UTF-8", model);
            message.setText(text, true);
        });
    }
}