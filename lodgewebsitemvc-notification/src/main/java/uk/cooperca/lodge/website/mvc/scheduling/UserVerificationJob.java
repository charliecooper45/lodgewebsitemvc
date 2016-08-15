package uk.cooperca.lodge.website.mvc.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.cooperca.lodge.website.mvc.consumer.NotificationMessageConsumers;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.service.UserService;

import static org.joda.time.DateTime.now;

/**
 * Job that manages user verification. Sends a reminder email to users who are unverified and have not been sent a verification
 * email in the last 5 days.
 *
 * @author Charlie Cooper
 */
@Component
public class UserVerificationJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageConsumers.class);

    @Autowired
    public UserService userService;

    @Scheduled(cron = "${jobs.userVerification.cron}")
    public void run() {
        int count = 0;
        LOGGER.info("Starting user verification job...");
        for (User user : userService.getUnverifiedUsersBefore(now().minusDays(5))) {
            int id = user.getId();
            userService.sendVerificationReminder(id);
            count++;
            LOGGER.info("Sent verification reminder to user with ID {} who last requested verification on {}",
                    id, user.getVerificationRequestAt());
        }
        LOGGER.info("Verification job completed. Sent {} verification emails.", count);
    }
}
