package bg.unisofia.fmi.valentinalatinova.rest.data.bo;

import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiagnoseBO extends DataBaseObject {
    @JsonProperty
    private long id;
    @JsonProperty
    private String diagnoseName;

    public DiagnoseBO() {
    }

    public DiagnoseBO(long id, String diagnoseName) {
        this.id = id;
        this.diagnoseName = diagnoseName;

    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getInt("ID");
            diagnoseName = resultSet.getString("Diagnose");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public long getId() {
        return id;
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }

}
