package bg.unisofia.fmi.valentinalatinova.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ALARM_SCHEDULE, arg1.getSerializableExtra(Constants.ALARM_SCHEDULE));
        intent.setClassName(ReminderActivity.class.getPackage().getName(), ReminderActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
