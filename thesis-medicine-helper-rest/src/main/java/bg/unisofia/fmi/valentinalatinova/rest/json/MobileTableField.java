package bg.unisofia.fmi.valentinalatinova.rest.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MobileTableField {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private int order;
    @JsonIgnore
    private long usedId;

    public MobileTableField(long id, String name, int order, long usedId) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.usedId = usedId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getUsedId() {
        return usedId;
    }

    public void setUsedId(long usedId) {
        this.usedId = usedId;
    }
}
