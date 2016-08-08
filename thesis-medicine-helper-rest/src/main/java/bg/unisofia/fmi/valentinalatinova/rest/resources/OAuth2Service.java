package bg.unisofia.fmi.valentinalatinova.rest.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.json.AuthToken;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDAO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDAO;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2Service {
    private ImmutableList<String> allowedGrantTypes;
    private AccessTokenDAO accessTokenDAO;
    private UserDAO userDao;
    private int accessTokenExpireTimeMinutes;

    public OAuth2Service(ImmutableList<String> allowedGrantTypes, AccessTokenDAO accessTokenDAO, UserDAO userDao,
            int accessTokenExpireTimeMinutes) {
        this.allowedGrantTypes = allowedGrantTypes;
        this.accessTokenDAO = accessTokenDAO;
        this.userDao = userDao;
        this.accessTokenExpireTimeMinutes = accessTokenExpireTimeMinutes;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password) {

        // Check if the grant type is allowed
        if (!allowedGrantTypes.contains(grantType)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(Result.createError("Authentication method not allowed"))
                    .build();
        }

        // Try to find a user with the supplied credentials.
        Optional<User> optUser = userDao.findByUsernameAndPassword(username, password);
        if (optUser == null || !optUser.isPresent()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Result.createError("Unauthorized"))
                    .build();
        }

        // User was found, generate a token and return it.
        User user = optUser.get();
        AccessToken accessToken = accessTokenDAO.generateAccessToken(user);
        DateTime expiryDate = accessToken.getLastAccess().plusMinutes(accessTokenExpireTimeMinutes);
        return replySuccess(accessToken.getAccessTokenId(), user, expiryDate);
    }

    private Response replySuccess(UUID uuid, User user, DateTime expiryDate) {
        AuthToken authToken = new AuthToken();
        authToken.setAuthToken(uuid);
        authToken.setFirstName(user.getFirstName());
        authToken.setLastName(user.getLastName());
        authToken.setIsDoctor(user.getIsDoctor());
        authToken.setExpiryDate(expiryDate);

        return Response.status(Response.Status.OK)
                .entity(authToken)
                .build();
    }
}
