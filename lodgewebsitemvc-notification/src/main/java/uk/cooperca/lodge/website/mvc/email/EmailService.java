package uk.cooperca.lodge.website.mvc.email;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type;
import uk.cooperca.lodge.website.mvc.service.UserService;

import java.util.Optional;

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

    public void sendEmail(Type type, int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            switch(type) {
                case EMAIL_UPDATE:
                    sendEmailUpdateEmail(user.get());
                    break;
            }
        }
    }

    private void sendEmailUpdateEmail(User user) {
//        sender.send(mimeMessage -> {
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//            message.setTo(user.getEmail());
//            message.setSubject("Lodge Website - Email Update");
//            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/template/test.vm", "UTF-8", null);
//            message.setText(text, true);
//        });
    }
}