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
    private Duration frequencyType;
    @JsonProperty
    private long patientId;
    @JsonProperty
    private long doctorId;
    @JsonProperty
    private long diagnoseId;
    @JsonProperty
    private String description;
    @JsonProperty
    private Duration startAfterType;
    @JsonProperty
    private int startAfterValue;
    @JsonProperty
    private Duration endAfterType;
    @JsonProperty
    private int endAfterValue;
    public WebScheduleDO() {
    }

    public WebScheduleDO(long id, Timestamp startDate, Timestamp endDate, String description, long doctorId, int frequency, Duration frequencyType, long diagnoseId) {
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
            frequencyType = Duration.fromValue(resultSet.getInt("frequencyTypes"));
            startAfterValue = resultSet.getInt("startAfterValue");
            startAfterType = Duration.fromValue(resultSet.getInt("startAfterType"));
            endAfterValue = resultSet.getInt("endAfterValue");
            endAfterType = Duration.fromValue(resultSet.getInt("endAfterType"));
            doctorId = resultSet.getLong("Doctors_ID");
            diagnoseId = resultSet.getLong("Diagnoses_ID");
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

    public Duration getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(Duration frequencyType) {
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

    public Duration getStartAfterType() {
        return startAfterType;
    }

    public void setStartAfterType(Duration startAfterType) {
        this.startAfterType = startAfterType;
    }

    public int getStartAfterValue() {
        return startAfterValue;
    }

    public void setStartAfterValue(int startAfterValue) {
        this.startAfterValue = startAfterValue;
    }

    public Duration getEndAfterType() {
        return endAfterType;
    }

    public void setEndAfterType(Duration endAfterType) {
        this.endAfterType = endAfterType;
    }

    public int getEndAfterValue() {
        return endAfterValue;
    }

    public void setEndAfterValue(int endAfterValue) {
        this.endAfterValue = endAfterValue;
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
