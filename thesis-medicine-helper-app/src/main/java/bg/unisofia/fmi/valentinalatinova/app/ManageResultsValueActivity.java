package bg.unisofia.fmi.valentinalatinova.app;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

public class ManageResultsValueActivity extends Activity {

    private static final String PATH_SAVE_VALUE = "/mobile/results/value/save";
    private static final String PATH_UPDATE_VALUE = "/mobile/results/value/update";

    private MobileResultsValue currentValue;
    private long currentResultsId = -1;
    private long currentValueId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_results_value);
        // Get results value object
        Intent intent = getIntent();
        currentValue = (MobileResultsValue) intent.getSerializableExtra(Constants.MOBILE_RESULTS_VALUE);
        // If results value is passed then this is an Edit Action
        if (currentValue != null) {
            currentValueId = currentValue.getId();
            currentResultsId = currentValue.getResultsId();
            setTitle(R.string.manage_results_value_edit_window);
        } else {
            // Added as Extra only on Add, not on Edit
            currentResultsId = intent.getLongExtra(Constants.MOBILE_RESULTS_ID, -1);
            setTitle(R.string.manage_results_value_add_window);
        }
        // Fill form with existing data
        initialiseManageResultsValueForm();
        registerOnClickListeners();
    }

    // Private methods
    private void registerOnClickListeners() {
        // Save button
        Button save = (Button) findViewById(R.id.manage_results_value_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readManageResultsValueForm();
                // If some ID has been captured then this is an Edit Action
                if (currentValueId > 0) {
                    currentValue.setId(currentValueId);
                    new ManageResultsValue().execute(PATH_UPDATE_VALUE);
                } else {
                    new ManageResultsValue().execute(PATH_SAVE_VALUE);
                }
            }
        });
        // Cancel button
        Button cancel = (Button) findViewById(R.id.manage_results_value_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initialiseManageResultsValueForm() {
        // Date
        DateTime dateTime = currentValue != null ? currentValue.getMeasurementDate() : DateTime.now();
        DatePicker date = (DatePicker) findViewById(R.id.manage_results_value_date);
        int year = dateTime.getYear();
        // Calendar month starts from 0
        int month = dateTime.getMonthOfYear() - 1;
        int day = dateTime.getDayOfMonth();
        date.init(year, month, day, null);
        // Time
        TimePicker time = (TimePicker) findViewById(R.id.manage_results_value_time);
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour();
        time.setIs24HourView(true);
        time.setCurrentHour(hour);
        time.setCurrentMinute(minute);
        // Measurement
        String measurementText = currentValue != null ? currentValue.getMeasurement() : "";
        EditText description = (EditText) findViewById(R.id.manage_results_value_measurement);
        description.setText(measurementText);
    }

    private void readManageResultsValueForm() {
        currentValue = new MobileResultsValue();
        currentValue.setResultsId(currentResultsId);
        // Date
        DatePicker date = (DatePicker) findViewById(R.id.manage_results_value_date);
        int year = date.getYear();
        // Calendar month starts from 0
        int month = date.getMonth() + 1;
        int day = date.getDayOfMonth();
        // Time
        TimePicker time = (TimePicker) findViewById(R.id.manage_results_value_time);
        int hour = time.getCurrentHour();
        int minute = time.getCurrentMinute();
        DateTime dateTime = new DateTime(year, month, day, hour, minute);
        currentValue.setMeasurementDate(dateTime);
        // Measurement
        EditText measurement = (EditText) findViewById(R.id.manage_results_value_measurement);
        currentValue.setMeasurement(measurement.getText().toString());
    }

    private void assembleResponseAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MOBILE_RESULTS_VALUE, currentValue);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class ManageResultsValue extends AsyncTask<String, String, Result> {

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected Result doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.post(params[0], currentValue, Result.class);
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
                    currentValue.setId(result.getId());
                    assembleResponseAndFinish();
                } else {
                    MainActivity.createErrorDialog(ManageResultsValueActivity.this, result.getError());
                }
            }
        }
    }
}
