package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.dto.MobileTableDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.MobileTableValueDto;

import java.util.List;

public interface MobileTableDao extends BaseDao<MobileTableValueDto> {

    /**
     * Get mobile tables for given user id.
     *
     * @param userId
     * @return list with MobileTableDto objects
     */
    List<MobileTableDto> getTables(long userId);

}
