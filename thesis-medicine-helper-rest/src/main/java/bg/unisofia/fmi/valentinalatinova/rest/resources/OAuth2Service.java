package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.AccessToken;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDao;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class OAuth2Service {
    private ImmutableList<String> allowedGrantTypes;
    private AccessTokenDao accessTokenDAO;
    private UserDao userDao;

    public OAuth2Service(ImmutableList<String> allowedGrantTypes, AccessTokenDao accessTokenDAO, UserDao userDao) {
        this.allowedGrantTypes = allowedGrantTypes;
        this.accessTokenDAO = accessTokenDAO;
        this.userDao = userDao;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap.SimpleEntry<String, String> getToken(@FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password) {

        // Check if the grant type is allowed
        if (!allowedGrantTypes.contains(grantType)) {
            Response response = Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
            throw new WebApplicationException(response);
        }

        // Try to find a user with the supplied credentials.
        Optional<User> user = userDao.findByUsernameAndPassword(username, password);
        if (user == null || !user.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        // User was found, generate a token and return it.
        AccessToken accessToken = accessTokenDAO.generateAccessToken(user.get());
        return new HashMap.SimpleEntry<String, String>("authToken", accessToken.getAccessTokenId().toString());
    }
}
