package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MobileResults extends BaseJson {
    @JsonProperty
    private String name;
    @JsonProperty
    private String unit;
    @JsonProperty
    private List<MobileResultsValue> values;
    @JsonIgnore
    private long userId;

    public MobileResults() {
        // Needed by Jackson deserialization
    }

    public MobileResults(long id, String name, String unit, List<MobileResultsValue> values, long userId) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.values = values;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<MobileResultsValue> getValues() {
        return values;
    }

    public void setValues(List<MobileResultsValue> values) {
        this.values = values;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
