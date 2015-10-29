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
    private static int uniqueIdResult = 1;
    private static int uniqueIdValue = 1;

    static {
        List<MobileResultsValue> values1 = new ArrayList<>();
        values1.add(new MobileResultsValue(uniqueIdValue++, "Measurement1", DateTime.now(), uniqueIdResult));
        values1.add(new MobileResultsValue(uniqueIdValue++, "Measurement2", DateTime.now(), uniqueIdResult));
        results.add(new MobileResults(uniqueIdResult++, "Table1", "unit1", values1, 1));

        List<MobileResultsValue> values2 = new ArrayList<>();
        values2.add(new MobileResultsValue(uniqueIdValue++, "Measurement1", DateTime.now(), uniqueIdResult));
        values2.add(new MobileResultsValue(uniqueIdValue++, "Measurement2", DateTime.now(), uniqueIdResult));
        results.add(new MobileResults(uniqueIdResult++, "Table2", "", values2, 1));

        List<MobileResultsValue> values3 = new ArrayList<>();
        values3.add(new MobileResultsValue(uniqueIdValue++, "Measurement1", DateTime.now(), uniqueIdResult));
        values3.add(new MobileResultsValue(uniqueIdValue++, "Measurement2", DateTime.now(), uniqueIdResult));
        results.add(new MobileResults(uniqueIdResult++, "Table3", null, values3, 1));
    }

    @Override
    public List<MobileResults> getResults(long userId) {
        return results;
    }

    @Override
    public Result createResult(MobileResults result) {
        try {
            result.setId(uniqueIdResult++);
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
    public Result save(MobileResultsValue resultsValue) {
        try {
            resultsValue.setId(uniqueIdValue++);
            for (MobileResults result : results) {
                if (result.getId() == resultsValue.getResultsId()) {
                    result.getValues().add(resultsValue);
                }
            }
            return Result.createSuccess(resultsValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result update(MobileResultsValue resultsValue) {
        try {
            for (MobileResults result : results) {
                if (result.getId() == resultsValue.getResultsId()) {
                    result.getValues().remove(resultsValue);
                    result.getValues().add(resultsValue);
                }
            }
            return Result.createSuccess(resultsValue.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    @Override
    public Result delete(long id, long userId) {
        try {
            for (MobileResults result : results) {
                for (MobileResultsValue tableValue : result.getValues()) {
                    if (tableValue.getId() == id) {
                        result.getValues().remove(tableValue);
                        return Result.createSuccess(id);
                    }
                }
            }
            return Result.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }
}
