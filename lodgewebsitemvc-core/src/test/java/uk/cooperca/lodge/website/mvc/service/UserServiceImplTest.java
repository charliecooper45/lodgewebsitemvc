package uk.cooperca.lodge.website.mvc.service;

import io.jsonwebtoken.*;
import org.joda.time.DateTime;
import org.junit.Test;
import uk.cooperca.lodge.website.mvc.command.UserCommand;
import uk.cooperca.lodge.website.mvc.entity.User;
import uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.cooperca.lodge.website.mvc.messaging.message.NotificationMessage.Type.EMAIL_UPDATE;
import static uk.cooperca.lodge.website.mvc.token.TokenManager.SIGNATURE_ALGORITHM;

public class UserServiceImplTest extends AbstractServiceTest {

    @Test
    public void testRegisterUser() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1);
        when(userRepository.save(any(User.class))).thenReturn(null, user);
        UserCommand command = new UserCommand();
        command.setPassword("1Password");

        // error saving
        userService.registerUser(command, Locale.ENGLISH);
        verifyZeroInteractions(producer);

        // saved successfully
        userService.registerUser(command, Locale.ENGLISH);
        verify(producer).sendMessage(NotificationMessage.Type.NEW_USER, 1);
    }

    @Test
    public void testVerifyUser() {
        User user = mock(User.class);
        when(user.isVerified()).thenReturn(true, false);
        when(userRepository.findById(1)).thenReturn(Optional.empty(), Optional.of(user));
        when(userRepository.updateVerified(true, 1)).thenReturn(0, 1);

        // user not present
        User verifiedUser = userService.verifyUser(generateTestToken(1, now().plusDays(1).toDate(), tokenManager.getKey()));
        assertNull(verifiedUser);
        verify(userRepository, never()).updateVerified(true, 1);

        // no update
        verifiedUser = userService.verifyUser(generateTestToken(1, now().plusDays(1).toDate(), tokenManager.getKey()));
        assertNull(verifiedUser);
        verify(userRepository, never()).updateVerified(true, 1);

        // update fails
        verifiedUser = userService.verifyUser(generateTestToken(1, now().plusDays(1).toDate(), tokenManager.getKey()));
        assertNull(verifiedUser);
        verify(userRepository).updateVerified(true, 1);

        // success
        verifiedUser = userService.verifyUser(generateTestToken(1, now().plusDays(1).toDate(), tokenManager.getKey()));
        assertNotNull(verifiedUser);
        verify(userRepository, times(2)).updateVerified(true, 1);
    }

    @Test
    public void testVerifyUserExceptionHandling() {
        // expired token
        try {
            userService.verifyUser(generateTestToken(1, now().minusSeconds(1).toDate(), tokenManager.getKey()));
            fail();
        } catch (ExpiredJwtException e) {}

        // malformed token
        try {
            String token = new StringBuilder(generateTestToken(1, now().plusDays(1).toDate(), tokenManager.getKey()))
                    .insert(3, ".")
                    .toString();
            userService.verifyUser(token);
            fail();
        } catch (MalformedJwtException e) {}

        // invalid signature
        try {
            Key key = new SecretKeySpec(parseBase64Binary("othersecret"), SIGNATURE_ALGORITHM.getJcaName());
            String token = generateTestToken(1, now().plusDays(1).toDate(), key);
            userService.verifyUser(token);
            fail();
        } catch (SignatureException e) {}
    }

    private String generateTestToken(int userId, Date expiration, Key key) {
        return Jwts.builder().setIssuer(tokenManager.getDomain())
                .setSubject(String.valueOf(userId))
                .setExpiration(expiration)
                .signWith(SIGNATURE_ALGORITHM, key)
                .compact();
    }

    @Test
    public void testUpdateEmail() {
        int id = 1;
        String email = "mynewemail@yahoo.co.uk";
        when(userRepository.updateEmail(email, id)).thenReturn(0, 1);
        when(userRepository.updateVerified(false, id)).thenReturn(1);
        when(userRepository.updateVerificationRequestAt(any(DateTime.class), eq(id))).thenReturn(1);

        // failed
        try {
            userService.updateEmail(email, id);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("repository returned 0", e.getMessage());
            verifyZeroInteractions(producer);
        }

        // success
        userService.updateEmail(email, id);
        verify(producer).sendMessage(EMAIL_UPDATE, 1);
    }
}
