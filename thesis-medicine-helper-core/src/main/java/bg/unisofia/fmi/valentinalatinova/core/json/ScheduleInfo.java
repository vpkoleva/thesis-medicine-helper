package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.utils.JsonDateTimeUtils;

public class ScheduleInfo extends BaseJson {
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

    public ScheduleInfo() {
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

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 17 * result + (title != null ? title.hashCode() : 0);
        result = 17 * result + (start != null ? start.hashCode() : 0);
        result = 17 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
