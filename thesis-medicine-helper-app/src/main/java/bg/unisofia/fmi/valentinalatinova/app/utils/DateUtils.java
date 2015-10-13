package bg.unisofia.fmi.valentinalatinova.app.utils;

import java.text.DateFormat;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    public static String formatDay(Date date) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return formatDay(date1).equals(formatDay(date2));
    }
}
