package io.gcandal.payments.server.utils;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.gcandal.payments.server.configuration.PaymentAppsConfiguration;
import io.gcandal.payments.server.core.User;
import io.gcandal.payments.server.db.UserDAO;
import org.hibernate.SessionFactory;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

public final class AuthUtils {
    private static final String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int ENCRYPTION_ALGORITHM_KEY_LENGTH = 512;
    private static final int ENCRYPTION_ALGORITHM_ITERATIONS = 10;
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int TOKEN_DURATION_MIN = 30;

    private AuthUtils() { }

    public static PasswordAndSalt hash(final String password) throws NoSuchAlgorithmException {
        final byte[] salt = getSalt();
        final String hashedPassword = hash(password, salt);

        return new PasswordAndSalt(hashedPassword, Base64.getEncoder().encodeToString(salt));
    }

    private static String hash(final String password, final byte[] salt) {
        if (password == null) {
            return "";
        }

        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
            final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ENCRYPTION_ALGORITHM_ITERATIONS, ENCRYPTION_ALGORITHM_KEY_LENGTH);
            final SecretKey key = keyFactory.generateSecret(keySpec);
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch(final NoSuchAlgorithmException|InvalidKeySpecException e) {
            throw new RuntimeException("Something went wrong while hashing password.", e);
        }
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        final SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    public static boolean matches(final String original, final String candidate, final String salt) {
        return original.equals(hash(candidate, Base64.getDecoder().decode(salt)));
    }

    public static String getValidToken(final byte[] tokenSecret,
                                       final String subject) throws JoseException {
        final JwtClaims claims = new JwtClaims();
        claims.setSubject(subject);
        claims.setExpirationTimeMinutesInTheFuture(TOKEN_DURATION_MIN);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));

        return jws.getCompactSerialization();
    }

    public static AuthDynamicFeature getAuthFeature(final SessionFactory sessionFactory,
                                                    final byte[] tokenSecret,
                                                    final HibernateBundle<PaymentAppsConfiguration> hibernateBundle) {

        final UserDAO userDAO = new UserDAO(sessionFactory);
        final JwtAuthenticator proxyAuth = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(JwtAuthenticator.class, UserDAO.class, userDAO);

        return new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                        .setJwtConsumer(getJwtConsumer(tokenSecret))
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(proxyAuth)
                        .buildAuthFilter());
    }

    private static JwtConsumer getJwtConsumer(final byte[] tokenSecret) {
        return new JwtConsumerBuilder()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireExpirationTime()
                    .setRequireSubject()
                    .setVerificationKey(new HmacKey(tokenSecret))
                    .setRelaxVerificationKeyValidation()
                    .build();
    }

    private static class JwtAuthenticator implements Authenticator<JwtContext, User> {

        private final UserDAO userDAO;

        JwtAuthenticator(final UserDAO userDAO) {
             this.userDAO = userDAO;
        }

        @UnitOfWork
        @Override
        public Optional<User> authenticate(final JwtContext context) {
            try {
                final String subject = context.getJwtClaims().getSubject();
                final List<User> matchingUsers = this.userDAO.getByName(subject);

                if (matchingUsers.isEmpty()) {
                    return Optional.empty();
                }

                return Optional.of(matchingUsers.get(0));
            }
            catch (final MalformedClaimException e) {
                return Optional.empty();
            }
        }
    }
}
