package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

import java.util.List;

public interface MobileResultsDao extends BaseDao<MobileResultsValue> {

    /**
     * Get mobile results for given user id.
     *
     * @param userId user id
     * @return list with MobileResults objects
     */
    List<MobileResults> getResults(long userId);

    /**
     * Creates results table with no values.
     *
     * @param result results table
     * @return result of the operation
     */
    Result createResult(MobileResults result);

    /**
     * Deletes results table with all its values.
     *
     * @param id results table id
     * @param userId user that makes the deletion
     * @return result of the operation
     */
    Result deleteResult(long id, long userId);
}
