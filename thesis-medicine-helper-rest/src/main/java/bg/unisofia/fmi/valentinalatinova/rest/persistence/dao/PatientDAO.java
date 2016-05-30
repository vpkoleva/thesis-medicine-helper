package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.PatientBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

import java.util.List;

public class PatientDAO {


    private DataBaseCommander dataBaseCommander;

    public PatientDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<PatientBO> getPatientsByDoctorId(final long doctorId) {
        String sql = "SELECT * FROM `Patients` INNER JOIN `Diagnoses` ON `Patients`.`Diagnose_ID` = `Diagnoses`.`ID` WHERE `Patients`.`Doctor_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        List<PatientBO> patients = dataBaseCommander.select(PatientBO.class, sql, doctorId);
        return patients;
    }

    public Result addPatient(final long userId, final String fName, final String lName, final long diagnoseID) {
        String sql = "INSERT INTO `patients` (firstName, lastName, Diagnose_ID, Doctor_ID) VALUES(?,?,?,(Select `Doctor_ID` from `Users` where `ID`=?));";
        final long result = dataBaseCommander.insert(sql, fName, lName, diagnoseID, userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create diagnose");
        }
    }
}
