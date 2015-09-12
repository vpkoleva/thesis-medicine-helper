package bg.unisofia.fmi.valentinalatinova.rest;

import bg.unisofia.fmi.valentinalatinova.rest.auth.OAuth2Authenticator;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDaoo;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDaoo;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.AccessTokenDaoImpl;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.UserDaoImpl;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileScheduleService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileTableService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.OAuth2Service;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.setup.Environment;

public class MedicineApp extends Application<MedicineConfig> {

    public static void main(String[] args) throws Exception {
        new MedicineApp().run(args);
    }

    @Override
    public void run(MedicineConfig config, Environment environment) {
        // Create DAOs
        AccessTokenDaoo accessTokenDAO = new AccessTokenDaoImpl();
        UserDaoo userDAO = new UserDaoImpl();

        // Register oauth2 service
        final OAuth2Service oAuth2Service = new OAuth2Service(config.getOAuth().getAllowedGrantTypes(),
                accessTokenDAO, userDAO);
        environment.jersey().register(oAuth2Service);

        // Register mobile schedule service
        final MobileScheduleService mobileScheduleService = new MobileScheduleService();
        environment.jersey().register(mobileScheduleService);

        // Register mobile table service
        final MobileTableService mobileTableService = new MobileTableService();
        environment.jersey().register(mobileTableService);

        // Register health check
        final MedicineCheck healthCheck = new MedicineCheck();
        environment.healthChecks().register("healthCheck", healthCheck);
        environment.jersey().register(healthCheck);

        // Register security component
        OAuth2Authenticator authenticator = new OAuth2Authenticator(accessTokenDAO, config.getOAuth().isDisabled(),
                config.getOAuth().getAccessTokenExpireTimeMinutes());
        environment.jersey().register(
                AuthFactory.binder(new OAuthFactory<User>(authenticator, "oauth2-provider", User.class)));
    }
}