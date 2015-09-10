package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import com.google.common.base.Optional;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccessTokenDAO {
    private static Map<UUID, AccessToken> accessTokenTable = new HashMap<>();

    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        AccessToken accessToken = accessTokenTable.get(accessTokenId);
        if (accessToken == null) {
            return Optional.absent();
        }
        return Optional.of(accessToken);
    }

    public AccessToken generateAccessToken(final User user, final DateTime dateTime) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), user, dateTime);
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }

    public void updateLastAccessTime(final UUID accessTokenUUID, final DateTime dateTime) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID);
        accessToken = new AccessToken(accessToken.getAccessTokenId(), accessToken.getUser(), dateTime);
        accessTokenTable.put(accessTokenUUID, accessToken);
    }
}
