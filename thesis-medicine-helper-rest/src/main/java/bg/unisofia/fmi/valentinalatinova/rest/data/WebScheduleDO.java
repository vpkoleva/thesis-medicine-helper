package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;

public class WebScheduleDO extends DataBaseObject {
    private Schedule schedule;
    private Timestamp startDate;
    private Timestamp endDate;

    @Override
    public void load(ResultSet resultSet) {
        try {
            schedule = new Schedule();
            schedule.setId(resultSet.getLong("ID"));
            schedule.setDescription(resultSet.getString("Description"));
            schedule.setStartDate(new DateTime(resultSet.getTimestamp("startDate")).withZone(DateTimeZone.UTC));
            schedule.setStartAfter(resultSet.getInt("startAfterValue"));
            schedule.setStartAfterType(Duration.fromValue(resultSet.getInt("startAfterType")));
            schedule.setDuration(resultSet.getInt("endAfterValue"));
            schedule.setDurationType(Duration.fromValue(resultSet.getInt("endAfterType")));
            schedule.setFrequency(resultSet.getInt("frequencyValue"));
            schedule.setFrequencyType(Duration.fromValue(resultSet.getInt("frequencyTypes")));
            schedule.setPatientId(resultSet.getLong("patients_ID"));
            schedule.setDoctorId(resultSet.getLong("doctors_ID"));
            schedule.setMobileUserId(resultSet.getLong("mobileusers_ID"));
            schedule.setDiagnoseId(resultSet.getLong("diagnoses_ID"));
            id = resultSet.getLong("ID");
            startDate = resultSet.getTimestamp("startDate");
            endDate = resultSet.getTimestamp("endDate");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
