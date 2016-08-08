package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;

public class MobileScheduleDaoImpl {

    private static List<MobileSchedule> schedules = new ArrayList<>();
    private static long uniqueId = 1;

    static {
        schedules.add(new MobileSchedule(uniqueId++, "Do1", DateTime.now(), 1, Duration.HOUR, 2, Duration.DAY, 123));
        schedules.add(new MobileSchedule(uniqueId++, "Do2", DateTime.now(), 1, Duration.MONTH, 1, Duration.DAY, 5));
    }

    public List<MobileSchedule> getAll(long userId) {
        return schedules;
    }

    public Result save(MobileSchedule schedule) {
        try {
            schedule.setId(uniqueId++);
            schedules.add(schedule);
            return Result.createSuccess(schedule.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    public Result update(MobileSchedule schedule) {
        try {
            schedules.remove(schedule);
            schedules.add(schedule);
            return Result.createSuccess(schedule.getId());
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }

    public Result delete(long id, long userId) {
        try {
            for (MobileSchedule schedule : schedules) {
                if (schedule.getId() == id) {
                    schedules.remove(schedule);
                    return Result.createSuccess(id);
                }
            }
            return Result.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return Result.createError(ex.getMessage());
        }
    }
}
