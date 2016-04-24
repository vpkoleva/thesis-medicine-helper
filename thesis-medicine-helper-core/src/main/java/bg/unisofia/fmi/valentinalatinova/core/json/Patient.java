
package bg.unisofia.fmi.valentinalatinova.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient extends BaseJson {

    @JsonProperty
    private String firstName;
    @JsonProperty
    private long id;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private int doctorId;
    @JsonProperty
    private Diagnose diagnose;

    public Patient() {
        // Needed by Jackson deserialization
    }

    public Patient(long id, String firstName, String lastName, int doctorId, Diagnose diagnose) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.doctorId = doctorId;
        this.diagnose = diagnose;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Diagnose getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
    }

}
