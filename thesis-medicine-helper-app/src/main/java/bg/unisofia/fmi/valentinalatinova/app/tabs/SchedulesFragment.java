package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

public class SchedulesFragment extends Fragment {

    private final String PATH_SCHEDULES = "/mobile/schedule/all";
    private View rootView;
    private TableLayout schedulesTable;

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
        rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
        schedulesTable = (TableLayout) rootView.findViewById(R.id.schedules_table);
        registerOnClickListenerButtonRefresh();
        invokeGetSchedules();
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

    private void invokeGetSchedules() {
        new GetSchedules().execute(PATH_SCHEDULES);
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

    private void drawTable(MobileScheduleDto[] results) {
        schedulesTable.removeAllViews();
        if (results != null && results.length > 0) {
            for (MobileScheduleDto result : results) {
                TableRow row = generateTableRow(result.getId());
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
            TableRow row = generateTableRow(0);
            TextView message = generateTextView("No Schedules available");
            row.addView(message);
            schedulesTable.addView(row);
        }
    }

    private TableRow generateTableRow(long id) {
        TableRow row = new TableRow(rootView.getContext());
        row.setTag(id);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        row.setClickable(true);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow currentRow = (TableRow) view;
                Logger.debug("Clicked: " + currentRow.getTag());
            }
        });
        return row;
    }

    private TextView generateTextView(String content) {
        TextView cell = new TextView(rootView.getContext());
        cell.setText(content);
        return cell;
    }

    private class GetSchedules extends AsyncTask<String, String, MobileScheduleDto[]> {

        @Override
        protected MobileScheduleDto[] doInBackground(String... params) {
            String path = params[0];
            HttpClient client = ((MainActivity) getActivity()).getHttpClient();
            MobileScheduleDto[] result = client.get(path, MobileScheduleDto[].class);
            return result;
        }

        @Override
        protected void onPostExecute(MobileScheduleDto[] result) {
            drawTable(result);
        }
    }
}
