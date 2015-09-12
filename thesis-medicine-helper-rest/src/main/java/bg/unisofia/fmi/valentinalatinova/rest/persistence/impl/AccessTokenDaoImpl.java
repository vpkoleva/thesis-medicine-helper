package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDaoo;
import com.google.common.base.Optional;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccessTokenDaoImpl implements AccessTokenDaoo {
    private static Map<UUID, AccessToken> accessTokenTable = new HashMap<>();

    @Override
    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        AccessToken accessToken = accessTokenTable.get(accessTokenId);
        if (accessToken == null) {
            return Optional.absent();
        }
        return Optional.of(accessToken);
    }

    @Override
    public AccessToken generateAccessToken(final User user) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), user, new DateTime());
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }

    @Override
    public void updateLastAccessTime(final UUID accessTokenUUID) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID);
        accessToken = new AccessToken(accessToken.getAccessTokenId(), accessToken.getUser(), new DateTime());
        accessTokenTable.put(accessTokenUUID, accessToken);
    }
}
