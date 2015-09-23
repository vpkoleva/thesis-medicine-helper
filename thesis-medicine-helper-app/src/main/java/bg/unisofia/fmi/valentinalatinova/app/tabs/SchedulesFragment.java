package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.HttpClient;
import bg.unisofia.fmi.valentinalatinova.app.Logger;
import bg.unisofia.fmi.valentinalatinova.app.MainActivity;
import bg.unisofia.fmi.valentinalatinova.app.R;
import bg.unisofia.fmi.valentinalatinova.core.dto.MobileScheduleDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.ResultDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchedulesFragment extends Fragment {

    private final int MENU_GROUP_ID = 101;
    private final int MENU_EDIT_ID = 1;
    private final int MENU_DELETE_ID = 2;
    private View rootView;
    private TableLayout schedulesTable;
    private MobileScheduleDto selectedSchedule;
    private List<MobileScheduleDto> allSchedules;

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
        schedulesTable = (TableLayout) rootView.findViewById(R.id.schedules_table);
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
        MobileScheduleDto schedule = (MobileScheduleDto) view.getTag();
        selectedSchedule = schedule;
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
                    Logger.debug("Edit: " + selectedSchedule.getId());
                    break;
                case MENU_DELETE_ID:
                    registerOnDeleteConfirmationDialog();
                    break;
            }
        }
        return true;
    }

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
                Logger.debug("Add schedule");
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
                        new DeleteSchedule().execute(selectedSchedule.getId());
                    }
                })
                .setNegativeButton(R.string.schedules_dialog_no, null)
                .show();
    }

    private void drawSchedulesTable() {
        schedulesTable.removeAllViews();
        if (allSchedules != null && allSchedules.size() > 0) {
            for (MobileScheduleDto result : allSchedules) {
                TableRow row = generateTableRow(result);
                // Add Date
                TextView date = generateTextView(result.getStartDate().toString());
                row.addView(date);
                // Add Description
                TextView description = generateTextView(result.getDescription());
                row.addView(description);
                // Add Duration
                TextView duration = generateTextView(result.getDuration() + result.getDurationType().toString());
                row.addView(duration);
                // Add Frequency
                TextView frequency = generateTextView(result.getFrequency() + result.getFrequencyType().toString());
                row.addView(frequency);
                schedulesTable.addView(row);
            }
        } else {
            TableRow row = generateTableRow(null);
            TextView message = generateTextView("No Schedules available");
            row.addView(message);
            schedulesTable.addView(row);
        }
    }

    private TableRow generateTableRow(MobileScheduleDto schedule) {
        TableRow row = new TableRow(rootView.getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        if (schedule != null) {
            row.setTag(schedule);
            registerForContextMenu(row);
        }
        return row;
    }

    private TextView generateTextView(String content) {
        TextView cell = new TextView(rootView.getContext());
        cell.setText(content);
        return cell;
    }

    private class GetSchedules extends AsyncTask<String, String, List<MobileScheduleDto>> {

        private final String PATH_SCHEDULES = "/mobile/schedule/all";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected List<MobileScheduleDto> doInBackground(String... params) {
            HttpClient client = ((MainActivity) getActivity()).getHttpClient();
            MobileScheduleDto[] schedulesArray = client.get(PATH_SCHEDULES, MobileScheduleDto[].class);
            List<MobileScheduleDto> result = new ArrayList<>();
            Collections.addAll(result, schedulesArray);
            return result;
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(List<MobileScheduleDto> result) {
            allSchedules = result;
            drawSchedulesTable();
        }
    }

    private class DeleteSchedule extends AsyncTask<Long, String, ResultDto> {

        private final String PATH_DELETE = "/mobile/schedule/delete/";

        @Override
        protected ResultDto doInBackground(Long... params) {
            HttpClient client = ((MainActivity) getActivity()).getHttpClient();
            return client.get(PATH_DELETE + params[0], ResultDto.class);
        }

        @Override
        protected void onPostExecute(ResultDto result) {
            if (result != null) {
                if (result.isSuccess()) {
                    allSchedules.remove(selectedSchedule);
                    drawSchedulesTable();
                } else {
                    ((MainActivity) getActivity()).createErrorDialog(result.getError());
                }
            }
        }
    }
}
