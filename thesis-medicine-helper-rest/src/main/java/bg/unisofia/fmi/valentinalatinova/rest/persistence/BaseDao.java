package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.dto.BaseDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.ResultDto;

public interface BaseDao<T extends BaseDto> {

    /**
     * Saves DTO.
     *
     * @param dto
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    ResultDto save(T dto);

    /**
     * Updates DTO.
     *
     * @param dto
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    ResultDto update(T dto);

    /**
     * Deletes DTO.
     *
     * @param id of DTO to be deleted
     * @param userId of user that DTO belong to
     * @return Result TRUE in case of success and FALSE in case of failure
     */
    ResultDto delete(long id, long userId);
}
