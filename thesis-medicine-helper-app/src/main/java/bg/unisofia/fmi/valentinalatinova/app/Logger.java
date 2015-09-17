package bg.unisofia.fmi.valentinalatinova.app;

import android.util.Log;

public class Logger {
    private static final String LOG_NAME = "medicine-helper";

    public static void error(Exception exception) {
        Log.e(LOG_NAME, exception.getMessage(), exception);
    }

    public static void debug(String message) {
        Log.d(LOG_NAME, message);
    }
}
