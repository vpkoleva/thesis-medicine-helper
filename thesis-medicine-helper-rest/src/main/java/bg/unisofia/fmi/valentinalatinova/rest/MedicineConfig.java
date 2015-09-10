package bg.unisofia.fmi.valentinalatinova.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class MedicineConfig extends Configuration {

    @NotNull
    @JsonProperty
    private OAuth authentication = new OAuth();

    public OAuth getOAuth() {
        return authentication;
    }

    public static class OAuth {
        @NotEmpty
        @JsonProperty
        private ImmutableList<String> allowedGrantTypes;

        @JsonProperty
        private int accessTokenExpireTimeMinutes;

        @JsonProperty
        private boolean disabled;

        public ImmutableList<String> getAllowedGrantTypes() {
            return allowedGrantTypes;
        }

        public int getAccessTokenExpireTimeMinutes() {
            return accessTokenExpireTimeMinutes;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }
}
