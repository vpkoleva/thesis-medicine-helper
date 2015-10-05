package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result extends BaseJson {
    @JsonProperty
    private boolean success;
    @JsonProperty
    private String error;

    public Result() {
        // Needed by Jackson deserialization
    }

    public Result(long id, boolean result, String error) {
        this.id = id;
        this.success = result;
        this.error = error;
    }

    public static Result createSuccess(long id) {
        return new Result(id, true, null);
    }

    public static Result createError(String error) {
        return new Result(-1, false, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
