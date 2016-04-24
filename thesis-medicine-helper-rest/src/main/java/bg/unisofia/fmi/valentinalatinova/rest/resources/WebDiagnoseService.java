package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.DiagnoseBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.dao.DiagnoseDAO;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/web/diagnose")
public class WebDiagnoseService {
    private DiagnoseDAO diagnoseDao;

    public WebDiagnoseService(DataBaseCommander dataBaseCommander) {
        this.diagnoseDao = new DiagnoseDAO(dataBaseCommander);
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiagnoseBO> getPatientsByDoctorId(@Auth User user) {
        return diagnoseDao.getDiagnosesByUserId(user.getId());
    }

    @POST
    @Timed
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public long addNewDiagnose(@Auth User user, DiagnoseBO diagnose) {
        return diagnoseDao.addDiagnosesByUserId(diagnose.getDiagnoseName(), user.getId());
    }
}
