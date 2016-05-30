package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.bo.DiagnoseBO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

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

    public Result addDiagnosesByUserId(final String diagnoseName, final long userId) {
        String sql = "INSERT INTO `Diagnoses` (Diagnose,Doctor_ID) VALUES(?,(Select `Doctor_ID` from `Users` where `ID`=?));";
        final long result = dataBaseCommander.insert(sql, diagnoseName, userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create diagnose");
        }
    }
}
