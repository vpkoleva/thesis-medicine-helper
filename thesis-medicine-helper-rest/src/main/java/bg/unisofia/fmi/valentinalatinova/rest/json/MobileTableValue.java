package bg.unisofia.fmi.valentinalatinova.rest.json;

import bg.unisofia.fmi.valentinalatinova.rest.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class MobileTableValue {
    @JsonProperty
    private long id;
    @JsonProperty
    private String measurement;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime measurementDate;
    @JsonProperty
    private long fieldId;

    public MobileTableValue(long id, String measurement, DateTime measurementDate, long fieldId) {
        this.id = id;
        this.measurement = measurement;
        this.measurementDate = measurementDate;
        this.fieldId = fieldId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getFieldId() {
        return fieldId;
    }

    public void setFieldId(long fieldId) {
        this.fieldId = fieldId;
    }
}
