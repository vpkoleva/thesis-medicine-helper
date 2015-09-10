package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDAO;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

@Path("/mobile/table")
public class MobileTableService {
    public MobileTableService() {
    }

    @GET
    @Timed
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, List> getMobileTable(@Auth User user) {
        return MobileTableDAO.getTable(user.getId());
    }
}
