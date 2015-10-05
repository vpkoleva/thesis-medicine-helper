package bg.unisofia.fmi.valentinalatinova.core.json;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class MobileTableValue extends BaseJson {
    @JsonProperty
    private String measurement;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime measurementDate;
    @JsonProperty
    private long tableId;
    @JsonIgnore
    private long userId;

    public MobileTableValue() {
        // Needed by Jackson deserialization
    }

    public MobileTableValue(long id, String measurement, DateTime measurementDate, long tableId) {
        this.id = id;
        this.measurement = measurement;
        this.measurementDate = measurementDate;
        this.tableId = tableId;
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

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
