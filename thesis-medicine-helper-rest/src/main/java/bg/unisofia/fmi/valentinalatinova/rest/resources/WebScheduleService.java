package bg.unisofia.fmi.valentinalatinova.rest.resources;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.json.ScheduleList;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import bg.unisofia.fmi.valentinalatinova.rest.helpers.SchedulesConversion;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.WebScheduleDAO;

@Path("/web/schedule")
public class WebScheduleService {
    private WebScheduleDAO webScheduleDao;
    private SchedulesConversion convert;

    public WebScheduleService(DataBaseCommander dataBaseCommander) {
        this.webScheduleDao = new WebScheduleDAO(dataBaseCommander);
        convert = new SchedulesConversion();
    }

    @GET
    @Timed
    @Path("/all/diagnose/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ScheduleList> getWebSchedules(@Auth User user, @PathParam("id") int diagnoseId,
            @QueryParam("start") String start, @QueryParam("end") String end) {
        List<WebScheduleDO> schedulesFromDB = webScheduleDao.getByDiagnoseId(diagnoseId, user.getId());
        return convert.convertDOtoJsonList(schedulesFromDB, start, end);
    }

    @GET
    @Timed
    @Path("/all/patient/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ScheduleList> getWebSchedulesForPatiens(@Auth User user, @PathParam("id") int patientId,
            @QueryParam("start") String start, @QueryParam("end") String end) {
        List<WebScheduleDO> schedulesFromDB = webScheduleDao.getByPatientId(patientId, user.getId());
        return convert.convertDOtoJsonList(schedulesFromDB, start, end);
    }

    @GET
    @Timed
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Schedule get(@Auth User user, @PathParam("id") long id) {
        return webScheduleDao.getById(id, user.getId());
    }

    @POST
    @Timed
    @Path("/save/default")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveWebScheduleFromDefault(@Auth User user, Map<String, String> values) {
        return webScheduleDao.addScheduleToPatientFromDefaultDiagnose(values.get("startDate"), values.get("patientId"));
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result saveWebSchedule(@Auth User user, Schedule schedule) {
        return webScheduleDao.save(convert.convertJsonToDO(schedule), user.getId());
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateWebSchedule(@Auth User user, Schedule schedule) {
        return webScheduleDao.update(convert.convertJsonToDO(schedule), user.getId());
    }

    @DELETE
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteWebSchedule(@Auth User user, @PathParam("id") long id) {
        return webScheduleDao.delete(id, user.getId());
    }
}
