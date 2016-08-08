package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;

public class AccessTokenDAO {
    private static Map<UUID, AccessToken> accessTokenTable = new HashMap<>();

    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        AccessToken accessToken = accessTokenTable.get(accessTokenId);
        if (accessToken == null) {
            return Optional.absent();
        }
        return Optional.of(accessToken);
    }

    public AccessToken generateAccessToken(final User user) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), user, new DateTime());
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }

    public void updateLastAccessTime(final UUID accessTokenUUID) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID);
        accessToken = new AccessToken(accessToken.getAccessTokenId(), accessToken.getUser(), new DateTime());
        accessTokenTable.put(accessTokenUUID, accessToken);
    }
}
