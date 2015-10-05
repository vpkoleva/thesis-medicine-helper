package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MobileTable extends BaseJson {
    @JsonProperty
    private String name;
    @JsonProperty
    private List<MobileTableValue> values;
    @JsonIgnore
    private long usedId;

    public MobileTable() {
        // Needed by Jackson deserialization
    }

    public MobileTable(long id, String name, List<MobileTableValue> values, long usedId) {
        this.id = id;
        this.name = name;
        this.values = values;
        this.usedId = usedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MobileTableValue> getValues() {
        return values;
    }

    public void setValues(List<MobileTableValue> values) {
        this.values = values;
    }

    public long getUsedId() {
        return usedId;
    }

    public void setUsedId(long usedId) {
        this.usedId = usedId;
    }
}
