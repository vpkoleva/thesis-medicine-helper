package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.dto.MobileScheduleDto;

import java.util.List;

public interface MobileScheduleDao extends BaseDao<MobileScheduleDto> {

    /**
     * Gets all schedules for given user.
     *
     * @return
     */
    List<MobileScheduleDto> getAll(long userId);
}
