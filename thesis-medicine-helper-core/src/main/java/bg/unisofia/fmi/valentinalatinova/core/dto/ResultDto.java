package bg.unisofia.fmi.valentinalatinova.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDto extends BaseDto {
    @JsonProperty
    private boolean success;
    @JsonProperty
    private String error;

    public ResultDto() {
        // Needed by Jackson deserialization
    }

    public ResultDto(long id, boolean result, String error) {
        this.id = id;
        this.success = result;
        this.error = error;
    }

    public static ResultDto createSuccess(long id) {
        return new ResultDto(id, true, null);
    }

    public static ResultDto createError(String error) {
        return new ResultDto(-1, false, error);
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
