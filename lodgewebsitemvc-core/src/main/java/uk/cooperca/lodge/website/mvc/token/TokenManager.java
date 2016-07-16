package uk.cooperca.lodge.website.mvc.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Required;

import javax.crypto.spec.SecretKeySpec;
import java.security.*;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static org.joda.time.LocalDateTime.now;
import static org.joda.time.Period.days;

/**
 * Manager that generates/decodes tokens used on the site.
 *
 * @author Charlie Cooper
 */
public class TokenManager {

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final Key key;
    private final String domain;

    private int verificationExpirationDays;

    public TokenManager(String secret, String domain) {
        this.key = new SecretKeySpec(parseBase64Binary(secret), SIGNATURE_ALGORITHM.getJcaName());
        this.domain = domain;
    }

    /**
     * Generates a verification token for the given user.
     *
     * @param userId the id of the user
     *
     * @return the encrypted token
     */
    public String generateVerificationToken(int userId) {
        return Jwts.builder().setIssuer(domain)
                .setSubject(String.valueOf(userId))
                .setExpiration(now().plus(days(verificationExpirationDays)).toDate())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    /**
     * Decodes the given token.
     *
     * @param token the encrypted token
     *
     * @return the decrypted token
     *
     * @throws MalformedJwtException if the specified token is invalid.
     * @throws SignatureException if a JWS signature was discovered, but could not be verified
     * @throws ExpiredJwtException if the "exp" claim is in the past
     */
    public Jwt decodeToken(String token) {
        return Jwts.parser().setSigningKey(key).parse(token);
    }

    public String getDomain() {
        return domain;
    }

    public Key getKey() {
        return key;
    }

    @Required
    public void setVerificationExpirationDays(int verificationExpirationDays) {
        this.verificationExpirationDays = verificationExpirationDays;
    }
}
