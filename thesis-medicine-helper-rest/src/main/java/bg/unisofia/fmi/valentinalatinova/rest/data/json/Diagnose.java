package bg.unisofia.fmi.valentinalatinova.rest.data.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseObject;

public class Diagnose extends DataBaseObject {
    @JsonProperty
    private String diagnoseName;

    public Diagnose() {
        // Needed by Jackson deserialization
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getLong("ID");
            diagnoseName = resultSet.getString("diagnose");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }
}
