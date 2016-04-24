package bg.unisofia.fmi.valentinalatinova.core.json;

import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class WebScheduleBO extends BaseJson {
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime startDate;
    @JsonProperty
    private String description;
    @JsonProperty
    private int startAfter;
    @JsonProperty
    private Duration startAfterType;
    @JsonProperty
    private int duration;
    @JsonProperty
    private Duration durationType;
    @JsonProperty
    private int frequency;
    @JsonProperty
    private Duration frequencyType;
    @JsonProperty
    private long patientId;
    @JsonProperty
    private long doctorId;
    @JsonProperty
    private long diagnoseId;

    public WebScheduleBO() {
        // Needed by Jackson deserialization
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(int startAfter) {
        this.startAfter = startAfter;
    }

    public Duration getStartAfterType() {
        return startAfterType;
    }

    public void setStartAfterType(Duration startAfterType) {
        this.startAfterType = startAfterType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Duration getDurationType() {
        return durationType;
    }

    public void setDurationType(Duration durationType) {
        this.durationType = durationType;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getDiagnoseId() {
        return diagnoseId;
    }

    public void setDiagnoseId(long diagnoseId) {
        this.diagnoseId = diagnoseId;
    }
}
