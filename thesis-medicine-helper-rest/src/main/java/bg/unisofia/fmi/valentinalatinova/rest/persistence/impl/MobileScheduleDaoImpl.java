package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileScheduleDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileScheduleDaoo;
import bg.unisofia.fmi.valentinalatinova.rest.utils.Duration;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileScheduleDaoImpl implements MobileScheduleDaoo {

    private static List<MobileScheduleDto> schedules = new ArrayList<>();

    static {
        schedules.add(new MobileScheduleDto(1, "Do1", DateTime.now(), 1, Duration.HOUR, 2, Duration.DAY, 123));
        schedules.add(new MobileScheduleDto(3, "Do2", DateTime.now(), 1, Duration.YEAR, 1, Duration.DAY, 5));
    }

    @Override
    public List<MobileScheduleDto> getAll(long userId) {
        return schedules;
    }

    @Override
    public ResultDto save(MobileScheduleDto schedule) {
        try {
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
    public ResultDto delete(long id) {
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
