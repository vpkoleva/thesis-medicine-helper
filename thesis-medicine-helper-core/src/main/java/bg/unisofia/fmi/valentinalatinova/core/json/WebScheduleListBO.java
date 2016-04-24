package bg.unisofia.fmi.valentinalatinova.core.json;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

public class WebScheduleListBO extends BaseJson {
    @JsonProperty
    private String title;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime start;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeUtils.DateTimeDeserializer.class)
    private DateTime end;

    public WebScheduleListBO() {
        // Needed by Jackson deserialization
    }

    public String getDescription() {
        return title;
    }

    public void setDescription(String description) {
        this.title = description;
    }

    public DateTime getStartDate() {
        return start;
    }

    public void setStartDate(DateTime startDate) {
        this.start = startDate;
    }

    public DateTime getEndDate() {
        return end;
    }

    public void setEndDate(DateTime endDate) {
        this.end = endDate;
    }
}
