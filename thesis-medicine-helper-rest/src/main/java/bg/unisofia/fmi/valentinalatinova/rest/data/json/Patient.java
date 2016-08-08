package bg.unisofia.fmi.valentinalatinova.rest.data.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseObject;

public class Patient extends DataBaseObject {
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private Long doctorId;
    @JsonProperty
    private String diagnoseName;
    @JsonProperty
    private Long diagnoseId;
    @JsonProperty
    private Long mobileUserId;

    public Patient() {
        // Needed by Jackson deserialization
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getLong("ID");
            diagnoseName = resultSet.getString("diagnose");
            firstName = resultSet.getString("firstName");
            lastName = resultSet.getString("lastName");
            diagnoseId = resultSet.getLong("diagnose_ID");
            doctorId = resultSet.getLong("doctor_ID");
            mobileUserId = resultSet.getLong("user_ID");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
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

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }

    public Long getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(Long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public Long getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(Long mobileUserId) {
        this.mobileUserId = mobileUserId;
    }
}
