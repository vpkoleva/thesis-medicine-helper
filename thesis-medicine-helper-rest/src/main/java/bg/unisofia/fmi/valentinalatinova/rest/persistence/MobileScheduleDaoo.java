package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileScheduleDto;

import java.util.List;

public interface MobileScheduleDaoo extends BaseDao<MobileScheduleDto> {

    /**
     * Gets all schedules for given user.
     *
     * @return
     */
    List<MobileScheduleDto> getAll(long userId);
}
