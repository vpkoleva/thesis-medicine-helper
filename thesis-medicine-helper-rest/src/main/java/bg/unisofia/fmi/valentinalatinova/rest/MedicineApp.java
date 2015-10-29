package bg.unisofia.fmi.valentinalatinova.rest;

import bg.unisofia.fmi.valentinalatinova.rest.auth.OAuth2Authenticator;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.AccessTokenDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.UserDao;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.AccessTokenDaoImpl;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.impl.UserDaoImpl;
import bg.unisofia.fmi.valentinalatinova.rest.resources.CorsResponseFilter;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileResultsService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileResultsValueService;
import bg.unisofia.fmi.valentinalatinova.rest.resources.MobileScheduleService;
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
        final boolean isAuthDisabled = config.getOAuth().isDisabled();
        final int tokenExpiryMinutes = config.getOAuth().getAccessTokenExpireTimeMinutes();

        // Create DAOs
        AccessTokenDao accessTokenDao = new AccessTokenDaoImpl();
        UserDao userDao = new UserDaoImpl();

        // Register CORS Response filter
        CorsResponseFilter filter = new CorsResponseFilter();
        environment.jersey().register(filter);

        // Register oauth2 service
        final OAuth2Service oAuth2Service = new OAuth2Service(config.getOAuth().getAllowedGrantTypes(), accessTokenDao,
                userDao, isAuthDisabled, tokenExpiryMinutes);
        environment.jersey().register(oAuth2Service);

        // Register mobile schedule service
        final MobileScheduleService mobileScheduleService = new MobileScheduleService();
        environment.jersey().register(mobileScheduleService);

        // Register mobile results service
        final MobileResultsService mobileResultsService = new MobileResultsService();
        environment.jersey().register(mobileResultsService);

        // Register mobile results value service
        final MobileResultsValueService mobileResultsValueService = new MobileResultsValueService();
        environment.jersey().register(mobileResultsValueService);

        // Register health check
        final MedicineCheck healthCheck = new MedicineCheck();
        environment.healthChecks().register("healthCheck", healthCheck);
        environment.jersey().register(healthCheck);

        // Register security component
        OAuth2Authenticator authenticator = new OAuth2Authenticator(accessTokenDao, isAuthDisabled, tokenExpiryMinutes);
        environment.jersey().register(
                AuthFactory.binder(new OAuthFactory<>(authenticator, "oauth2-provider", User.class)));
    }
}