package bg.unisofia.fmi.valentinalatinova.app.utils;

import android.util.Log;

public final class Logger {
    private static final String LOG_NAME = "medicine-helper";

    private Logger() {
        // Utils class
    }

    public static void error(Exception exception) {
        Log.e(LOG_NAME, exception.getMessage(), exception);
    }

    public static void debug(Class clazz, String message) {
        Log.d(LOG_NAME, clazz.getName() + ": " + message);
    }
}
