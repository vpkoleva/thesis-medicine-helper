package bg.unisofia.fmi.valentinalatinova.rest.resources;

import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileTableDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDaoo;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.MobileTableDaoImpl;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/mobile/table")
public class MobileTableService {
    private MobileTableDaoo mobileTableDaoo;

    public MobileTableService() {
        mobileTableDaoo = new MobileTableDaoImpl();
    }

    @GET
    @Timed
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MobileTableDto> getMobileTable(@Auth User user) {
        return mobileTableDaoo.getTables(user.getId());
    }
}
