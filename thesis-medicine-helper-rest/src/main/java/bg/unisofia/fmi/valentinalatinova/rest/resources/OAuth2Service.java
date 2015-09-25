package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.dto.AuthTokenDto;
import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDao;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2Service {
    private ImmutableList<String> allowedGrantTypes;
    private AccessTokenDao accessTokenDAO;
    private UserDao userDao;
    private boolean isAuthenticationDisabled;
    private int accessTokenExpireTimeMinutes;

    public OAuth2Service(ImmutableList<String> allowedGrantTypes, AccessTokenDao accessTokenDAO,
            UserDao userDao, boolean isAuthenticationDisabled, int accessTokenExpireTimeMinutes) {
        this.allowedGrantTypes = allowedGrantTypes;
        this.accessTokenDAO = accessTokenDAO;
        this.userDao = userDao;
        this.isAuthenticationDisabled = isAuthenticationDisabled;
        this.accessTokenExpireTimeMinutes = accessTokenExpireTimeMinutes;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthTokenDto getToken(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password) {

        if (isAuthenticationDisabled) {
            return new AuthTokenDto(UUID.randomUUID(), User.getNoAuthUser().getFirstName(),
                    User.getNoAuthUser().getLastName(), DateTime.now().plusMonths(1));
        }
        // Check if the grant type is allowed
        if (!allowedGrantTypes.contains(grantType)) {
            Response response = Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
            throw new WebApplicationException(response);
        }

        // Try to find a user with the supplied credentials.
        Optional<User> optUser = userDao.findByUsernameAndPassword(username, password);
        if (optUser == null || !optUser.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        // User was found, generate a token and return it.
        User user = optUser.get();
        AccessToken accessToken = accessTokenDAO.generateAccessToken(user);
        DateTime expiryDate = accessToken.getLastAccess().plusMinutes(accessTokenExpireTimeMinutes);
        return new AuthTokenDto(accessToken.getAccessTokenId(), user.getFirstName(), user.getLastName(), expiryDate);
    }
}
