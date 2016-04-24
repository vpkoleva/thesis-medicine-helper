package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleBO;
import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleListBO;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import bg.unisofia.fmi.valentinalatinova.rest.helpers.SchedulesConversion;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.dao.WebScheduleDAO;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.joda.time.DateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WebScheduleListBO> getWebSchedules(@Auth User user, @QueryParam("diagnoseId") int diagnoseId, @QueryParam("start") String start, @QueryParam("end") String end) {
        List<WebScheduleDO> schedulesFromDB = webScheduleDao.getScheduleByDiagnoseIdWithLimits(diagnoseId, start, end, user.getId());

        return convert.convertDOtoBOList(schedulesFromDB, start, end);
    }

//    @GET
//    @Timed
//    @Path("/all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<WebScheduleListBO> getWebSchedules(@Auth User user, @QueryParam("diagnoseId") int diagnoseId, @QueryParam("start") String start, @QueryParam("end") String end) {
//        List<WebScheduleDO> schedulesFromDB = webScheduleDao.getScheduleByDiagnoseIdWithLimits(diagnoseId, start, end, user.getId());
//
//        List<WebScheduleListBO> schedulesList = convert.convertDOtoBOList(schedulesFromDB, start, end);
//
//        return schedulesList;
//    }

    @POST
    @Timed
    @Path("/saveFromDefault")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes({MediaType.APPLICATION_JSON})
    public void saveWebScheduleFromDefault(@Auth User user,  @QueryParam("diagnoseId") int diagnoseId, @QueryParam("startDate") String startDate, @QueryParam("patientId") String patientId) {
         webScheduleDao.addScheduleToPatientFromDefaultDiagnose(diagnoseId, startDate, patientId, user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public long saveMobileSchedule(@Auth User user, WebScheduleBO schedule) {
        ArrayList<WebScheduleBO> arr = new ArrayList<>();
        arr.add(schedule);
        WebScheduleDO webScheduleDO = new WebScheduleDO();
        webScheduleDO = convert.convertBOtoDO(arr).get(0);
        return webScheduleDao.addScheduleByUserId(webScheduleDO, user.getId());
    }

    @POST
    @Timed
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Result updateMobileSchedule(@Auth User user, MobileSchedule schedule) {
        schedule.setUserId(user.getId());
        return null;
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteMobileSchedule(@Auth User user, @PathParam("id") long id) {
        return null;
    }
}
