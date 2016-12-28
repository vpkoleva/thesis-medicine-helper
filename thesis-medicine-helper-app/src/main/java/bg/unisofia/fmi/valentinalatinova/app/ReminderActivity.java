package bg.unisofia.fmi.valentinalatinova.app;

import org.joda.time.DateTimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;
import bg.unisofia.fmi.valentinalatinova.app.utils.DateUtils;
import bg.unisofia.fmi.valentinalatinova.app.utils.Logger;
import bg.unisofia.fmi.valentinalatinova.core.json.ScheduleInfo;

public class ReminderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ScheduleInfo schedule = (ScheduleInfo) getIntent().getSerializableExtra(Constants.ALARM_SCHEDULE);
        TextView text = (TextView) findViewById(R.id.reminder_text);
        String date = DateUtils.formatDateTime(schedule.getStart().withZone(DateTimeZone.getDefault()));
        text.setText(schedule.getTitle() + ": " + System.getProperty("line.separator") + date);
        Logger.debug(ReminderActivity.class, "Reminder: " + schedule.getTitle() + ": " + date);
        registerOnClickListeners();
    }

    // Private methods
    private void registerOnClickListeners() {
        // OK button
        Button ok = (Button) findViewById(R.id.reminder_button_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
