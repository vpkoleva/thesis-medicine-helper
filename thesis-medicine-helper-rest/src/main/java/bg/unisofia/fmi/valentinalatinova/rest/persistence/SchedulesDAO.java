package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import java.sql.PreparedStatement;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.rest.data.User;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;

public class SchedulesDAO {

    private DataBaseCommander dataBaseCommander;

    public SchedulesDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<WebScheduleDO> findByDiagnoseId(final int diagnoseId, final long userId) {
        String sql = "SELECT * FROM `schedules` WHERE `diagnoses_ID`=? "
                + "AND `patients_ID` IS NULL "
                + "AND `doctors_ID`=(SELECT `doctor_ID` from `users` WHERE `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, diagnoseId, userId);
    }

    public List<WebScheduleDO> findByPatientId(final int patientID, final long userId) {
        String sql = "SELECT * FROM `schedules` WHERE `patients_ID`=? "
                + "AND `doctors_ID`=(SELECT `doctor_ID` from `users` WHERE `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, patientID, userId);
    }

    public List<WebScheduleDO> findByMobileUserId(final long userId) {
        String sql = "SELECT * FROM `schedules` "
                + "WHERE `mobileusers_ID`=(SELECT `mobileuser_ID` from `users` WHERE `ID`=?) "
                + "UNION SELECT * FROM `schedules` "
                + "WHERE `patients_ID`=(SELECT `patient_ID` from `users` WHERE `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, userId, userId);
    }

    public Schedule getById(final long scheduleId, final User user) {
        String sql = "SELECT * FROM `schedules` WHERE `ID`=? AND " + getUserOrDoctor(user);
        List<WebScheduleDO> result = dataBaseCommander.select(WebScheduleDO.class, sql, scheduleId, user.getId());
        if (result != null && result.size() == 1) {
            return result.get(0).getSchedule();
        } else {
            return null;
        }
    }

    public Result save(final WebScheduleDO webSchedule, final long userId) {
        String sql = "INSERT INTO `schedules` (startDate, endDate, description, frequencyValue, frequencyTypes, " +
                "patients_ID, doctors_ID, mobileusers_ID, diagnoses_ID, startAfterType, startAfterValue, "
                + "endAfterType, endAfterValue) "
                + "VALUES(?,?,?,?,?,?,(SELECT `doctor_ID` FROM `users` WHERE `ID`=?),"
                + "(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?),?,?,?,?,?);";
        Schedule schedule = webSchedule.getSchedule();
        final long result = dataBaseCommander.insert(sql, webSchedule.getStartDate(),
                webSchedule.getEndDate(), schedule.getDescription(), schedule.getFrequency(),
                schedule.getFrequencyType().getValue(), schedule.getPatientId(), userId, userId,
                schedule.getDiagnoseId(), schedule.getStartAfterType().getValue(), schedule.getStartAfter(),
                schedule.getDurationType().getValue(), schedule.getDuration());
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create schedule");
        }
    }

    public Result addScheduleToPatientFromDefaultDiagnose(final String startDate, final String patientId) {
        String sql = "INSERT INTO `schedules` (startDate, endDate, description, frequencyValue, frequencyTypes,"
                + "patients_ID, doctors_ID, diagnoses_ID, startAfterValue, startAfterType, endAfterValue, "
                + "endAfterType) "
                + "SELECT ?, date_add(?, interval datediff(endDate, startDate) day), description, "
                + "frequencyValue, frequencyTypes, ?, doctors_ID, null, startAfterValue, startAfterType, "
                + "endAfterValue, endAfterType "
                + "FROM `schedules` WHERE diagnoses_ID=(SELECT diagnose_ID FROM `patients` WHERE ID=?)";
        DateTime dateTime = new DateTime(startDate);
        String newStartDate = DateTimeFormat.forPattern("yyyy-MM-dd").print(dateTime);
        final long result = dataBaseCommander.insert(sql, newStartDate, newStartDate, patientId, patientId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create schedule");
        }
    }

    public Result update(final WebScheduleDO webSchedule, final User user) {
        String sql = "UPDATE `schedules` SET startDate=?, endDate=?, description=?, "
                + "frequencyValue=?, frequencyTypes=?, patients_ID=?, "
                + "diagnoses_ID=?, startAfterType=?, startAfterValue=?, endAfterType=?, endAfterValue=? "
                + "WHERE `ID`=? AND " + getUserOrDoctor(user);
        Schedule schedule = webSchedule.getSchedule();
        PreparedStatement preparedStatement = dataBaseCommander.createPreparedStatement(sql,
                webSchedule.getStartDate(), webSchedule.getEndDate(),
                schedule.getDescription(), schedule.getFrequency(), schedule.getFrequencyType().getValue(),
                schedule.getPatientId(), schedule.getDiagnoseId(),
                schedule.getStartAfterType().getValue(), schedule.getStartAfter(),
                schedule.getDurationType().getValue(), schedule.getDuration(),
                schedule.getId(), user.getId());
        final boolean result = dataBaseCommander.execute(preparedStatement);
        if (result) {
            return Result.createSuccess(webSchedule.getId());
        } else {
            return Result.createError("Cannot update schedule");
        }
    }

    public Result delete(final long scheduleId, final User user) {
        String sql = "DELETE FROM `schedules` WHERE `ID`=? AND " + getUserOrDoctor(user);
        final boolean result = dataBaseCommander.deleteOrUpdate(sql, scheduleId, user.getId());
        if (result) {
            return Result.createSuccess(-1);
        } else {
            return Result.createError("Cannot delete schedule");
        }
    }

    private String getUserOrDoctor(User user) {
        return user.getIsDoctor() ? "`doctors_ID`=(SELECT `doctor_ID` from `users` WHERE `ID`=?)"
                : "`mobileusers_ID`=(SELECT `mobileuser_ID` from `users` WHERE `ID`=?)";
    }
}
