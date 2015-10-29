package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileResultsDao;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileResultsDaoImpl implements MobileResultsDao {
    private static List<MobileResults> results = new ArrayList<>();

    static {
        List<MobileResultsValue> values1 = new ArrayList<>();
        values1.add(new MobileResultsValue(1, "Measurement1", DateTime.now(), 1));
        values1.add(new MobileResultsValue(2, "Measurement2", DateTime.now(), 1));
        results.add(new MobileResults(1, "Table1", "unit1", values1, 1));

        List<MobileResultsValue> values2 = new ArrayList<>();
        values2.add(new MobileResultsValue(1, "Measurement1", DateTime.now(), 2));
        values2.add(new MobileResultsValue(2, "Measurement2", DateTime.now(), 2));
        results.add(new MobileResults(2, "Table2", "", values2, 1));

        List<MobileResultsValue> values3 = new ArrayList<>();
        values3.add(new MobileResultsValue(1, "Measurement1", DateTime.now(), 3));
        values3.add(new MobileResultsValue(2, "Measurement2", DateTime.now(), 3));
        results.add(new MobileResults(3, "Table3", null, values3, 1));
    }

    @Override
    public List<MobileResults> getResults(long userId) {
        return results;
    }

    @Override
    public Result createResult(MobileResults result) {
        try {
            results.add(result);
            return Result.createSuccess(result.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result deleteResult(long id, long userId) {
        try {
            for (MobileResults result : results) {
                if (result.getId() == id) {
                    results.remove(result);
                    return Result.createSuccess(id);
                }
            }
            return Result.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result save(MobileResultsValue tableValue) {
        try {
            results.get(0).getValues().add(tableValue);
            return Result.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result update(MobileResultsValue tableValue) {
        try {
            results.get(0).getValues().remove(tableValue);
            results.get(0).getValues().add(tableValue);
            return Result.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result delete(long id, long userId) {
        try {
            for (MobileResultsValue tableValue : results.get(0).getValues()) {
                if (tableValue.getId() == id) {
                    results.get(0).getValues().remove(tableValue);
                    return Result.createSuccess(id);
                }
            }
            return Result.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }
}
