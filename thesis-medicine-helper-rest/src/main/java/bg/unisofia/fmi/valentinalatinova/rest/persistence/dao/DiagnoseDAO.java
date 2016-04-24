package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.DiagnoseBO;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.PatientBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Valentina on 1/22/2016.
 */
public class DiagnoseDAO {


    private DataBaseCommander dataBaseCommander;

    public DiagnoseDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<DiagnoseBO> getDiagnosesByUserId(final long doctorId) {
        String sql = "SELECT * FROM `Diagnoses` WHERE `Doctor_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        List<DiagnoseBO> diagnoses = dataBaseCommander.select(DiagnoseBO.class, sql, doctorId);
       return diagnoses;
    }
    public long addDiagnosesByUserId(final String diagnoseName, final long userId) {
        String sql = "INSERT INTO `Diagnoses` (Diagnose,Doctor_ID) VALUES(?,(Select `Doctor_ID` from `Users` where `ID`=?));";
        PreparedStatement pr = dataBaseCommander.createPreparedStatement(sql, diagnoseName, userId);
        return dataBaseCommander.insertWithReturnNewID(pr);
    }

}
