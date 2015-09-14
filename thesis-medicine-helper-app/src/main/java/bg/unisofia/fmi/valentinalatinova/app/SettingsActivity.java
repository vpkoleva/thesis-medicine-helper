package bg.unisofia.fmi.valentinalatinova.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_URL = "settings_url";
    public static final String KEY_ACCEPT_ALL_CERTIFICATES = "settings_accept_all_certificates";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}