package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileTableDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileTableValueDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileTableDaoImpl;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
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
    public List<MobileTableDto> getMobileTable(@Auth User user) {
        return mobileTableDao.getTables(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public ResultDto saveMobileTableValue(@Auth User user, MobileTableValueDto tableValue) {
        tableValue.setUserId(user.getId());
        return mobileTableDao.save(tableValue);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public ResultDto updateMobileTableValue(@Auth User user, MobileTableValueDto tableValue) {
        tableValue.setUserId(user.getId());
        return mobileTableDao.update(tableValue);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultDto deleteMobileTableValue(@Auth User user, @PathParam("id") long id) {
        return mobileTableDao.delete(id, user.getId());
    }
}
