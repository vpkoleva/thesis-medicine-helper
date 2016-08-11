package bg.unisofia.fmi.valentinalatinova.core.json;

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

    public MobileResults() {
        // Needed by Jackson deserialization
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
}
