package bg.unisofia.fmi.valentinalatinova.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    public static final String RESULT_EXTRA = "HttpClient";
    public static final String KEY_USERNAME = "settings_username";
    private static final int RESULT_SETTINGS = 10;
    private static HttpClient httpClient;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState previous state if such exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        httpClient = new HttpClient();
        reloadSettings();
        registerOnClickListenerButtonLogin();
    }

    private void registerOnClickListenerButtonLogin() {
        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAuthToken().execute();
            }
        });
    }

    /**
     * Create standard options menu. Currently there is only Settings option in it.
     *
     * @param menu the menu where items are placed
     * @return TRUE if menu is to be displayed, in case of FALSE menu will not be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    /**
     * Called when some item from menu is called. Currently only Settings is present.
     *
     * @param item selected menu item
     * @return FALSE for normal processing, TRUE to consume menu action here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.menu_settings == item.getItemId()) {
            startSettingsActivity(this);
        }
        return true;
    }

    /**
     * Called when launched activity exits giving the code you started the activity with.
     *
     * @param requestCode code supplied to startActivityForResult() method
     * @param resultCode result code returned from activity
     * @param data data that is transferred from exiting activity to current activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SETTINGS:
                reloadSettings();
                break;
        }
    }

    public static void startSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        ((Activity) context).startActivityForResult(intent, RESULT_SETTINGS);
    }

    private void reloadSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String settingsUrl = sharedPref.getString(SettingsActivity.KEY_URL, null);
        boolean settingsAcceptAll = sharedPref.getBoolean(SettingsActivity.KEY_ACCEPT_ALL_CERTIFICATES, true);
        String username = sharedPref.getString(KEY_USERNAME, "");
        // If connection defaults are not read, take them from string.xml
        if (settingsUrl == null) {
            settingsUrl = this.getString(R.string.settings_url_default);
            settingsAcceptAll = Boolean.valueOf(this.getString(R.string.settings_accept_all_certificates));
        }
        httpClient.setEndpointUrl(settingsUrl);
        httpClient.setAcceptAllCertificates(settingsAcceptAll);
        httpClient.setUsername(username);
    }

    private class GetAuthToken extends AsyncTask<String, String, HttpClient> {

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected HttpClient doInBackground(String... params) {
            EditText username = (EditText) findViewById(R.id.login_username);
            String user = username.getText().toString();
            EditText password = (EditText) findViewById(R.id.login_password);
            String pass = password.getText().toString();
            httpClient.setUsername(user);
            httpClient.setPassword(pass);
            boolean result = httpClient.authenticate();
            if (result) {
                return httpClient;
            } else {
                return null;
            }
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(HttpClient result) {
            if (result != null) {
                Bundle resultData = new Bundle();
                resultData.putSerializable(RESULT_EXTRA, result);
                Intent intent = new Intent();
                intent.putExtras(resultData);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                TextView login = (TextView) findViewById(R.id.login_error);
                login.setText(R.string.login_error);
            }
        }
    }
}