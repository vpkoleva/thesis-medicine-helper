package bg.unisofia.fmi.valentinalatinova.rest.resources;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.json.Diagnose;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DiagnoseDAO;

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
    public List<Diagnose> getDiagnoses(@Auth User user) {
        return diagnoseDao.get(user.getId());
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Result saveDiagnose(@Auth User user, Diagnose diagnose) {
        return diagnoseDao.save(diagnose, user.getId());
    }

    @DELETE
    @Timed
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteDiagnose(@Auth User user, @PathParam("id") long id) {
        return diagnoseDao.delete(id, user.getId());
    }
}
