package bg.unisofia.fmi.valentinalatinova.rest.resources;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileResultsDAO;

@Path("/mobile/results")
public class MobileResultsService {
    private MobileResultsDAO mobileResultsDao;

    public MobileResultsService(DataBaseCommander dataBaseCommander) {
        mobileResultsDao = new MobileResultsDAO(dataBaseCommander);
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
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileResults(@Auth User user, MobileResults result) {
        return mobileResultsDao.saveResult(result, user.getId());
    }

    @POST
    @Timed
    @Path("/value/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileResultsValue(@Auth User user, MobileResultsValue resultsValue) {
        return mobileResultsDao.saveValue(resultsValue, user.getId());
    }

    @POST
    @Timed
    @Path("/value/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileResultsValue(@Auth User user, MobileResultsValue resultsValue) {
        return mobileResultsDao.updateValue(resultsValue, user.getId());
    }

    @DELETE
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileResults(@Auth User user, @PathParam("id") long id) {
        return mobileResultsDao.deleteResult(id, user.getId());
    }

    @DELETE
    @Timed
    @Path("/value/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileResultsValue(@Auth User user, @PathParam("id") long id) {
        return mobileResultsDao.deleteValue(id, user.getId());
    }
}
