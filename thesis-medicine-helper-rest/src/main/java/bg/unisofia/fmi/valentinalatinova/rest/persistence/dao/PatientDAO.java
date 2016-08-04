package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;

import java.sql.PreparedStatement;
import java.util.List;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.PatientBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

public class PatientDAO {

    private DataBaseCommander dataBaseCommander;

    public PatientDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<PatientBO> getPatientsByDoctorId(final long doctorId) {
        String sql
                = "SELECT * FROM `Patients` INNER JOIN `Diagnoses` ON `Patients`.`Diagnose_ID` = `Diagnoses`.`ID` "
                + "WHERE `Patients`.`Doctor_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        List<PatientBO> patients = dataBaseCommander.select(PatientBO.class, sql, doctorId);
        return patients;
    }

    public Result addPatient(final long userId, final String fName, final String lName, final long diagnoseID) {
        String sql
                = "INSERT INTO `patients` (firstName, lastName, Diagnose_ID, Doctor_ID) VALUES(?,?,?,(Select "
                + "`Doctor_ID` from `Users` where `ID`=?));";
        final long result = dataBaseCommander.insert(sql, fName, lName, diagnoseID, userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create diagnose");
        }
    }

    public Result linkPatientToMobileUser(final long userId, final long patientID, final String code) {
        String sql = "SELECT ID from `mobileusers` where code=?;";
        final Long mUserId = dataBaseCommander.select(sql, code);
        if (mUserId > 0) {
            String sqlForUpdate = "UPDATE `users` SET patient_ID=? WHERE mobileusers_ID=?;";
            PreparedStatement pr = dataBaseCommander.createPreparedStatement(sqlForUpdate, patientID, mUserId);
            final Boolean result = dataBaseCommander.execute(pr);
            if (result) {
                return Result.createSuccess(1);
            } else {
                return Result.createError("Cannot create link");
            }

        } else {
            return Result.createError("Cannot create link");
        }
    }
}
