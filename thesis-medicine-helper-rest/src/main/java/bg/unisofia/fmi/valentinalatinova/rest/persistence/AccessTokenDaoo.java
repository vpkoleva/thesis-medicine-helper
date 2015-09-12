package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import com.google.common.base.Optional;

import java.util.UUID;

public interface AccessTokenDaoo {

    /**
     * Finds access token by id.
     *
     * @param accessTokenId
     * @return AccessToken
     */
    Optional<AccessToken> findAccessTokenById(final UUID accessTokenId);

    /**
     * Generates access token for given user with currect date time.
     *
     * @param user
     * @return AccessToken if found or absent if not
     */
    AccessToken generateAccessToken(final User user);

    /**
     * Updates last access time to be current time of the given access token UUID.
     *
     * @param accessTokenUUID
     */
    void updateLastAccessTime(final UUID accessTokenUUID);
}
