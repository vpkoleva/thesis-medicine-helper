package bg.unisofia.fmi.valentinalatinova.app;

import android.app.Activity;
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
import bg.unisofia.fmi.valentinalatinova.app.tabs.SchedulesFragment;
import bg.unisofia.fmi.valentinalatinova.core.dto.MobileScheduleDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.core.utils.Duration;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ManageScheduleActivity extends Activity {

    private final String PATH_SAVE_SCHEDULE = "/mobile/schedule/save";
    private final String PATH_UPDATE_SCHEDULE = "/mobile/schedule/update";
    private MobileScheduleDto currentSchedule;
    private long currentScheduleId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        // Get schedule object
        Intent intent = getIntent();
        currentSchedule = (MobileScheduleDto) intent.getSerializableExtra(SchedulesFragment.RESULT_EXTRA);
        // If schedule is passed then this is an Edit Action so capture its ID
        if (currentSchedule != null) {
            currentScheduleId = currentSchedule.getId();
            setTitle(R.string.schedules_context_edit);
        } else {
            setTitle(R.string.schedules_button_add);
        }
        // Fill form with existing data
        initialiseManageScheduleForm();
        registerOnClickListenerButtonSave();
        registerOnClickListenerButtonCancel();
    }

    private void registerOnClickListenerButtonSave() {
        Button button = (Button) findViewById(R.id.manage_schedule_button_save);
        button.setOnClickListener(new View.OnClickListener() {
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
    }

    private void registerOnClickListenerButtonCancel() {
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
        // Duration
        int duration = currentSchedule != null ? currentSchedule.getDuration() : 1;
        createDurationNumberPicker(R.id.manage_schedule_duration, duration);
        Duration durationType = currentSchedule != null ? currentSchedule.getDurationType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_duration_type, durationType);
        // Frequency
        int frequency = currentSchedule != null ? currentSchedule.getFrequency() : 1;
        createDurationNumberPicker(R.id.manage_schedule_frequency, frequency);
        Duration frequencyType = currentSchedule != null ? currentSchedule.getFrequencyType() : Duration.DAY;
        createDurationSpinner(R.id.manage_schedule_frequency_type, frequencyType);
    }

    private void readManageScheduleForm() {
        currentSchedule = new MobileScheduleDto();
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
        // Duration
        NumberPicker duration = (NumberPicker) findViewById(R.id.manage_schedule_duration);
        currentSchedule.setDuration(duration.getValue());
        Spinner durationType = (Spinner) findViewById(R.id.manage_schedule_duration_type);
        currentSchedule.setDurationType(Duration.valueOf(durationType.getSelectedItem().toString()));
        // Frequency
        NumberPicker frequency = (NumberPicker) findViewById(R.id.manage_schedule_frequency);
        currentSchedule.setFrequency(frequency.getValue());
        Spinner frequencyType = (Spinner) findViewById(R.id.manage_schedule_duration_type);
        currentSchedule.setFrequencyType(Duration.valueOf(frequencyType.getSelectedItem().toString()));
    }

    private void createDurationNumberPicker(int id, int value) {
        NumberPicker duration = (NumberPicker) findViewById(id);
        duration.setMinValue(1);
        duration.setMaxValue(60);
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
        Bundle resultData = new Bundle();
        resultData.putSerializable(SchedulesFragment.RESULT_EXTRA, currentSchedule);
        Intent intent = new Intent();
        intent.putExtras(resultData);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ManageSchedule extends AsyncTask<String, String, ResultDto> {

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected ResultDto doInBackground(String... params) {
            HttpClient client = MainActivity.getHttpClient();
            return client.post(params[0], currentSchedule, ResultDto.class);
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(ResultDto result) {
            if (result.isSuccess()) {
                currentSchedule.setId(result.getId());
                assembleResponseAndFinish();
            } else {
                MainActivity.createErrorDialog(ManageScheduleActivity.this, result.getError());
            }
        }
    }
}