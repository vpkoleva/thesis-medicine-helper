package bg.unisofia.fmi.valentinalatinova.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import bg.unisofia.fmi.valentinalatinova.app.tabs.TabsPagerAdapter;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final int RESULT_LOGIN = 1;
    private static HttpClient httpClient;
    private ViewPager viewPager;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState previous state if such exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLoginActivity();
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
            case RESULT_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle result = data.getExtras();
                    httpClient = (HttpClient) result.getSerializable(LoginActivity.RESULT_EXTRA);
                    initialiseTabs();
                } else {
                    finish();
                }
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

    // Static methods
    public static HttpClient getAuthenticatedHttpClient() {
        if (!httpClient.isAuthenticated()) {
            httpClient.authenticate();
        }
        return httpClient;
    }

    public static void createErrorDialog(final Context context, String message) {
        AlertDialog.Builder error = new AlertDialog.Builder(context);
        error.setTitle(R.string.error_name);
        error.setMessage(message);
        error.setPositiveButton(R.string.error_close, null);
        error.setCancelable(true);
        error.create().show();
    }

    // Private methods
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("", httpClient);
        startActivityForResult(intent, RESULT_LOGIN);
    }

    private void initialiseTabs() {
        // Initialisation
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity_pager);
        viewPager.setAdapter(mAdapter);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        String[] tabs = {getString(R.string.tab_schedules), getString(R.string.tab_results)};
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
    }
}
