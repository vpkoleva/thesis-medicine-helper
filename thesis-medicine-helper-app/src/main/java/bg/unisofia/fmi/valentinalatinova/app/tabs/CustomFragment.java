package bg.unisofia.fmi.valentinalatinova.app.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import bg.unisofia.fmi.valentinalatinova.app.R;

public class CustomFragment extends Fragment {

    private final int PAD = 10;
    protected View rootView;

    protected TableRow generateTableRow(Object tag) {
        TableRow row = new TableRow(rootView.getContext());
        row.setLayoutParams(new TableLayout.LayoutParams());
        row.setBackgroundResource(R.drawable.border);
        if (tag != null) {
            row.setTag(tag);
            registerForContextMenu(row);
        }
        return row;
    }

    protected TextView generateTextView(String content) {
        TextView cell = new TextView(rootView.getContext());
        cell.setText(content);
        cell.setPadding(PAD, PAD, PAD, PAD);
        return cell;
    }

    protected TextView generateTableSeparator() {
        TextView separator = generateTextView("");
        separator.setBackgroundResource(R.drawable.border);
        separator.setPadding(2, PAD, 2, PAD);
        return separator;
    }

    protected void generateDeleteConfirmationDialog(String message, final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(rootView.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.delete_dialog_name)
                .setMessage(String.format(getString(R.string.delete_dialog_text), message))
                .setPositiveButton(R.string.delete_dialog_yes, listener)
                .setNegativeButton(R.string.delete_dialog_no, null)
                .show();
    }
}
