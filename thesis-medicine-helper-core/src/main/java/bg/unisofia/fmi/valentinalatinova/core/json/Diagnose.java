package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Diagnose extends BaseJson {
    @JsonProperty
    private String diagnoseName;
    @JsonProperty
    private long doctorId;

    public Diagnose() {
    }

    public Diagnose(long id, String diagnoseName, long doctorId) {
        this.id = id;
        this.diagnoseName = diagnoseName;
        this.doctorId = doctorId;
    }

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public void setDiagnoseName(String diagnoseName) {
        this.diagnoseName = diagnoseName;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }
}
