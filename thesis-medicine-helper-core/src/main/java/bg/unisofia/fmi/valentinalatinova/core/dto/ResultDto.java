package bg.unisofia.fmi.valentinalatinova.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDto extends BaseDto {
    @JsonProperty
    private boolean result;
    @JsonProperty
    private String error;

    public ResultDto(long id, boolean result, String error) {
        this.id = id;
        this.result = result;
        this.error = error;
    }

    public static ResultDto createSuccess(long id) {
        ResultDto result = new ResultDto(id, true, null);
        return result;
    }

    public static ResultDto createError(String error) {
        ResultDto result = new ResultDto(-1, false, error);
        return result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
