package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileTable;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileTableValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDao;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileTableDaoImpl implements MobileTableDao {
    private static List<MobileTable> tables = new ArrayList<>();

    static {
        List<MobileTableValue> values1 = new ArrayList<>();
        values1.add(new MobileTableValue(1, "Measurement1", DateTime.now(), 1));
        values1.add(new MobileTableValue(1, "Measurement2", DateTime.now(), 1));
        tables.add(new MobileTable(1, "Table1", values1, 1));

        List<MobileTableValue> values2 = new ArrayList<>();
        values2.add(new MobileTableValue(1, "Measurement1", DateTime.now(), 2));
        values2.add(new MobileTableValue(1, "Measurement2", DateTime.now(), 2));
        tables.add(new MobileTable(2, "Table2", values2, 1));
    }

    @Override
    public List<MobileTable> getTables(long userId) {
        return tables;
    }

    @Override
    public Result save(MobileTableValue tableValue) {
        try {
            tables.get(0).getValues().add(tableValue);
            return Result.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result update(MobileTableValue tableValue) {
        try {
            tables.get(0).getValues().remove(tableValue);
            tables.get(0).getValues().add(tableValue);
            return Result.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result delete(long id, long userId) {
        try {
            for (MobileTableValue tableValue : tables.get(0).getValues()) {
                if (tableValue.getId() == id) {
                    tables.get(0).getValues().remove(tableValue);
                    return Result.createSuccess(id);
                }
            }
            return Result.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }
}
