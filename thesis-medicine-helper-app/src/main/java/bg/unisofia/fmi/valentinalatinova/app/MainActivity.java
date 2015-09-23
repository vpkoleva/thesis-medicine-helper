package bg.unisofia.fmi.valentinalatinova.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import bg.unisofia.fmi.valentinalatinova.app.tabs.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int RESULT_SETTINGS = 1;
    private HttpClient httpClient;
    private ViewPager viewPager;
    private String[] tabs = {"Schedules", "Tables"};

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState previous state if such exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseTabs();
        httpClient = new HttpClient();
        reloadSettings();
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

    /**
     * Called when tab is selected.
     *
     * @param tab tab that was selected
     * @param fragmentTransaction for queueing fragment operations
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // On tab selected show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Called when tab is exists selected state.
     *
     * @param tab tab that was unselected
     * @param fragmentTransaction for queueing fragment operations
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * Called when type is already selected and then selected again.
     *
     * @param tab tab that was reselected
     * @param fragmentTransaction for queueing fragment operations
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void createErrorDialog(String message) {
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle(R.string.error_name);
        error.setMessage(message);
        error.setPositiveButton(R.string.error_close, null);
        error.setCancelable(true);
        error.create().show();
    }

    private void initialiseTabs() {
        // Initialisation
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mAdapter);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
    }

    private void reloadSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String settingsUrl = sharedPref.getString(SettingsActivity.KEY_URL, null);
        boolean settingsAcceptAll = sharedPref.getBoolean(SettingsActivity.KEY_ACCEPT_ALL_CERTIFICATES, true);
        // If defaults are not read, take them from string.xml
        if (settingsUrl == null) {
            settingsUrl = this.getString(R.string.settings_url_default);
            settingsAcceptAll = Boolean.valueOf(this.getString(R.string.settings_accept_all_certificates));
        }
        httpClient.setEndpointUrl(settingsUrl);
        httpClient.setAcceptAllCertificates(settingsAcceptAll);
    }
}
