package bg.unisofia.fmi.valentinalatinova.rest;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.setup.Environment;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bg.unisofia.fmi.valentinalatinova.rest.auth.OAuth2Authenticator;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDAO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDAO;
import bg.unisofia.fmi.valentinalatinova.rest.resources.CorsResponseFilter;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileResultsService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileScheduleService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.OAuth2Service;
import bg.unisofia.fmi.valentinalatinova.rest.resources.WebDiagnoseService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.WebPatientsService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.WebScheduleService;

public class MedicineApp extends Application<MedicineConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineApp.class);

    public static void main(String[] args) throws Exception {
        new MedicineApp().run(args);
    }

    @Override
    public void run(MedicineConfig config, Environment environment) throws Exception {
        final int tokenExpiryMinutes = config.getOAuth().getAccessTokenExpireTimeMinutes();

        // App will not start if DataBaseCommander cannot be instantiated
        DataBaseCommander dataBaseCommander;
        try {
            dataBaseCommander = new DataBaseCommander(config.getDatabase());
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException ex) {
            LOGGER.error("Error during DataBaseCommander instantiation:", ex);
            throw ex;
        }

        // Register CORS Response filter
        CorsResponseFilter filter = new CorsResponseFilter(config.getAllowCorsForDomain());
        environment.jersey().register(filter);

        // Create DAOs
        AccessTokenDAO accessTokenDao = new AccessTokenDAO();
        UserDAO userDao = new UserDAO(dataBaseCommander);

        // Register oauth2 service
        final OAuth2Service oAuth2Service = new OAuth2Service(config.getOAuth().getAllowedGrantTypes(),
                accessTokenDao, userDao, tokenExpiryMinutes);
        environment.jersey().register(oAuth2Service);

        // Register mobile schedule service
        final MobileScheduleService mobileScheduleService = new MobileScheduleService(dataBaseCommander);
        environment.jersey().register(mobileScheduleService);

        // Register mobile schedule service
        final WebPatientsService patientService = new WebPatientsService(dataBaseCommander);
        environment.jersey().register(patientService);

        // Register mobile diagnose service
        final WebDiagnoseService diagnoseService = new WebDiagnoseService(dataBaseCommander);
        environment.jersey().register(diagnoseService);

        // Register web schedule service
        final WebScheduleService webScheduleService = new WebScheduleService(dataBaseCommander);
        environment.jersey().register(webScheduleService);

        // Register mobile results service
        final MobileResultsService mobileResultsService = new MobileResultsService(dataBaseCommander);
        environment.jersey().register(mobileResultsService);

        // Register health check
        final MedicineCheck healthCheck = new MedicineCheck();
        environment.healthChecks().register("healthCheck", healthCheck);
        environment.jersey().register(healthCheck);

        // Register security component
        OAuth2Authenticator authenticator = new OAuth2Authenticator(accessTokenDao, tokenExpiryMinutes);
        environment.jersey().register(
                AuthFactory.binder(new OAuthFactory<>(authenticator, "oauth2-provider", User.class)));
    }
}