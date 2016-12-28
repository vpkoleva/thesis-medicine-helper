package bg.unisofia.fmi.valentinalatinova.app.tabs;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.AlarmReceiver;
import bg.unisofia.fmi.valentinalatinova.app.MainActivity;
import bg.unisofia.fmi.valentinalatinova.app.ManageScheduleActivity;
import bg.unisofia.fmi.valentinalatinova.app.R;
import bg.unisofia.fmi.valentinalatinova.app.utils.Constants;
import bg.unisofia.fmi.valentinalatinova.app.utils.DateUtils;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.app.utils.Logger;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;
import bg.unisofia.fmi.valentinalatinova.core.json.ScheduleInfo;

public class SchedulesFragment extends CustomFragment {

    private final int MENU_GROUP_ID = 101;
    private final int MENU_EDIT_ID = 111;
    private final int MENU_DELETE_ID = 112;
    private final int RESULT_ADD = 121;
    private final int RESULT_EDIT = 122;
    private CaldroidFragment schedulesCalendar;
    private ScheduleInfo currentSchedule;
    private List<ScheduleInfo> allSchedules;
    private List<Date> datesWithSchedules = new ArrayList<>();
    private AlarmManager alarmManager;
    private List<PendingIntent> alarmPendingIntents = new ArrayList<>();

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
        alarmManager = (AlarmManager) rootView.getContext().getSystemService(Context.ALARM_SERVICE);
        invokeGetSchedules();
        initialiseCalendar();
        registerOnClickListeners();
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
        ScheduleInfo schedule = (ScheduleInfo) view.getTag();
        currentSchedule = schedule;
        menu.setHeaderTitle(schedule.getTitle());
        menu.add(MENU_GROUP_ID, MENU_EDIT_ID, MENU_EDIT_ID, R.string.menu_edit);
        menu.add(MENU_GROUP_ID, MENU_DELETE_ID, MENU_DELETE_ID, R.string.menu_delete);
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
                    intent.putExtra(Constants.SCHEDULE_ID, currentSchedule.getId());
                    startActivityForResult(intent, RESULT_EDIT);
                    break;
                case MENU_DELETE_ID:
                    generateDeleteConfirmationDialog(currentSchedule.getTitle(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteSchedule().execute(currentSchedule.getId());
                                }
                            });
                    break;
            }
        }
        return false;
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
            invokeGetSchedules();
        }
    }

    // Private methods
    private void invokeGetSchedules() {
        new GetSchedules().execute();
    }

    private void registerOnClickListeners() {
        // Refresh button
        Button refresh = (Button) rootView.findViewById(R.id.schedules_button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeGetSchedules();
            }
        });
        // Add button
        Button add = (Button) rootView.findViewById(R.id.schedules_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageScheduleActivity.class);
                startActivityForResult(intent, RESULT_ADD);
            }
        });
    }

    private void initialiseCalendar() {
        Logger.debug(SchedulesFragment.class, "Initialise calendar");
        schedulesCalendar = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar today = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, today.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, today.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        schedulesCalendar.setArguments(args);
        schedulesCalendar.setCaldroidListener(calendarListener);
        schedulesCalendar.setTextColorForDate(R.color.blue, today.getTime());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.schedules_calendar, schedulesCalendar);
        transaction.commit();
    }

    private void refreshCalendar() {
        // Clear previously selected dates
        schedulesCalendar.clearBackgroundResourceForDates(datesWithSchedules);
        schedulesCalendar.refreshView();
        // Iterate current dates
        if (allSchedules != null && allSchedules.size() > 0) {
            // Initialise empty array
            datesWithSchedules = new ArrayList<>();
            // Iterate current dates
            for (ScheduleInfo schedule : allSchedules) {
                Date date = schedule.getStart().toDate();
                // Save date with schedule
                datesWithSchedules.add(date);
                schedulesCalendar.setBackgroundResourceForDate(R.color.gray, date);
            }
            // Refresh view
            schedulesCalendar.refreshView();
        }
    }

    private List<ScheduleInfo> getSchedulesByDate(Date date) {
        List<ScheduleInfo> result = new ArrayList<>();
        for (ScheduleInfo schedule : allSchedules) {
            if (DateUtils.isSameDay(date, schedule.getStart().toDate())) {
                result.add(schedule);
            }
        }
        Collections.sort(result, new Comparator<ScheduleInfo>() {
            @Override
            public int compare(ScheduleInfo lhs, ScheduleInfo rhs) {
                return ((Long) lhs.getStart().getMillis()).compareTo(rhs.getStart().getMillis());
            }
        });
        return result;
    }

    private void drawSchedulesTable(List<ScheduleInfo> schedules) {
        // Clear Schedules table
        TextView label = (TextView) rootView.findViewById(R.id.schedules_view_for);
        label.setVisibility(TextView.INVISIBLE);
        TableLayout schedulesTable = (TableLayout) rootView.findViewById(R.id.schedules_list);
        schedulesTable.removeAllViews();

        // Draw Schedules table
        if (schedules != null && schedules.size() > 0) {
            // Generate header
            label.setVisibility(TextView.VISIBLE);
            String format = getString(R.string.schedules_view_for);
            String text = String.format(format, DateUtils
                    .formatDay(schedules.get(0).getStart().withZone(DateTimeZone.getDefault()).toDate()));
            label.setText(text);
            // Generate records
            for (ScheduleInfo result : schedules) {
                TableRow row = generateTableRow(result);
                // Add hour
                TextView date = generateTextView(DateUtils
                        .formatTime(result.getStart().withZone(DateTimeZone.getDefault())));
                row.addView(date);
                // Add separator
                TextView separator = generateTableSeparator();
                row.addView(separator);
                // Add description
                TextView description = generateTextView(result.getTitle());
                row.addView(description);
                // Add row
                schedulesTable.addView(row);
            }
        }
    }

    /**
     * Implements CaldroidListener abstract class to handles calendar events.
     */
    private final CaldroidListener calendarListener = new CaldroidListener() {
        private Date lastClicked = Calendar.getInstance().getTime();

        @Override
        public void onSelectDate(final Date date, View view) {
            drawSchedulesTable(getSchedulesByDate(date));
            schedulesCalendar.clearTextColorForDate(lastClicked);
            schedulesCalendar.setTextColorForDate(R.color.blue, date);
            schedulesCalendar.refreshView();
            lastClicked = date;
        }
    };

    private class GetSchedules extends AsyncTask<String, String, List<ScheduleInfo>> {

        private static final String PATH_SCHEDULES = "/mobile/schedule/all";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected List<ScheduleInfo> doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            ScheduleInfo[] schedulesArray = client.get(PATH_SCHEDULES, ScheduleInfo[].class);
            List<ScheduleInfo> result = new ArrayList<>();
            if (schedulesArray != null) {
                Collections.addAll(result, schedulesArray);
                Logger.debug(SchedulesFragment.class, "Get schedules: " + result.size());
                setAlarms(result);
            }
            return result;
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(List<ScheduleInfo> result) {
            allSchedules = result;
            refreshCalendar();
            drawSchedulesTable(getSchedulesByDate(Calendar.getInstance().getTime()));
        }

        private void setAlarms(List<ScheduleInfo> schedules) {
            // Cancel all exiting
            for (PendingIntent pendingIntent : alarmPendingIntents) {
                alarmManager.cancel(pendingIntent);
            }

            alarmPendingIntents = new ArrayList<>();
            for (ScheduleInfo schedule : schedules) {
                DateTime runTime = schedule.getStart().withZone(DateTimeZone.getDefault());
                if (runTime.isAfterNow()) {
                    Intent intent = new Intent(rootView.getContext(), AlarmReceiver.class);
                    intent.putExtra(Constants.ALARM_SCHEDULE, schedule);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(rootView.getContext(),
                            schedule.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    Logger.debug(SchedulesFragment.class, "Schedule '" + schedule.getTitle()
                            + "' with id '" + schedule.getId() + "' will start once at '" + runTime);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, runTime.getMillis(), pendingIntent);
                    alarmPendingIntents.add(pendingIntent);
                }
            }
        }
    }

    private class DeleteSchedule extends AsyncTask<Long, String, Result> {

        private static final String PATH_DELETE = "/mobile/schedule/delete/";

        @Override
        protected Result doInBackground(Long... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.delete(PATH_DELETE + params[0], Result.class);
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null) {
                if (result.isSuccess()) {
                    Logger.debug(SchedulesFragment.class, "Deleted schedule: " + result.getId());
                    invokeGetSchedules();
                } else {
                    MainActivity.createErrorDialog(rootView.getContext(), result.getError());
                }
            }
        }
    }
}
