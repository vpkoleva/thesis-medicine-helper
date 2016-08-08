package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import java.sql.PreparedStatement;
import java.util.List;

import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.json.Diagnose;

public class DiagnoseDAO {

    private DataBaseCommander dataBaseCommander;

    public DiagnoseDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<Diagnose> get(final long userId) {
        String sql = "SELECT * FROM `diagnoses` WHERE `doctor_ID`=(SELECT `doctor_ID` FROM `users` WHERE `ID`=?)";
        return dataBaseCommander.select(Diagnose.class, sql, userId);
    }

    public Result save(final Diagnose diagnose, final long userId) {
        String sql = "INSERT INTO `diagnoses` (diagnose, doctor_ID) "
                + "VALUES (?, (SELECT `doctor_ID` from `users` WHERE `ID`=?));";
        final long result = dataBaseCommander.insert(sql, diagnose.getDiagnoseName(), userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create diagnose");
        }
    }

    public Result delete(final long diagnoseId, final long userId) {
        PreparedStatement deleteSchedules = dataBaseCommander
                .createPreparedStatement("DELETE FROM `schedules` WHERE `diagnoses_ID`=?", diagnoseId);
        PreparedStatement deleteDiagnose = dataBaseCommander.createPreparedStatement("DELETE FROM `diagnoses` WHERE "
                + "id=? AND `doctor_ID`=(SELECT `doctor_ID` FROM `users` WHERE `ID`=?)", diagnoseId, userId);
        final boolean result = dataBaseCommander.execute(deleteSchedules, deleteDiagnose);
        if (result) {
            return Result.createSuccess(-1);
        } else {
            return Result.createError("Cannot delete diagnose");
        }
    }
}
