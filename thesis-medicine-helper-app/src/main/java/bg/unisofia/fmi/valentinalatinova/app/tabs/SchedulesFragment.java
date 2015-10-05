package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import bg.unisofia.fmi.valentinalatinova.app.HttpClient;
import bg.unisofia.fmi.valentinalatinova.app.Logger;
import bg.unisofia.fmi.valentinalatinova.app.MainActivity;
import bg.unisofia.fmi.valentinalatinova.app.ManageScheduleActivity;
import bg.unisofia.fmi.valentinalatinova.app.R;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileSchedule;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SchedulesFragment extends Fragment {

    public static final String RESULT_EXTRA = "MobileSchedule";
    private final int MENU_GROUP_ID = 101;
    private final int MENU_EDIT_ID = 111;
    private final int MENU_DELETE_ID = 112;
    private final int RESULT_ADD = 121;
    private final int RESULT_EDIT = 122;
    private View rootView;
    private CaldroidFragment schedulesCalendar;
    private MobileSchedule currentSchedule;
    private List<MobileSchedule> allSchedules;

    /**
     * Instantiates fragment user interface.
     *
     * @param inflater object used to inflate any views in the fragment
     * @param container parent to which fragment is attached to
     * @param savedInstanceState previous states
     * @return return fragment UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
        initialiseCalendar();
        registerOnClickListenerButtonRefresh();
        registerOnClickListenerButtonAdd();
        invokeGetSchedules();
        return rootView;
    }

    /**
     * Called to save fragment state. Workaround to make Setting menu not crash the fragment.
     *
     * @param outState bundle to save state in
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    /**
     * Called when context menu is to be shown.
     *
     * @param menu context menu being built
     * @param view view to which menu belongs to
     * @param menuInfo extra information about the view
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MobileSchedule schedule = (MobileSchedule) view.getTag();
        currentSchedule = schedule;
        menu.setHeaderTitle(schedule.getDescription());
        menu.add(MENU_GROUP_ID, MENU_EDIT_ID, MENU_EDIT_ID, R.string.schedules_context_edit);
        menu.add(MENU_GROUP_ID, MENU_DELETE_ID, MENU_DELETE_ID, R.string.schedules_context_delete);
    }

    /**
     * Called when item from context menu is selected.
     *
     * @param item selected item
     * @return FALSE for normal processing, TRUE to consume menu action here
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (MENU_GROUP_ID == item.getGroupId()) {
            switch (item.getItemId()) {
                case MENU_EDIT_ID:
                    Intent intent = new Intent(rootView.getContext(), ManageScheduleActivity.class);
                    intent.putExtra(RESULT_EXTRA, currentSchedule);
                    startActivityForResult(intent, RESULT_EDIT);
                    break;
                case MENU_DELETE_ID:
                    registerOnDeleteConfirmationDialog();
                    break;
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // In case of Add or Edit Schedule request and OK result
        if ((requestCode == RESULT_ADD || requestCode == RESULT_EDIT) && resultCode == Activity.RESULT_OK) {
            Bundle result = data.getExtras();
            currentSchedule = (MobileSchedule) result.getSerializable(RESULT_EXTRA);
            // Remove before add in case of Edit Schedule
            if (requestCode == RESULT_EDIT) {
                allSchedules.remove(currentSchedule);
            }
            allSchedules.add(currentSchedule);
            refreshCalendar();
        }
    }

    // Private methods
    private void invokeGetSchedules() {
        new GetSchedules().execute();
    }

    private void registerOnClickListenerButtonRefresh() {
        Button button = (Button) rootView.findViewById(R.id.schedules_button_refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeGetSchedules();
            }
        });
    }

    private void registerOnClickListenerButtonAdd() {
        Button button = (Button) rootView.findViewById(R.id.schedules_button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageScheduleActivity.class);
                startActivityForResult(intent, RESULT_ADD);
            }
        });
    }

    private void registerOnDeleteConfirmationDialog() {
        new AlertDialog.Builder(rootView.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.schedules_dialog_name)
                .setMessage(R.string.schedules_dialog_text)
                .setPositiveButton(R.string.schedules_dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteSchedule().execute(currentSchedule.getId());
                    }
                })
                .setNegativeButton(R.string.schedules_dialog_no, null)
                .show();
    }

    private void initialiseCalendar() {
        schedulesCalendar = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar today = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, today.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, today.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        schedulesCalendar.setArguments(args);
        schedulesCalendar.setCaldroidListener(calendarListener);
        schedulesCalendar.setBackgroundResourceForDate(R.drawable.today, today.getTime());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.schedules_calendar, schedulesCalendar);
        transaction.commit();
    }

    private void refreshCalendar() {
        if (allSchedules != null && allSchedules.size() > 0) {
            for (MobileSchedule schedule : allSchedules) {
                Date date = schedule.getStartDate().toDate();
                if (isToday(date)) {
                    schedulesCalendar.setBackgroundResourceForDate(R.drawable.today_circle, date);
                } else {
                    schedulesCalendar.setBackgroundResourceForDate(R.drawable.circle, date);
                }
            }
            schedulesCalendar.refreshView();
        }
    }

    private boolean isToday(Date date) {
        Date today = Calendar.getInstance().getTime();
        int todayInt = today.getYear() + today.getMonth() + today.getDay();
        int dateInt = date.getYear() + date.getMonth() + date.getDay();
        return dateInt == todayInt;
    }

    private final CaldroidListener calendarListener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
        }

        @Override
        public void onChangeMonth(int month, int year) {
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            Logger.debug("Date clicked" + date.toString());
        }

        @Override
        public void onCaldroidViewCreated() {
        }
    };

    private class GetSchedules extends AsyncTask<String, String, List<MobileSchedule>> {

        private final String PATH_SCHEDULES = "/mobile/schedule/all";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected List<MobileSchedule> doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            MobileSchedule[] schedulesArray = client.get(PATH_SCHEDULES, MobileSchedule[].class);
            List<MobileSchedule> result = new ArrayList<>();
            if (schedulesArray != null) {
                Collections.addAll(result, schedulesArray);
            }
            return result;
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(List<MobileSchedule> result) {
            allSchedules = result;
            refreshCalendar();
        }
    }

    private class DeleteSchedule extends AsyncTask<Long, String, Result> {

        private final String PATH_DELETE = "/mobile/schedule/delete/";

        @Override
        protected Result doInBackground(Long... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.get(PATH_DELETE + params[0], Result.class);
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null) {
                if (result.isSuccess()) {
                    allSchedules.remove(currentSchedule);
                    refreshCalendar();
                } else {
                    MainActivity.createErrorDialog(rootView.getContext(), result.getError());
                }
            }
        }
    }
}
