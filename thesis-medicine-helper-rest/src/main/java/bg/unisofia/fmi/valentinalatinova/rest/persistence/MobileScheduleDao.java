package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileSchedule;

import java.util.List;

public interface MobileScheduleDao extends BaseDao<MobileSchedule> {

    /**
     * Gets all schedules for given user.
     *
     * @return
     */
    List<MobileSchedule> getAll(long userId);
}
