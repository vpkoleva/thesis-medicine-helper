package bg.unisofia.fmi.valentinalatinova.app;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.Schedule;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;

public class ManageScheduleActivity extends Activity {

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
        currentScheduleId = intent.getLongExtra(Constants.SCHEDULE_ID, -1);
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

    // Private methods
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
        Button cancel = (Button) findViewById(R.id.manage_schedule_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initialiseManageScheduleForm() {
        final boolean isEdit = currentSchedule != null;
        // Description
        String descriptionText = isEdit ? currentSchedule.getDescription() : "";
        EditText description = (EditText) findViewById(R.id.manage_schedule_description);
        description.setText(descriptionText);
        // Date
        DateTime dateTime = isEdit ? calculateStartTime(currentSchedule.getStartDate(), currentSchedule.getStartAfter(),
                currentSchedule.getStartAfterType()) : DateTime.now();
        DatePicker date = (DatePicker) findViewById(R.id.manage_schedule_date);
        int year = dateTime.getYear();
        // Calendar month starts from 0
        int month = dateTime.getMonthOfYear() - 1;
        int day = dateTime.getDayOfMonth();
        date.init(year, month, day, null);
        // Time
        TimePicker time = (TimePicker) findViewById(R.id.manage_schedule_time);
        int hour = dateTime.getHourOfDay();
        time.setIs24HourView(true);
        time.setCurrentHour(isEdit ? hour : hour + 1);
        time.setCurrentMinute(0);
        // StartAfter
        int startAfter = isEdit ? currentSchedule.getStartAfter() : 0;
        createDurationNumberPicker(R.id.manage_schedule_startAfter, startAfter, 0);
        Duration startAfterType = isEdit ? currentSchedule.getStartAfterType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_startAfter_type, startAfterType);
        // Frequency
        int frequency = isEdit ? currentSchedule.getFrequency() : 1;
        createDurationNumberPicker(R.id.manage_schedule_frequency, frequency, 1);
        Duration frequencyType = isEdit ? currentSchedule.getFrequencyType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_frequency_type, frequencyType);
        // Duration
        int duration = isEdit ? currentSchedule.getDuration() : 1;
        createDurationNumberPicker(R.id.manage_schedule_duration, duration, 1);
        Duration durationType = isEdit ? currentSchedule.getDurationType() : Duration.DAY;
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
        // StartAfter
        NumberPicker startAfter = (NumberPicker) findViewById(R.id.manage_schedule_startAfter);
        currentSchedule.setStartAfter(startAfter.getValue());
        Spinner startAfterType = (Spinner) findViewById(R.id.manage_schedule_startAfter_type);
        currentSchedule.setStartAfterType(toDuration(startAfterType.getSelectedItem().toString()));
        // StartDate
        DateTime dateTime = calculateStartTime(new DateTime(year, month, day, hour, minute),
                currentSchedule.getStartAfter(), currentSchedule.getStartAfterType());
        currentSchedule.setStartDate(dateTime);
        // Frequency
        NumberPicker frequency = (NumberPicker) findViewById(R.id.manage_schedule_frequency);
        currentSchedule.setFrequency(frequency.getValue());
        Spinner frequencyType = (Spinner) findViewById(R.id.manage_schedule_frequency_type);
        currentSchedule.setFrequencyType(toDuration(frequencyType.getSelectedItem().toString()));
        // Duration
        NumberPicker duration = (NumberPicker) findViewById(R.id.manage_schedule_duration);
        currentSchedule.setDuration(duration.getValue());
        Spinner durationType = (Spinner) findViewById(R.id.manage_schedule_duration_type);
        currentSchedule.setDurationType(toDuration(durationType.getSelectedItem().toString()));
    }

    private void createDurationNumberPicker(int id, int value, int minValue) {
        NumberPicker duration = (NumberPicker) findViewById(id);
        duration.setMinValue(minValue);
        duration.setMaxValue(30);
        duration.setValue(value);
    }

    private void createDurationSpinner(int id, Duration selectedValue) {
        Spinner spinner = (Spinner) findViewById(id);
        List<String> duration = new ArrayList<>();
        int selection = 0;
        for (int i = 0; i < Duration.values().length; i++) {
            Duration item = Duration.values()[i];
            duration.add(fromDuration(item));
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

    public DateTime calculateStartTime(DateTime dateTime, int value, Duration duration) {
        switch (duration) {
            case HOUR:
                return dateTime.minusHours(value).withZone(DateTimeZone.getDefault());
            case DAY:
                return dateTime.minusDays(value).withZone(DateTimeZone.getDefault());
            case MONTH:
                return dateTime.minusMonths(value).withZone(DateTimeZone.getDefault());
            case YEAR:
                return dateTime.minusYears(value).withZone(DateTimeZone.getDefault());
            default:
                throw new IllegalArgumentException();
        }
    }

    private String fromDuration(Duration duration) {
        switch (duration) {
            case HOUR:
                return getString(R.string.duration_hour);
            case DAY:
                return getString(R.string.duration_day);
            case MONTH:
                return getString(R.string.duration_month);
            case YEAR:
                return getString(R.string.duration_year);
            default:
                return getString(R.string.duration_day);
        }
    }

    private Duration toDuration(String string) {
        if (string.equals(getString(R.string.duration_hour))) {
            return Duration.HOUR;
        } else if (string.equals(getString(R.string.duration_month))) {
            return Duration.MONTH;
        } else if (string.equals(getString(R.string.duration_year))) {
            return Duration.YEAR;
        } else {
            return Duration.DAY;
        }
    }

    private class GetSchedule extends AsyncTask<Long, String, Schedule> {

        private static final String PATH_SCHEDULE = "/mobile/schedule/get/";

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