package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MobileResults extends BaseJson {
    @JsonProperty
    private String name;
    @JsonProperty
    private String units;
    @JsonProperty
    private List<MobileResultsValue> values = new ArrayList<>();
    @JsonIgnore
    private Long userId;

    public MobileResults() {
        // Needed by Jackson deserialization
    }

    public MobileResults(long id, String name, String units, List<MobileResultsValue> values, long userId) {
        this.id = id;
        this.name = name;
        this.units = units;
        this.values = values;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<MobileResultsValue> getValues() {
        return values;
    }

    public void setValues(List<MobileResultsValue> values) {
        this.values = values;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
