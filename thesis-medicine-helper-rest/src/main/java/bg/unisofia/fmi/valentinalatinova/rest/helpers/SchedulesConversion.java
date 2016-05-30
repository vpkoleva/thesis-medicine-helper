package bg.unisofia.fmi.valentinalatinova.rest.helpers;

import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleBO;
import bg.unisofia.fmi.valentinalatinova.core.json.WebScheduleListBO;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SchedulesConversion {

    public List<WebScheduleDO> convertBOtoDO(WebScheduleBO... listWithSchedules) {
        List<WebScheduleDO> result = new ArrayList<>();

        for (WebScheduleBO schedule : listWithSchedules) {
            WebScheduleDO dataObj = new WebScheduleDO();

            DateTime start;
            if (schedule.getStartDate() == null) {
                start = new DateTime(0);
            } else {
                start = schedule.getStartDate();
                dataObj.setPatientId(schedule.getPatientId());
            }

            dataObj.setId(schedule.getId());
            dataObj.setDescription(schedule.getDescription());
            dataObj.setDiagnoseId(schedule.getDiagnoseId());
            dataObj.setDoctorId(schedule.getDoctorId());
            start = schedule.getStartAfterType().calculateDateTime(start, schedule.getStartAfter());
            DateTime end = schedule.getDurationType().calculateDateTime(start, schedule.getDuration());
            dataObj.setStartDate(new Timestamp(start.getMillis()));
            dataObj.setEndDate(new Timestamp(end.getMillis()));
            dataObj.setFrequencyValue(schedule.getFrequency());
            dataObj.setFrequencyType(Duration.fromValue(schedule.getFrequencyType().getValue()));
            dataObj.setEndAfterType(schedule.getDurationType());
            dataObj.setEndAfterValue(schedule.getDuration());
            dataObj.setStartAfterType(schedule.getStartAfterType());
            dataObj.setStartAfterValue(schedule.getStartAfter());
            result.add(dataObj);
        }
        return result;
    }

    public List<WebScheduleListBO> convertDOtoBOList(List<WebScheduleDO> listWithSchedules, String startDateFromReq, String endDateFromReq) {
        List<WebScheduleListBO> result = new ArrayList<>();

        for (WebScheduleDO schedule : listWithSchedules) {

            DateTime start = setStartDate(schedule, startDateFromReq);
            while (start.isBefore(new DateTime(endDateFromReq)) && start.isBefore(schedule.getEndDate().getTime())) {
                WebScheduleListBO dataObj = new WebScheduleListBO();
                dataObj.setId(schedule.getId());
                dataObj.setDescription(schedule.getDescription());
                dataObj.setStartDate(start);
                dataObj.setEndDate(start.plusHours(1));
                result.add(dataObj);
                start = schedule.getFrequencyType().calculateDateTime(start, schedule.getFrequencyValue());
            }
        }
        return result;
    }

    public WebScheduleBO convertDOtoBO(WebScheduleDO scheduleFromDB) {

        WebScheduleBO result = new WebScheduleBO();
        result.setId(scheduleFromDB.getId());
        result.setDescription(scheduleFromDB.getDescription());
        result.setFrequencyType(scheduleFromDB.getFrequencyType());
        result.setFrequency(scheduleFromDB.getFrequencyValue());


        return result;
    }

    public DateTime setStartDate(WebScheduleDO schedule, String startDateFromReq) {
        DateTime start = new DateTime(schedule.getStartDate().getTime());
        DateTime startFromReq = new DateTime(startDateFromReq);
        int daysFromStartToStartReq = Days.daysBetween(start, startFromReq).getDays();
        double daysFromDur = schedule.getFrequencyType().durationToDays(schedule.getFrequencyValue());
        Double diff = daysFromStartToStartReq / daysFromDur;
        DateTime startNew = schedule.getFrequencyType().calculateDateTime(start, schedule.getFrequencyValue() * diff.intValue());
        startNew = startNew.minusMonths(1);
        if (start.isBefore(startNew)) {
            start = startNew;
        }
        return start;
    }
}
