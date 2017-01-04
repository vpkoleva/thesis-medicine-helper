package bg.unisofia.fmi.valentinalatinova.app.utils;

import java.text.DateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public final class DateUtils {

    private DateUtils() {
        // Utils class
    }

    public static String formatDay(Date date) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }

    public static String formatTime(DateTime date) {
        return DateTimeFormat.forPattern("HH:mm").print(date);
    }

    public static String formatDateTime(DateTime date) {
        return formatDay(date.toDate()) + " " + formatTime(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return formatDay(date1).equals(formatDay(date2));
    }
}
