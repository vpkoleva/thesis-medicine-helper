package bg.unisofia.fmi.valentinalatinova.app;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;

public class ManageScheduleActivity extends Activity {

    public static final String SCHEDULE_ID = "ScheduleId";

    private static final String PATH_SAVE_SCHEDULE = "/mobile/schedule/save";
    private static final String PATH_UPDATE_SCHEDULE = "/mobile/schedule/update";

    private Schedule currentSchedule;
    private long currentScheduleId = -1;
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);
        // Get schedule object
        Intent intent = getIntent();
        currentScheduleId = intent.getLongExtra(SCHEDULE_ID, -1);
        // If schedule is passed then this is an Edit Action so capture its ID
        if (currentScheduleId != -1) {
            setTitle(R.string.manage_schedule_edit_window);
            progress = new ProgressDialog(this);
            new GetSchedule().execute(currentScheduleId);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading schedule data...");
            progress.show();
        } else {
            setTitle(R.string.manage_schedule_add_window);
            initialiseManageScheduleForm();
        }
        registerOnClickListeners();
    }

    private void registerOnClickListeners() {
        // Save button
        Button save = (Button) findViewById(R.id.manage_schedule_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readManageScheduleForm();
                // If some ID has been captured then this is an Edit Action
                if (currentScheduleId > 0) {
                    currentSchedule.setId(currentScheduleId);
                    new ManageSchedule().execute(PATH_UPDATE_SCHEDULE);
                } else {
                    new ManageSchedule().execute(PATH_SAVE_SCHEDULE);
                }
            }
        });
        // Cancel button
        Button button = (Button) findViewById(R.id.manage_schedule_button_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initialiseManageScheduleForm() {
        // Description
        String descriptionText = currentSchedule != null ? currentSchedule.getDescription() : "";
        EditText description = (EditText) findViewById(R.id.manage_schedule_description);
        description.setText(descriptionText);
        // Date
        DateTime dateTime = currentSchedule != null ? currentSchedule.getStartDate() : DateTime.now();
        DatePicker date = (DatePicker) findViewById(R.id.manage_schedule_date);
        int year = dateTime.getYear();
        // Calendar month starts from 0
        int month = dateTime.getMonthOfYear() - 1;
        int day = dateTime.getDayOfMonth();
        date.init(year, month, day, null);
        // Time
        TimePicker time = (TimePicker) findViewById(R.id.manage_schedule_time);
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour();
        time.setCurrentHour(hour);
        time.setCurrentMinute(minute);
        // StartAfter
        int startAfter = currentSchedule != null ? currentSchedule.getStartAfter() : 1;
        createDurationNumberPicker(R.id.manage_schedule_startAfter, startAfter);
        Duration startAfterType = currentSchedule != null ? currentSchedule.getStartAfterType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_startAfter_type, startAfterType);
        // Frequency
        int frequency = currentSchedule != null ? currentSchedule.getFrequency() : 1;
        createDurationNumberPicker(R.id.manage_schedule_frequency, frequency);
        Duration frequencyType = currentSchedule != null ? currentSchedule.getFrequencyType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_frequency_type, frequencyType);
        // Duration
        int duration = currentSchedule != null ? currentSchedule.getDuration() : 1;
        createDurationNumberPicker(R.id.manage_schedule_duration, duration);
        Duration durationType = currentSchedule != null ? currentSchedule.getDurationType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_duration_type, durationType);
    }

    private void readManageScheduleForm() {
        currentSchedule = new Schedule();
        // Description
        EditText description = (EditText) findViewById(R.id.manage_schedule_description);
        currentSchedule.setDescription(description.getText().toString());
        // Date
        DatePicker date = (DatePicker) findViewById(R.id.manage_schedule_date);
        int year = date.getYear();
        // Calendar month starts from 0
        int month = date.getMonth() + 1;
        int day = date.getDayOfMonth();
        // Time
        TimePicker time = (TimePicker) findViewById(R.id.manage_schedule_time);
        int hour = time.getCurrentHour();
        int minute = time.getCurrentMinute();
        DateTime dateTime = new DateTime(year, month, day, hour, minute);
        currentSchedule.setStartDate(dateTime);
        // StartAfter
        NumberPicker startAfter = (NumberPicker) findViewById(R.id.manage_schedule_startAfter);
        currentSchedule.setStartAfter(startAfter.getValue());
        Spinner startAfterType = (Spinner) findViewById(R.id.manage_schedule_startAfter_type);
        currentSchedule.setStartAfterType(Duration.valueOf(startAfterType.getSelectedItem().toString()));
        // Frequency
        NumberPicker frequency = (NumberPicker) findViewById(R.id.manage_schedule_frequency);
        currentSchedule.setFrequency(frequency.getValue());
        Spinner frequencyType = (Spinner) findViewById(R.id.manage_schedule_frequency_type);
        currentSchedule.setFrequencyType(Duration.valueOf(frequencyType.getSelectedItem().toString()));
        // Duration
        NumberPicker duration = (NumberPicker) findViewById(R.id.manage_schedule_duration);
        currentSchedule.setDuration(duration.getValue());
        Spinner durationType = (Spinner) findViewById(R.id.manage_schedule_duration_type);
        currentSchedule.setDurationType(Duration.valueOf(durationType.getSelectedItem().toString()));
    }

    private void createDurationNumberPicker(int id, int value) {
        NumberPicker duration = (NumberPicker) findViewById(id);
        duration.setMinValue(1);
        duration.setMaxValue(30);
        duration.setValue(value);
    }

    private void createDurationSpinner(int id, Duration selectedValue) {
        Spinner spinner = (Spinner) findViewById(id);
        List<String> duration = new ArrayList<>();
        int selection = 0;
        for (int i = 0; i < Duration.values().length; i++) {
            Duration item = Duration.values()[i];
            duration.add(item.toString());
            if (item.equals(selectedValue)) {
                selection = i;
            }
        }
        // Create the ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);
        // Set the Adapter
        spinner.setAdapter(arrayAdapter);
        // Set selected item
        spinner.setSelection(selection);
    }

    private void assembleResponseAndFinish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private class GetSchedule extends AsyncTask<Long, String, Schedule> {

        private final String PATH_SCHEDULE = "/mobile/schedule/get/";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected Schedule doInBackground(Long... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.get(PATH_SCHEDULE + params[0], Schedule.class);
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(Schedule result) {
            currentSchedule = result;
            if (progress != null) {
                progress.dismiss();
            }
            initialiseManageScheduleForm();
        }
    }

    private class ManageSchedule extends AsyncTask<String, String, Result> {

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected Result doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.post(params[0], currentSchedule, Result.class);
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null) {
                if (result.isSuccess()) {
                    currentSchedule.setId(result.getId());
                    assembleResponseAndFinish();
                } else {
                    MainActivity.createErrorDialog(ManageScheduleActivity.this, result.getError());
                }
            }
        }
    }
}