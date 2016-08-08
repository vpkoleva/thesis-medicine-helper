package bg.unisofia.fmi.valentinalatinova.rest.resources;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.json.Patient;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.PatientDAO;

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
    public List<Patient> getPatientsByDoctorId(@Auth User user) {
        return patientDao.get(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Result savePatient(@Auth User user, Patient patient) {
        return patientDao.save(patient, user.getId());
    }

    @DELETE
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteDiagnose(@Auth User user, @PathParam("id") long id) {
        return patientDao.delete(id, user.getId());
    }

    @POST
    @Timed
    @Path("/link")
    @Produces(MediaType.APPLICATION_JSON)
    public Result linkPatientToMobile(@Auth User user, Map<String, String> values) {
        return patientDao.linkPatientToMobileUser(values.get("patientId"), values.get("code"));
    }
}
