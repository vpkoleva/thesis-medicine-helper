package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
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

@Path("/mobile/results/value")
public class MobileResultsValueService {
    private MobileResultsDaoImpl mobileResultsDao;

    public MobileResultsValueService() {
        mobileResultsDao = new MobileResultsDaoImpl();
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileResultsValue(@Auth User user, MobileResultsValue resultsValue) {
        resultsValue.setUserId(user.getId());
        return mobileResultsDao.save(resultsValue);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileResultsValue(@Auth User user, MobileResultsValue resultsValue) {
        resultsValue.setUserId(user.getId());
        return mobileResultsDao.update(resultsValue);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileResultsValue(@Auth User user, @PathParam("id") long id) {
        return mobileResultsDao.delete(id, user.getId());
    }
}
