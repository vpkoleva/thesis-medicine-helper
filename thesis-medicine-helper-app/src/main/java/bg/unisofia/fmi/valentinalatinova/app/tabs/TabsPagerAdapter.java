package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final int TABS_COUNT = 2;

    public TabsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Schedules fragment activity
                return new SchedulesFragment();
            case 1:
                // Results fragment activity
                return new ResultsFragment();
            default:
                // Invalid fragment
                return null;
        }
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }
}
