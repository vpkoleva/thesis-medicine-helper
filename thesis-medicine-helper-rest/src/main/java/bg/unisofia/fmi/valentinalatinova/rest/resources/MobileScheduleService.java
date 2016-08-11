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

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.json.ScheduleInfo;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import bg.unisofia.fmi.valentinalatinova.rest.helpers.SchedulesConversion;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.SchedulesDAO;

@Path("/mobile/schedule")
public class MobileScheduleService {
    private SchedulesDAO schedulesDAO;
    private SchedulesConversion convert;

    public MobileScheduleService(DataBaseCommander dataBaseCommander) {
        this.schedulesDAO = new SchedulesDAO(dataBaseCommander);
        convert = new SchedulesConversion();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ScheduleInfo> getMobileSchedules(@Auth User user) {
        List<WebScheduleDO> schedulesFromDB = schedulesDAO.findByMobileUserId(user.getId());
        return convert.convertDOtoJsonList(schedulesFromDB, new DateTime(0L).toString("yyyy-MM-dd"),
                new DateTime(2020, 12, 31, 23, 59).toString("yyyy-MM-dd"));
    }

    @GET
    @Timed
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Schedule get(@Auth User user, @PathParam("id") long id) {
        return schedulesDAO.getById(id, user);
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveMobileSchedule(@Auth User user, Schedule schedule) {
        return schedulesDAO.save(convert.convertJsonToDO(schedule), user.getId());
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileSchedule(@Auth User user, Schedule schedule) {
        return schedulesDAO.update(convert.convertJsonToDO(schedule), user);
    }

    @DELETE
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileSchedule(@Auth User user, @PathParam("id") long id) {
        return schedulesDAO.delete(id, user);
    }
}
