package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;

public class Schedule extends BaseJson {
    @JsonProperty
    private String description;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime startDate;
    @JsonProperty
    private Integer startAfter;
    @JsonProperty
    private Duration startAfterType;
    @JsonProperty
    private Integer frequency;
    @JsonProperty
    private Duration frequencyType;
    @JsonProperty
    private Integer duration;
    @JsonProperty
    private Duration durationType;
    private Long patientId;
    private Long doctorId;
    private Long mobileUserId;
    private Long diagnoseId;

    public Schedule() {
        // Needed by Jackson deserialization
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(Integer startAfter) {
        this.startAfter = startAfter;
    }

    public Duration getStartAfterType() {
        return startAfterType;
    }

    public void setStartAfterType(Duration startAfterType) {
        this.startAfterType = startAfterType;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Duration getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(Duration frequencyType) {
        this.frequencyType = frequencyType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Duration getDurationType() {
        return durationType;
    }

    public void setDurationType(Duration durationType) {
        this.durationType = durationType;
    }

    @JsonIgnore
    public Long getPatientId() {
        return patientId;
    }

    @JsonProperty
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @JsonIgnore
    public Long getDoctorId() {
        return doctorId;
    }

    @JsonProperty
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @JsonIgnore
    public Long getMobileUserId() {
        return mobileUserId;
    }

    @JsonProperty
    public void setMobileUserId(Long mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    @JsonIgnore
    public Long getDiagnoseId() {
        return diagnoseId;
    }

    @JsonProperty
    public void setDiagnoseId(Long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }
}
