package bg.unisofia.fmi.valentinalatinova.core.json;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class MobileResultsValue extends BaseJson {
    @JsonProperty
    private String measurement;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime measurementDate;
    @JsonProperty
    private Long resultsId;
    @JsonIgnore
    private Long userId;

    public MobileResultsValue() {
        // Needed by Jackson deserialization
    }

    public MobileResultsValue(long id, String measurement, DateTime measurementDate, long resultsId) {
        this.id = id;
        this.measurement = measurement;
        this.measurementDate = measurementDate;
        this.resultsId = resultsId;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public DateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(DateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Long getResultsId() {
        return resultsId;
    }

    public void setResultsId(Long resultsId) {
        this.resultsId = resultsId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
