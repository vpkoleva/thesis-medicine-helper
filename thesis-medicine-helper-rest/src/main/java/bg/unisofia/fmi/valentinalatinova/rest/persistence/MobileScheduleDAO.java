package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.rest.utils.Result;
import bg.unisofia.fmi.valentinalatinova.rest.utils.Duration;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileScheduleDAO {

    private static List<MobileSchedule> schedules = new ArrayList<MobileSchedule>();

    static {
        schedules.add(new MobileSchedule(1, "Do1", DateTime.now(), 1, Duration.HOUR, 2, Duration.DAY, 123));
    }

    public static List<MobileSchedule> getAll() {
        return schedules;
    }

    public static String save(MobileSchedule schedule) {
        try {
            schedules.add(schedule);
            return Result.OK.toString();
        } catch (Exception ex) {
            return Result.KO.toString();
        }
    }

    public static String update(MobileSchedule schedule) {
        try {
            schedules.remove(schedule);
            schedules.add(schedule);
            return Result.OK.toString();
        } catch (Exception ex) {
            return Result.KO.toString();
        }
    }

    public static String remove(long id) {
        try {
            for (MobileSchedule schedule : schedules) {
                if (schedule.getId() == id) {
                    schedules.remove(schedule);
                    return Result.OK.toString();
                }
            }
            return Result.KO.toString();
        } catch (Exception ex) {
            return Result.KO.toString();
        }
    }
}
