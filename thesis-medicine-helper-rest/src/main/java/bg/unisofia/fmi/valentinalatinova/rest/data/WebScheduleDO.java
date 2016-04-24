package bg.unisofia.fmi.valentinalatinova.rest.data;

import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleBO;
import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleListBO;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WebScheduleDO extends DataBaseObject {
    private long id;
    @JsonProperty
    private Timestamp startDate;
    @JsonProperty
    private Timestamp endDate;
    @JsonProperty
    private int frequencyValue;
    @JsonProperty
    private int frequencyType;
    @JsonProperty
    private long patientId;
    @JsonProperty
    private long doctorId;
    @JsonProperty
    private long diagnoseId;
    @JsonProperty
    private String description;

    public WebScheduleDO() {
    }

    public WebScheduleDO(long id, Timestamp startDate, Timestamp endDate, String description, long doctorId, int frequency, int frequencyType, long diagnoseId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.doctorId = doctorId;
        this.frequencyValue = frequency;
        this.diagnoseId = diagnoseId;
        this.frequencyType=frequencyType;
    }

    @Override
    public void load(ResultSet resultSet) {
        try {
            id = resultSet.getInt("ID");
            startDate = resultSet.getTimestamp("startDate");
            endDate = resultSet.getTimestamp("endDate");
            description = resultSet.getString("Description");
            frequencyValue = resultSet.getInt("frequencyValue");
            frequencyType = resultSet.getInt("frequencyTypes");
            doctorId = resultSet.getLong("Doctor_ID");
            doctorId = resultSet.getLong("Diagnoses_ID");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(int frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public int getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(int frequencyType) {
        this.frequencyType = frequencyType;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
