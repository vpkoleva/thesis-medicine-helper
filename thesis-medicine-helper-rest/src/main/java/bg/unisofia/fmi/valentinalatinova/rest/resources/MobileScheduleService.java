package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileScheduleDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileScheduleDaoImpl;
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
    public List<MobileSchedule> getMobileSchedules(@Auth User user) {
        return mobileScheduleDao.getAll(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileSchedule(@Auth User user, MobileSchedule schedule) {
        schedule.setUserId(user.getId());
        return mobileScheduleDao.save(schedule);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileSchedule(@Auth User user, MobileSchedule schedule) {
        schedule.setUserId(user.getId());
        return mobileScheduleDao.update(schedule);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileSchedule(@Auth User user, @PathParam("id") long id) {
        return mobileScheduleDao.delete(id, user.getId());
    }
}
