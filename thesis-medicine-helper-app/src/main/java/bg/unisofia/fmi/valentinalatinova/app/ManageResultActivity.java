package bg.unisofia.fmi.valentinalatinova.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

public class ManageResultActivity extends Activity {

    private MobileResults currentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_result);
        registerOnClickListeners();
    }

    // Private methods
    private void registerOnClickListeners() {
        // Save button
        Button save = (Button) findViewById(R.id.manage_result_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentResult = new MobileResults();
                EditText name = (EditText) findViewById(R.id.manage_result_name);
                currentResult.setName(name.getText().toString());
                EditText units = (EditText) findViewById(R.id.manage_result_units);
                currentResult.setUnits(units.getText().toString());
                new CreateResult().execute();
            }
        });
        // Cancel button
        Button cancel = (Button) findViewById(R.id.manage_result_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void assembleResponseAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MOBILE_RESULTS, currentResult);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class CreateResult extends AsyncTask<String, String, Result> {

        private static final String PATH_CREATE_RESULT = "/mobile/results/save";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected Result doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.post(PATH_CREATE_RESULT, currentResult, Result.class);
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
                    currentResult.setId(result.getId());
                    assembleResponseAndFinish();
                } else {
                    MainActivity.createErrorDialog(ManageResultActivity.this, result.getError());
                }
            }
        }
    }
}
