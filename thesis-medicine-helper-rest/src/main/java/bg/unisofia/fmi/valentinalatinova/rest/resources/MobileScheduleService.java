package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileScheduleDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileScheduleDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileScheduleDaoImpl;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/schedule")
public class MobileScheduleService {
    private MobileScheduleDao mobileScheduleDao;

    public MobileScheduleService() {
        this.mobileScheduleDao = new MobileScheduleDaoImpl();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MobileScheduleDto> getMobileSchedules(@Auth User user) {
        return mobileScheduleDao.getAll(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public ResultDto saveMobileSchedule(@Auth User user, MobileScheduleDto schedule) {
        schedule.setUserId(user.getId());
        return mobileScheduleDao.save(schedule);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public ResultDto updateMobileSchedule(@Auth User user, MobileScheduleDto schedule) {
        schedule.setUserId(user.getId());
        return mobileScheduleDao.update(schedule);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultDto deleteMobileSchedule(@Auth User user, @PathParam("id") long id) {
        return mobileScheduleDao.delete(id, user.getId());
    }
}
