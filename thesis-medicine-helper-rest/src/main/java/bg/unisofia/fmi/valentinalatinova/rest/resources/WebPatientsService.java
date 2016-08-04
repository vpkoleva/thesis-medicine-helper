package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.PatientBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.dao.PatientDAO;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/web/patient")
public class WebPatientsService {
    private PatientDAO patientDao;

    public WebPatientsService(DataBaseCommander dataBaseCommander) {
        this.patientDao = new PatientDAO(dataBaseCommander);
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PatientBO> getPatientsByDoctorId(@Auth User user) {
        try {
            return patientDao.getPatientsByDoctorId(user.getId());
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Timed
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Result addPatient(@Auth User user, PatientBO pat) {
        try {
            return patientDao.addPatient(user.getId(), pat.getFirstName(), pat.getLastName(), pat.getDiagnoseId());
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Timed
    @Path("/link")
    @Produces(MediaType.APPLICATION_JSON)
    public Result linkPatientToMobile(@Auth User user,  @PathParam("patientID") long patientID,@PathParam("code") String code) {
        try {
            return patientDao.linkPatientToMobileUser(user.getId(), patientID, code);
        } catch (Exception e) {
            return null;
        }
    }

}
