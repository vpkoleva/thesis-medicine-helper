package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;

public class ScheduleList extends BaseJson {
    @JsonProperty
    private String title;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateSerializer.class)
    private DateTime start;
    @JsonProperty
    @JsonSerialize(using = JsonDateTimeUtils.DateSerializer.class)
    private DateTime end;

    public ScheduleList() {
        // Needed by Jackson deserialization
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }
}
