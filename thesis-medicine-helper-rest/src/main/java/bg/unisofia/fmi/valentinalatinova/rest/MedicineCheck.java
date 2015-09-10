package bg.unisofia.fmi.valentinalatinova.rest;

import com.codahale.metrics.health.HealthCheck;

public class MedicineCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy("OK");
    }
}
