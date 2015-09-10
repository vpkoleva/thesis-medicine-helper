package bg.unisofia.fmi.valentinalatinova.rest.json;

import bg.unisofia.fmi.valentinalatinova.rest.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.rest.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class MobileSchedule {
    @JsonProperty
    private long id;
    @JsonProperty
    private String description;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime startDate;
    @JsonProperty
    private int duration;
    @JsonProperty
    private Duration durationType;
    @JsonProperty
    private int frequency;
    @JsonProperty
    private Duration frequencyType;
    @JsonIgnore
    private long userId;

    public MobileSchedule() {
        // Needed by Jackson deserialization
    }

    public MobileSchedule(long id, String description, DateTime startDate, int duration,
            Duration durationType, int frequency, Duration frequencyType, long userId) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.durationType = durationType;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MobileSchedule that = (MobileSchedule) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
