package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.json.BaseJson;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

public interface BaseDao<T extends BaseJson> {

    /**
     * Saves DTO.
     *
     * @param dto
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    Result save(T dto);

    /**
     * Updates DTO.
     *
     * @param dto
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    Result update(T dto);

    /**
     * Deletes DTO.
     *
     * @param id of DTO to be deleted
     * @param userId of user that DTO belong to
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    Result delete(long id, long userId);
}
