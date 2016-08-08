package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import java.util.List;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseResult;
import bg.unisofia.fmi.valentinalatinova.rest.data.json.Patient;

public class PatientDAO {

    private DataBaseCommander dataBaseCommander;

    public PatientDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<Patient> get(final long userId) {
        String sql = "SELECT * FROM `patients` "
                + "INNER JOIN `diagnoses` ON `patients`.`diagnose_ID` = `diagnoses`.`ID` "
                + "WHERE `patients`.`doctor_ID`=(SELECT `doctor_ID` FROM `users` WHERE `ID`=?)";
        return dataBaseCommander.select(Patient.class, sql, userId);
    }

    public Result save(final Patient patient, final long userId) {
        String sql = "INSERT INTO `patients` (firstName, lastName, diagnose_ID, doctor_ID) "
                + "VALUES(?,?,?,(SELECT `doctor_ID` FROM `users` WHERE `ID`=?));";
        final long result = dataBaseCommander
                .insert(sql, patient.getFirstName(), patient.getLastName(), patient.getDiagnoseId(), userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create patient");
        }
    }

    public Result delete(final long patientId, final long userId) {
        // TODO delete associated records with this patient
        String sql = "DELETE FROM `patients` WHERE id=? "
                + "AND `doctor_ID`=(SELECT `doctor_ID` FROM `users` WHERE `ID`=?)";
        final boolean result = dataBaseCommander.delete(sql, patientId, userId);
        if (result) {
            return Result.createSuccess(-1);
        } else {
            return Result.createError("Cannot delete patient");
        }
    }

    public Result linkPatientToMobileUser(final String patientId, final String code) {
        String sql = "SELECT ID FROM `mobileusers` WHERE code=?";
        List<DataBaseResult> dataBaseResults = dataBaseCommander.select(DataBaseResult.class, sql, code);
        if (dataBaseResults.size() == 1) {
            final long mUserId = Long.parseLong(dataBaseResults.get(0).getValue("ID"));
            String sqlUpdate = "UPDATE `users` SET patient_ID=? WHERE mobileuser_ID=?";
            dataBaseCommander.insert(sqlUpdate, patientId, mUserId);
            return Result.createSuccess(-1);
        } else {
            return Result.createError("Cannot create link. Incorrect code.");
        }
    }
}
