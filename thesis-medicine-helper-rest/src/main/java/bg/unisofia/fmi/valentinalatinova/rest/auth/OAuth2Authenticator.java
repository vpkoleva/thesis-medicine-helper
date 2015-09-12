package bg.unisofia.fmi.valentinalatinova.rest.auth;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDaoo;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.UUID;

public class OAuth2Authenticator implements Authenticator<String, User> {
    private AccessTokenDaoo accessTokenDao;
    private boolean authDisabled;
    private int accessTokenExpireTimeMinutes;

    public OAuth2Authenticator(AccessTokenDaoo accessTokenDao, boolean authDisabled, int accessTokenExpireTimeMinutes) {
        this.accessTokenDao = accessTokenDao;
        this.authDisabled = authDisabled;
        this.accessTokenExpireTimeMinutes = accessTokenExpireTimeMinutes;
    }

    @Override
    public Optional<User> authenticate(String accessTokenId) throws AuthenticationException {
        // Bypass authentication based on configuration
        if (authDisabled) {
            return Optional.of(User.getNoAuthUser());
        }

        // Check input, must be a valid UUID
        UUID accessTokenUUID;
        try {
            accessTokenUUID = UUID.fromString(accessTokenId);
        } catch (IllegalArgumentException e) {
            return Optional.absent();
        }

        // Get the access token from the database
        Optional<AccessToken> accessToken = accessTokenDao.findAccessTokenById(accessTokenUUID);
        if (accessToken == null || !accessToken.isPresent()) {
            return Optional.absent();
        }

        // Check if the last access time is not too far in the past (the access token is expired)
        Period period = new Period(accessToken.get().getLastAccess(), new DateTime());
        if (period.getMinutes() > accessTokenExpireTimeMinutes) {
            return Optional.absent();
        }

        // Update the access time for the token
        accessTokenDao.updateLastAccessTime(accessTokenUUID);

        // Return the user's id for processing
        return Optional.of(accessToken.get().getUser());
    }
}
