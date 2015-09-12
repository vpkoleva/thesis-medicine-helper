package bg.unisofia.fmi.valentinalatinova.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MobileTableDto extends BaseDto {
    @JsonProperty
    private String name;
    @JsonProperty
    private List<MobileTableValueDto> values;
    @JsonIgnore
    private long usedId;

    public MobileTableDto(long id, String name, List<MobileTableValueDto> values, long usedId) {
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

    public List<MobileTableValueDto> getValues() {
        return values;
    }

    public void setValues(List<MobileTableValueDto> values) {
        this.values = values;
    }

    public long getUsedId() {
        return usedId;
    }

    public void setUsedId(long usedId) {
        this.usedId = usedId;
    }
}
