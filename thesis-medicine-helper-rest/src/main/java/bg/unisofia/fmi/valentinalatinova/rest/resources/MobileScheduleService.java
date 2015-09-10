package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileScheduleDAO;
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
    public MobileScheduleService() {
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MobileSchedule> getMobileSchedules(@Auth User user) {
        return MobileScheduleDAO.getAll();
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String addMobileSchedule(@Auth User user, MobileSchedule schedule) {
        schedule.setUserId(user.getId());
        return MobileScheduleDAO.save(schedule);
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateMobileSchedule(@Auth User user, MobileSchedule schedule) {
        schedule.setUserId(user.getId());
        return MobileScheduleDAO.update(schedule);
    }

    @GET
    @Timed
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeMobileSchedule(@Auth User user, @PathParam("id") long id) {
        return MobileScheduleDAO.remove(id);
    }
}
