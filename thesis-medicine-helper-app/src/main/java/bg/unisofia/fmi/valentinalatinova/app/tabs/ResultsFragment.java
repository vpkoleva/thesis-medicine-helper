package bg.unisofia.fmi.valentinalatinova.app.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTimeZone;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.MainActivity;
import bg.unisofia.fmi.valentinalatinova.app.ManageResultActivity;
import bg.unisofia.fmi.valentinalatinova.app.ManageResultsValueActivity;
import bg.unisofia.fmi.valentinalatinova.app.R;
import bg.unisofia.fmi.valentinalatinova.app.utils.DateUtils;
import bg.unisofia.fmi.valentinalatinova.app.utils.HttpClient;
import bg.unisofia.fmi.valentinalatinova.app.utils.Logger;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;
import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;
import bg.unisofia.fmi.valentinalatinova.core.json.Result;

public class ResultsFragment extends CustomFragment {

    private final int MENU_GROUP_ID = 201;
    private final int MENU_EDIT_ID = 211;
    private final int MENU_DELETE_ID = 212;
    private final int RESULT_ADD = 221;
    private final int RESULT_VALUE_ADD = 231;
    private final int RESULT_VALUE_EDIT = 232;
    private List<MobileResults> allResults;
    private MobileResults currentResult;
    private MobileResultsValue currentResultValue;

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
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        toggleNavigationButtons(false);
        registerOnClickListenersNavigationButtons();
        registerOnClickListenersManageResultsButtons();
        registerOnClickListenersAddResultsValueButton();
        invokeGetAlResults();
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
        final MobileResultsValue value = (MobileResultsValue) view.getTag();
        // Create MobileResults to use it in indexOf() since equals() depend on 'id' only
        final MobileResults result = new MobileResults();
        result.setId(value.getResultsId());
        currentResultValue = value;
        currentResult = allResults.get(allResults.indexOf(result));
        menu.setHeaderTitle(formatValueString(currentResultValue));
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
        Logger.debug(item.getGroupId() + "-" + item.getItemId());
        if (MENU_GROUP_ID == item.getGroupId()) {
            switch (item.getItemId()) {
                case MENU_EDIT_ID:
                    Intent intent = new Intent(rootView.getContext(), ManageResultsValueActivity.class);
                    intent.putExtra(ManageResultsValueActivity.RESULT_EXTRA, currentResultValue);
                    startActivityForResult(intent, RESULT_VALUE_EDIT);
                    break;
                case MENU_DELETE_ID:
                    generateDeleteConfirmationDialog(formatValueString(currentResultValue),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteResultsValue().execute(currentResultValue.getId());
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
        // In case of OK result
        if (resultCode == Activity.RESULT_OK) {
            Bundle result = data.getExtras();
            // In case of Add result
            if (requestCode == RESULT_ADD) {
                currentResult = (MobileResults) result.getSerializable(ManageResultActivity.RESULT_EXTRA);
                allResults.add(currentResult);
                currentResultValue = null;
                refreshResultsTable(0);
                // In case of Add or Edit result value
            } else if (requestCode == RESULT_VALUE_EDIT || requestCode == RESULT_VALUE_ADD) {
                currentResultValue = (MobileResultsValue) result
                        .getSerializable(ManageResultsValueActivity.RESULT_EXTRA);
                // Remove before add in case of Edit Schedule
                if (requestCode == RESULT_VALUE_EDIT) {
                    // equals() depend on 'id' only
                    currentResult.setId(currentResultValue.getResultsId());
                    currentResult = allResults.get(allResults.indexOf(currentResult));
                    currentResult.getValues().remove(currentResultValue);
                }
                currentResult.getValues().add(currentResultValue);
                drawResultsTable(currentResult);
            }
        }
    }

    // Private methods

    private void registerOnClickListenersNavigationButtons() {
        // Previous
        Button prev = (Button) rootView.findViewById(R.id.results_button_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshResultsTable(-1);
            }
        });
        // Next
        Button next = (Button) rootView.findViewById(R.id.results_button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshResultsTable(1);
            }
        });
    }

    private void registerOnClickListenersManageResultsButtons() {
        // Delete button
        Button delete = (Button) rootView.findViewById(R.id.results_button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentResult != null) {
                    generateDeleteConfirmationDialog(currentResult.getName(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new DeleteResults().execute(currentResult.getId());
                        }
                    });
                }
            }
        });
        // Refresh button
        Button refresh = (Button) rootView.findViewById(R.id.results_button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeGetAlResults();
            }
        });
        // Add button
        Button add = (Button) rootView.findViewById(R.id.results_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageResultActivity.class);
                startActivityForResult(intent, RESULT_ADD);
            }
        });
    }

    private void registerOnClickListenersAddResultsValueButton() {
        Button add = (Button) rootView.findViewById(R.id.result_values_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageResultsValueActivity.class);
                intent.putExtra(ManageResultsValueActivity.RESULT_ID, currentResult.getId());
                startActivityForResult(intent, RESULT_VALUE_ADD);
            }
        });
    }

    private void invokeGetAlResults() {
        new GetAllResults().execute();
    }

    private void toggleNavigationButtons(boolean isVisible) {
        Button previous = (Button) rootView.findViewById(R.id.results_button_prev);
        Button next = (Button) rootView.findViewById(R.id.results_button_next);
        if (isVisible) {
            previous.setVisibility(Button.VISIBLE);
            next.setVisibility(Button.VISIBLE);
        } else {
            previous.setVisibility(Button.INVISIBLE);
            next.setVisibility(Button.INVISIBLE);
        }
    }

    private void toggleManageResultsButton(boolean isVisible) {
        Button deleteResult = (Button) rootView.findViewById(R.id.results_button_delete);
        Button addValue = (Button) rootView.findViewById(R.id.result_values_button_add);
        if (isVisible) {
            deleteResult.setVisibility(Button.VISIBLE);
            addValue.setVisibility(Button.VISIBLE);
        } else {
            deleteResult.setVisibility(Button.INVISIBLE);
            addValue.setVisibility(Button.INVISIBLE);
        }
    }

    private TableLayout clearResultsTable() {
        TableLayout resultsTable = (TableLayout) rootView.findViewById(R.id.results_list);
        resultsTable.removeAllViews();
        return resultsTable;
    }

    private void drawResultsTable(MobileResults table) {
        // Clear table
        TableLayout resultsTable = clearResultsTable();
        // Add header
        TableRow header = generateTableRow(null);
        String unit = table.getUnits() != null && !"".equals(table.getUnits()) ? " (" + table.getUnits() + ")" : "";
        TextView cell = generateTextView(table.getName() + unit);
        header.addView(cell);
        resultsTable.addView(header);
        // Add values
        for (final MobileResultsValue value : table.getValues()) {
            final TableRow row = generateTableRow(value);
            // Add date time
            TextView date = generateTextView(DateUtils
                    .formatDateTime(value.getMeasurementDate().withZone(DateTimeZone.getDefault())));
            row.addView(date);
            // Add separator
            TextView separator = generateTableSeparator();
            row.addView(separator);
            // Add value
            TextView result = generateTextView(value.getMeasurement());
            row.addView(result);
            // Add row
            resultsTable.addView(row);
        }
    }

    private void refreshResultsTable(int value) {
        int size = allResults.size();
        int index = 0;
        if (size > 0) {
            toggleManageResultsButton(true);
            toggleNavigationButtons(size > 1);
            // Get first result in case of deletion
            if (currentResult == null) {
                currentResult = allResults.get(0);
            }
            // Get current index
            index = allResults.indexOf(currentResult);
            // Move index to right or left
            if (value < 0) {
                index--;
            } else if (value > 0) {
                index++;
            }
            // If lower than 0 then set it to last element
            if (index < 0) {
                index = allResults.size() - 1;
            }
            // If more than size set it to first element
            if (index > allResults.size() - 1) {
                index = 0;
            }
            currentResult = allResults.get(index);
            drawResultsTable(currentResult);
        } else {
            currentResult = null;
            clearResultsTable();
            toggleManageResultsButton(false);
        }
    }

    private String formatValueString(MobileResultsValue value) {
        return DateUtils.formatDateTime(value.getMeasurementDate().withZone(DateTimeZone.getDefault()))
                + ": " + value.getMeasurement();
    }

    private class GetAllResults extends AsyncTask<String, String, List<MobileResults>> {

        private final String PATH_SCHEDULES = "/mobile/results/all";

        /**
         * Performs the action in background thread.
         *
         * @param params parameters of the task
         * @return result
         */
        @Override
        protected List<MobileResults> doInBackground(String... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            MobileResults[] tablesArray = client.get(PATH_SCHEDULES, MobileResults[].class);
            List<MobileResults> result = new ArrayList<>();
            if (tablesArray != null) {
                Collections.addAll(result, tablesArray);
            }
            return result;
        }

        /**
         * Runs the UI thread after doInBackground() method is executed.
         *
         * @param result result from doInBackground() method
         */
        @Override
        protected void onPostExecute(List<MobileResults> result) {
            allResults = result;
            refreshResultsTable(0);
        }
    }

    private class DeleteResults extends AsyncTask<Long, String, Result> {

        private final String PATH_DELETE = "/mobile/results/delete/";

        @Override
        protected Result doInBackground(Long... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.delete(PATH_DELETE + params[0], Result.class);
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null) {
                if (result.isSuccess()) {
                    // Create MobileResults to use it in remove() since equals() depend on 'id' only
                    MobileResults toRemove = new MobileResults();
                    toRemove.setId(result.getId());
                    allResults.remove(toRemove);
                    currentResult = null;
                    refreshResultsTable(0);
                } else {
                    MainActivity.createErrorDialog(rootView.getContext(), result.getError());
                }
            }
        }
    }

    private class DeleteResultsValue extends AsyncTask<Long, String, Result> {

        private final String PATH_DELETE = "/mobile/results/value/delete/";

        @Override
        protected Result doInBackground(Long... params) {
            HttpClient client = MainActivity.getAuthenticatedHttpClient();
            return client.delete(PATH_DELETE + params[0], Result.class);
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null) {
                if (result.isSuccess()) {
                    // Create MobileResultsValue to use it in remove() since equals() depend on 'id' only
                    MobileResultsValue toRemove = new MobileResultsValue();
                    toRemove.setId(result.getId());
                    currentResult.getValues().remove(toRemove);
                    drawResultsTable(currentResult);
                } else {
                    MainActivity.createErrorDialog(rootView.getContext(), result.getError());
                }
            }
        }
    }
}
