package bg.unisofia.fmi.valentinalatinova.rest.helpers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.json.ScheduleInfo;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;

public class SchedulesConversion {

    public WebScheduleDO convertJsonToDO(Schedule schedule) {
        final WebScheduleDO result = new WebScheduleDO();

        result.setId(schedule.getId());
        result.setSchedule(schedule);
        DateTime startDate = schedule.getStartDate() == null ? new DateTime(0) : schedule.getStartDate();
        startDate = startDate.withZone(DateTimeZone.UTC);
        DateTime start = calculateDateTime(startDate, schedule.getStartAfter(), schedule.getStartAfterType());
        DateTime end = calculateDateTime(start, schedule.getDuration(), schedule.getDurationType());
        result.setEndDate(new Timestamp(end.getMillis()));
        result.setStartDate(new Timestamp(startDate.getMillis()));

        return result;
    }

    public List<ScheduleInfo> convertDOtoJsonList(List<WebScheduleDO> webSchedules, String startDate, String endDate) {
        final List<ScheduleInfo> result = new ArrayList<>();

        for (WebScheduleDO webSchedule : webSchedules) {
            final Schedule schedule = webSchedule.getSchedule();
            final DateTime end = new DateTime(endDate);
            DateTime start = setStartDate(schedule, startDate);
            while (!start.isAfter(end) && start.isBefore(webSchedule.getEndDate().getTime())) {
                ScheduleInfo dataObj = new ScheduleInfo();
                dataObj.setId(schedule.getId());
                dataObj.setTitle(schedule.getDescription());
                dataObj.setStart(start);
                dataObj.setEnd(start.plusHours(1));
                result.add(dataObj);
                start = getNextStartDate(start, schedule);
            }
        }

        return result;
    }

    public List<Schedule> convertDOtoJson(List<WebScheduleDO> webSchedules) {
        final List<Schedule> result = new ArrayList<>();

        for (WebScheduleDO webSchedule : webSchedules) {
            result.add(webSchedule.getSchedule());
        }

        return result;
    }

    private DateTime getNextStartDate(DateTime start, Schedule schedule) {
        return calculateDateTime(start, schedule.getFrequency(), schedule.getFrequencyType());
    }

    private DateTime setStartDate(Schedule schedule, String startDateFromReq) {
        DateTime start = schedule.getStartDate();
        start = calculateDateTime(start, schedule.getStartAfter(), schedule.getStartAfterType());
        DateTime expectedStart = new DateTime(startDateFromReq);
        while (start.isBefore(expectedStart)) {
            start = getNextStartDate(start, schedule);
        }
        return start;
    }

    private DateTime calculateDateTime(DateTime dateTime, int value, Duration duration) {
        switch (duration) {
            case HOUR:
                return dateTime.plusHours(value);
            case DAY:
                return dateTime.plusDays(value);
            case MONTH:
                return dateTime.plusMonths(value);
            case YEAR:
                return dateTime.plusYears(value);
            default:
                throw new IllegalArgumentException();
        }
    }
}
