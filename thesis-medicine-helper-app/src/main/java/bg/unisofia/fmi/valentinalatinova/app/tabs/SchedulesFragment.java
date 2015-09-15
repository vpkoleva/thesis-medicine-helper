package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bg.unisofia.fmi.valentinalatinova.app.R;

public class SchedulesFragment extends Fragment {

    /**
     * Instantiates fragment user interface.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
        return rootView;
    }

    /**
     * Workaround to make Setting menu not crash the fragment.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}
