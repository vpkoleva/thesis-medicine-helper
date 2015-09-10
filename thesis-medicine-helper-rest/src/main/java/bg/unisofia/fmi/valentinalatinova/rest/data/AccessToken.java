package bg.unisofia.fmi.valentinalatinova.rest.data;

import org.joda.time.DateTime;

import java.util.UUID;

public class AccessToken {
    private UUID accessTokenId;
    private User user;
    private DateTime lastAccess;

    public AccessToken(UUID accessTokenId, User user, DateTime lastAccess) {
        this.accessTokenId = accessTokenId;
        this.user = user;
        this.lastAccess = lastAccess;
    }

    public UUID getAccessTokenId() {
        return accessTokenId;
    }

    public User getUser() {
        return user;
    }

    public DateTime getLastAccess() {
        return lastAccess;
    }
}
