package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;

public class MobileResultsValue extends BaseJson {
    @JsonProperty
    private String measurement;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime measurementDate;
    @JsonProperty
    private Long resultsId;

    public MobileResultsValue() {
        // Needed by Jackson deserialization
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
}
