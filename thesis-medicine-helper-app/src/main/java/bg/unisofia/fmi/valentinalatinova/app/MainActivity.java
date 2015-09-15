package bg.unisofia.fmi.valentinalatinova.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.tabs.TabsPagerAdapter;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int RESULT_SETTINGS = 1;
    private String settingsUrl = null;
    private boolean settingsAcceptAll = true;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Schedules", "Tables"};

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reloadSettings();

        // Initialisation
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
    }

    /**
     * Create standard options menu. Currently there is only Settings option in it.
     *
     * @param menu the menu where items are placed
     * @return
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
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, RESULT_SETTINGS);
                break;
        }
        return true;
    }

    /**
     * Called when launched activity exits giving the code you started the activity with.
     *
     * @param requestCode code supplied to startActivityForResult() method
     * @param resultCode result code returned from activity
     * @param data data that is tranferred from exitting activity to current activity
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

    /**
     * Called when tab is selected.
     *
     * @param tab
     * @param fragmentTransaction
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // On tab selected show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Called when tab is exists selected state.
     *
     * @param tab
     * @param fragmentTransaction
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * Called when type is already selected and then selected again.
     *
     * @param tab
     * @param fragmentTransaction
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public void doGetSchedules(View view) {
        final String urlString = settingsUrl + "/mobile/schedule/all";
        new CallAPI().execute(urlString);
    }

    private void reloadSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        settingsUrl = sharedPref.getString(SettingsActivity.KEY_URL, null);
        settingsAcceptAll = sharedPref.getBoolean(SettingsActivity.KEY_ACCEPT_ALL_CERTIFICATES, true);
        // If defaults are not read, take them from string.xml
        if (settingsUrl == null) {
            settingsUrl = this.getString(R.string.settings_url_default);
            settingsAcceptAll = Boolean.valueOf(this.getString(R.string.settings_accept_all_certificates));
        }
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
