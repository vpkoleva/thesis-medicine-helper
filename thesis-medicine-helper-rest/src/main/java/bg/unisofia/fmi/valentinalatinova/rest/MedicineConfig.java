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

    @NotNull
    @JsonProperty
    private Database database = new Database();

    public OAuth getOAuth() {
        return authentication;
    }

    public Database getDatabase() {
        return database;
    }

    public static class OAuth {
        @NotEmpty
        @JsonProperty
        private ImmutableList<String> allowedGrantTypes;

        @NotEmpty
        @JsonProperty
        private int accessTokenExpireTimeMinutes;

        public ImmutableList<String> getAllowedGrantTypes() {
            return allowedGrantTypes;
        }

        public int getAccessTokenExpireTimeMinutes() {
            return accessTokenExpireTimeMinutes;
        }
    }

    public static class Database {
        @NotEmpty
        @JsonProperty
        private String host;

        @NotEmpty
        @JsonProperty
        private String port;

        @NotEmpty
        @JsonProperty
        private String database;

        @NotEmpty
        @JsonProperty
        private String username;

        @NotEmpty
        @JsonProperty
        private String password;

        public String getHost() {
            return host;
        }

        public String getPort() {
            return port;
        }

        public String getDatabase() {
            return database;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}