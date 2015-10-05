package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileTable;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileTableValue;

import java.util.List;

public interface MobileTableDao extends BaseDao<MobileTableValue> {

    /**
     * Get mobile tables for given user id.
     *
     * @param userId
     * @return list with MobileTable objects
     */
    List<MobileTable> getTables(long userId);

}
