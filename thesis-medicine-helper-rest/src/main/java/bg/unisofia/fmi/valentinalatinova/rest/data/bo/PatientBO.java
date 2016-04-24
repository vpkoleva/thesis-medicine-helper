package bg.unisofia.fmi.valentinalatinova.rest.data.bo;

import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientBO extends DataBaseObject {
    @JsonProperty
    private long id;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private long doctorId;



    @JsonProperty
    private long diagnoseId;
    @JsonProperty
    private String diagnoseName;

    public PatientBO() {
    }

    public PatientBO(long id, String diagnoseName, long doctorId, String lastName, String firstName, long diagnoseId) {
        this.id = id;
        this.diagnoseName = diagnoseName;
        this.doctorId = doctorId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.diagnoseId = diagnoseId;
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getInt("ID");
            diagnoseName = resultSet.getString("Diagnose");
            firstName = resultSet.getString("firstName");
            lastName = resultSet.getString("lastName");
            doctorId = resultSet.getLong("Doctor_ID");

        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
