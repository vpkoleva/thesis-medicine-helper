package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

import java.sql.PreparedStatement;
import java.util.List;

public class WebScheduleDAO {

    private DataBaseCommander dataBaseCommander;

    public WebScheduleDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public Result addScheduleByUserId(final WebScheduleDO scheduleToAdd, final long userId) {
        String sql = "INSERT INTO `schedulesunited` (startDate, endDate, " +
                "Description, frequencyValue, frequencyTypes, " +
                "Patients_ID, Doctors_ID, Diagnoses_ID, " +
                "startAfterType, startAfterValue, endAfterType, endAfterValue) " +
                "VALUES(?,?,?,?,?,?,(Select `Doctor_ID` from `Users` where `ID`=?),?,?,?,?,?);";
        final long result = dataBaseCommander.insert(sql, scheduleToAdd.getStartDate(), scheduleToAdd.getEndDate(),
                scheduleToAdd.getDescription(), scheduleToAdd.getFrequencyValue(), scheduleToAdd.getFrequencyType().getValue(),
                scheduleToAdd.getPatientId() == 0 ? null : scheduleToAdd.getPatientId(), userId, scheduleToAdd.getDiagnoseId(),
                scheduleToAdd.getStartAfterType().getValue(), scheduleToAdd.getStartAfterValue(), scheduleToAdd.getEndAfterType().getValue(), scheduleToAdd.getEndAfterValue());
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create schedule");
        }
    }

    public List<WebScheduleDO> getScheduleByDiagnoseIdWithLimits(final int diagnoseId, final String start, final String end, final long userId) {
        String sql = "SELECT * FROM `schedulesunited` Where `Diagnoses_ID`=? AND `Doctors_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, diagnoseId, userId);
    }

    public List<WebScheduleDO> getScheduleByPatientIdWithLimits(final int patientID, final String start, final String end, final long userId) {
        String sql = "SELECT * FROM `schedulesunited` Where `Patients_ID`=? AND `Doctors_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, patientID, userId);
    }

    public List<WebScheduleDO> getScheduleByDiagnoseId(final int diagnoseId, final long userId) {
        String sql = "SELECT * FROM `schedulesunited` Where `Diagnoses_ID`=? AND `Doctors_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, diagnoseId, userId);
    }

    public Result addScheduleToPatientFromDefaultDiagnose(final String startDate, final String patientId, final long userId) {
        String sql = "insert into schedulesunited (startDate, endDate, Description, frequencyValue, frequencyTypes,Patients_ID, Doctors_ID, Diagnoses_ID, startAfterValue, startAfterType, endAfterValue, endAfterType) select ?, date_add(?, interval datediff(endDate, startDate) day), Description, frequencyValue, frequencyTypes, ?, Doctors_ID, null, startAfterValue, startAfterType, endAfterValue, endAfterType from schedulesunited where Diagnoses_ID=(select Diagnose_ID from patients where ID=?)";
        final long result = dataBaseCommander.insert(sql, startDate, startDate, patientId, patientId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create schedule");
        }
    }

    public List<WebScheduleDO> getWebScheduleByID(final long scheduleId) {
        String sql = "SELECT * FROM `schedulesunited` Where `ID`=?";
        return dataBaseCommander.select(WebScheduleDO.class, sql, scheduleId);
    }

    public Result deleteScheduleById(final long scheduleId, final long userId) {
        String sql = "DELETE FROM `schedulesunited` Where `ID`=?";
        final int result = dataBaseCommander.delete(sql, scheduleId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create schedule");
        }
    }

    public Result updateScheduleByUserId(final WebScheduleDO scheduleToUpdate, final long userId) {
        String sql = "UPDATE `schedulesunited` SET startDate =?,endDate =?,Description =?," +
                "frequencyValue =?,frequencyTypes =?," +
                "Patients_ID=?, Doctors_ID=(Select `Doctor_ID` from `Users` where `ID`=?), " +
                "Diagnoses_ID=?, startAfterType=?, startAfterValue=?, " +
                "endAfterType=?, endAfterValue=? WHERE `ID`=?";
        PreparedStatement preparedStatement = dataBaseCommander.createPreparedStatement(sql,
                scheduleToUpdate.getStartDate(), scheduleToUpdate.getEndDate(), scheduleToUpdate.getDescription(),
                scheduleToUpdate.getFrequencyValue(), scheduleToUpdate.getFrequencyType().getValue(),
                scheduleToUpdate.getPatientId() == 0 ? null : scheduleToUpdate.getPatientId(), userId,
                scheduleToUpdate.getDiagnoseId(), scheduleToUpdate.getStartAfterType().getValue(), scheduleToUpdate.getStartAfterValue(),
                scheduleToUpdate.getEndAfterType().getValue(), scheduleToUpdate.getEndAfterValue(),
                scheduleToUpdate.getId());
        final boolean result = dataBaseCommander.execute(preparedStatement);
        if (result) {
            return Result.createSuccess(scheduleToUpdate.getId());
        } else {
            return Result.createError("Cannot update schedule");
        }
    }
}
