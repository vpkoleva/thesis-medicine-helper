package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileResultsDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileResultsDaoImpl;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/results")
public class MobileResultsService {
    private MobileResultsDao mobileResultsDao;

    public MobileResultsService() {
        mobileResultsDao = new MobileResultsDaoImpl();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MobileResults> getMobileResults(@Auth User user) {
        return mobileResultsDao.getResults(user.getId());
    }

    @POST
    @Timed
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileResults(@Auth User user, MobileResults result) {
        result.setUserId(user.getId());
        return mobileResultsDao.createResult(result);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileResults(@Auth User user, @PathParam("id") long id) {
        return mobileResultsDao.deleteResult(id, user.getId());
    }
}
