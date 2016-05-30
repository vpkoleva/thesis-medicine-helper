package bg.unisofia.fmi.valentinalatinova.core.utils;

import org.joda.time.DateTime;

public enum Duration {
    HOUR(1),
    DAY(2),
    MONTH(3),
    YEAR(4);

    private int value;

    Duration(int value) {
        this.value = value;
    }

    public DateTime calculateDateTime(DateTime dateTime, int value) {
        switch (this) {
            case HOUR:
                return dateTime.plusHours(value);
            case DAY:
                return dateTime.plusDays(value);
            case MONTH:
                return dateTime.plusMonths(value);
            case YEAR:
                return dateTime.plusYears(value);
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }

    public static Duration fromValue(int value) {
        for (Duration item : Duration.values()) {
            if (value == item.getValue()) {
                return item;
            }
        }
        return null;
    }

    public double durationToDays(int durValue) {
        switch (this) {
            case HOUR:
                return 24 / durValue;
            case DAY:
                return durValue;
            case MONTH:
                return 30 * durValue;
            case YEAR:
                return 365 * durValue;
            default:
                return 0;
        }
    }
}
