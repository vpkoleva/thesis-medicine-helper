package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileTable;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileTableValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileTableDaoImpl;
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

@Path("/mobile/table")
public class MobileTableService {
    private MobileTableDao mobileTableDao;

    public MobileTableService() {
        mobileTableDao = new MobileTableDaoImpl();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MobileTable> getMobileTable(@Auth User user) {
        return mobileTableDao.getTables(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileTableValue(@Auth User user, MobileTableValue tableValue) {
        tableValue.setUserId(user.getId());
        return mobileTableDao.save(tableValue);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileTableValue(@Auth User user, MobileTableValue tableValue) {
        tableValue.setUserId(user.getId());
        return mobileTableDao.update(tableValue);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileTableValue(@Auth User user, @PathParam("id") long id) {
        return mobileTableDao.delete(id, user.getId());
    }
}
