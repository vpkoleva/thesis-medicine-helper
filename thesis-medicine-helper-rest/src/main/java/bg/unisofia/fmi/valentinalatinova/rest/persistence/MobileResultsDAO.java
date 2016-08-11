package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.data.MobileResultsDO;
import bg.unisofia.fmi.valentinalatinova.rest.data.MobileValueDO;

public class MobileResultsDAO {

    private DataBaseCommander dataBaseCommander;

    public MobileResultsDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public List<MobileResults> getResults(long userId) {
        String sqlTables = "SELECT * FROM `mtables` "
                + "WHERE `mobileuser_ID`=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)";
        List<MobileResultsDO> tables = dataBaseCommander.select(MobileResultsDO.class, sqlTables, userId);
        if (tables.isEmpty()) {
            return new ArrayList<>();
        }

        String tableIds = "";
        for (MobileResultsDO table : tables) {
            tableIds += table.getId() + ",";
        }
        tableIds = tableIds.substring(0, tableIds.length() - 1);

        String sqlvalues = "SELECT * FROM `mtablevalues` WHERE mtables_ID IN (" + tableIds + ") "
                + "AND `mobileuser_ID`=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)";
        List<MobileValueDO> values = dataBaseCommander.select(MobileValueDO.class, sqlvalues, userId);
        Map<Long, List<MobileResultsValue>> valuesMap = new HashMap<>();

        for (MobileValueDO value : values) {
            List<MobileResultsValue> resultsValues = valuesMap.get(value.getResultId());
            if (resultsValues == null) {
                resultsValues = new ArrayList<>();
            }
            resultsValues.add(value.getMobileResultsValue());
            valuesMap.put(value.getResultId(), resultsValues);
        }

        for (MobileResultsDO table : tables) {
            table.getMobileResults().setValues(valuesMap.get(table.getId()));
        }

        List<MobileResults> result = new ArrayList<>();
        for (MobileResultsDO table : tables) {
            result.add(table.getMobileResults());
        }
        return result;
    }

    public Result saveResult(final MobileResults value, final long userId) {
        String sql = "INSERT INTO `mtables` (name, units, mobileuser_ID) "
                + "VALUES(?,?,(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?))";
        final long result = dataBaseCommander.insert(sql, value.getName(), value.getUnits(), userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot create mobile result table");
        }
    }

    public Result saveValue(final MobileResultsValue resultsValue, final long userId) {
        String sql = "INSERT INTO `mtablevalues` (measurement, date, mtables_ID, mobileuser_ID) "
                + "VALUES(?,?,?,(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?))";
        final long result = dataBaseCommander.insert(sql, resultsValue.getMeasurement(),
                new Timestamp(resultsValue.getMeasurementDate().getMillis()), resultsValue.getResultsId(), userId);
        if (result > 0) {
            return Result.createSuccess(result);
        } else {
            return Result.createError("Cannot save mobile result");
        }
    }

    public Result updateValue(final MobileResultsValue resultsValue, final long userId) {
        String sql = "UPDATE `mtablevalues` SET measurement=?, date=? "
                + "WHERE ID=? AND mobileuser_ID=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)";
        final boolean result = dataBaseCommander.deleteOrUpdate(sql, resultsValue.getMeasurement(),
                new Timestamp(resultsValue.getMeasurementDate().getMillis()), resultsValue.getId(), userId);
        if (result) {
            return Result.createSuccess(resultsValue.getId());
        } else {
            return Result.createError("Cannot update mobile result");
        }
    }

    public Result deleteResult(long id, long userId) {
        PreparedStatement deleteValues = dataBaseCommander
                .createPreparedStatement("DELETE FROM `mtablevalues` WHERE mtables_ID=? "
                        + "AND `mobileuser_ID`=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)", id, userId);
        PreparedStatement deleteTable = dataBaseCommander
                .createPreparedStatement("DELETE FROM `mtables` WHERE `ID`=? "
                        + "AND `mobileuser_ID`=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)", id, userId);
        final boolean result = dataBaseCommander.execute(deleteValues, deleteTable);
        if (result) {
            return Result.createSuccess(id);
        } else {
            return Result.createError("Cannot delete mobile result table");
        }
    }

    public Result deleteValue(long id, long userId) {
        String sql = "DELETE FROM `mtablevalues` WHERE ID=? "
                + "AND `mobileuser_ID`=(SELECT `mobileuser_ID` FROM `users` WHERE `ID`=?)";
        final boolean result = dataBaseCommander.deleteOrUpdate(sql, id, userId);
        if (result) {
            return Result.createSuccess(id);
        } else {
            return Result.createError("Cannot delete mobile result");
        }
    }
}
