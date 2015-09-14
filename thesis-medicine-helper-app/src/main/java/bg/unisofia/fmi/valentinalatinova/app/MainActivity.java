package bg.unisofia.fmi.valentinalatinova.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    public final static String API_RESULT = "API_RESULT";
    private static final int RESULT_SETTINGS = 1;
    private static String settingsUrl = null;
    private static boolean settingsAcceptAllSsl = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reloadSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RESULT_SETTINGS);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SETTINGS:
                reloadSettings();
                break;
        }
    }

    public void doGetCall(View view) {
        final String urlString = settingsUrl + "/mobile/schedule/all";
        new CallAPI().execute(urlString);
    }

    private void reloadSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        settingsUrl = sharedPref.getString(SettingsActivity.KEY_URL, "");
        settingsAcceptAllSsl = sharedPref.getBoolean(SettingsActivity.KEY_ACCEPT_ALL_CERTIFICATES, true);
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String resultToDisplay = "";
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                String authorization = "Bearer 66408bd9-2bc0-40c3-9823-e9bec390532a";
                urlConnection.setRequestProperty("Authorization", authorization);
                InputStream resp = urlConnection.getInputStream();
                InputStreamReader is = new InputStreamReader(resp);
                BufferedReader br = new BufferedReader(is);
                String read = null;
                StringBuffer sb = new StringBuffer();
                while ((read = br.readLine()) != null) {
                    sb.append(read);
                }
                resultToDisplay = sb.toString();
            } catch (SSLException ssle) {
                resultToDisplay = "Error: " + ssle.getLocalizedMessage();
            } catch (IOException ioe) {
                resultToDisplay = "Error: " + ioe.getLocalizedMessage();
            }
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.main_result);
            textView.setTextSize(14);
            textView.setText(result);
        }
    }
}
