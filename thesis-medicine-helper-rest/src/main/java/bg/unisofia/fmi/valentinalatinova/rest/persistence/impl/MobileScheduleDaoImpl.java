package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.core.dto.MobileScheduleDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileScheduleDao;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileScheduleDaoImpl implements MobileScheduleDao {

    private static List<MobileScheduleDto> schedules = new ArrayList<>();
    private static long uniqueId = 1;

    static {
        schedules.add(new MobileScheduleDto(uniqueId++, "Do1", DateTime.now(), 1, Duration.HOUR, 2, Duration.DAY, 123));
        schedules.add(new MobileScheduleDto(uniqueId++, "Do2", DateTime.now(), 1, Duration.MONTH, 1, Duration.DAY, 5));
    }

    @Override
    public List<MobileScheduleDto> getAll(long userId) {
        return schedules;
    }

    @Override
    public ResultDto save(MobileScheduleDto schedule) {
        try {
            schedule.setId(uniqueId++);
            schedules.add(schedule);
            return ResultDto.createSuccess(schedule.getId());
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }

    @Override
    public ResultDto update(MobileScheduleDto schedule) {
        try {
            schedules.remove(schedule);
            schedules.add(schedule);
            return ResultDto.createSuccess(schedule.getId());
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }

    @Override
    public ResultDto delete(long id, long userId) {
        try {
            for (MobileScheduleDto schedule : schedules) {
                if (schedule.getId() == id) {
                    schedules.remove(schedule);
                    return ResultDto.createSuccess(id);
                }
            }
            return ResultDto.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }
}
