package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

public class MobileResultsDaoImpl {
    private static List<MobileResults> results = new ArrayList<>();
    private static long uniqueIdResult = 1;
    private static long uniqueIdValue = 1;

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

    public List<MobileResults> getResults(long userId) {
        return results;
    }

    public Result createResult(MobileResults result) {
        try {
            result.setId(uniqueIdResult++);
            results.add(result);
            return Result.createSuccess(result.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

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
