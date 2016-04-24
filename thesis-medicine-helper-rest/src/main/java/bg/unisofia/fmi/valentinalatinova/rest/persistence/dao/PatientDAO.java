package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.PatientBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Valentina on 1/22/2016.
 */
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

    public long addPatient(final long userId, final String fName, final String lName, final long diagnoseID)
    {
        String sql = "INSERT INTO `patients` (firstName, lastName, Diagnose_ID, Doctor_ID) VALUES(?,?,?,(Select `Doctor_ID` from `Users` where `ID`=?));";
        PreparedStatement pr = dataBaseCommander.createPreparedStatement(sql, fName, lName, diagnoseID, userId);
        return dataBaseCommander.insertWithReturnNewID(pr);
    }


}
