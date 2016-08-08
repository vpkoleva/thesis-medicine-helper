package bg.unisofia.fmi.valentinalatinova.core.utils;

public enum Duration {
    HOUR(1),
    DAY(2),
    MONTH(3),
    YEAR(4);

    private int value;

    Duration(int value) {
        this.value = value;
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
        throw new IllegalArgumentException();
    }
}
