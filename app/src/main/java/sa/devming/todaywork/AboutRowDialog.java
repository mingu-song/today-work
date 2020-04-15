package sa.devming.todaywork;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AboutRowDialog extends DialogFragment {
    public enum ChangeList { ADD_EQUIP, DEL_EQUIP, ADD_WORKER, DEL_WORKER, DEL_ALL}
    public interface AboutRowDialogListener {
        void onSelected(ChangeList selected);
    }

    AboutRowDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AboutRowDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AboutRowDialogListener");
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.add_equip_row));
        arrayAdapter.add(getString(R.string.del_equip_row));
        arrayAdapter.add(getString(R.string.add_worker_row));
        arrayAdapter.add(getString(R.string.del_worker_row));
        arrayAdapter.add(getString(R.string.del_all));

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        listener.onSelected(ChangeList.ADD_EQUIP);
                        break;
                    case 1:
                        listener.onSelected(ChangeList.DEL_EQUIP);
                        break;
                    case 2:
                        listener.onSelected(ChangeList.ADD_WORKER);
                        break;
                    case 3:
                        listener.onSelected(ChangeList.DEL_WORKER);
                        break;
                    case 4:
                        listener.onSelected(ChangeList.DEL_ALL);
                        break;
                }
            }
        });
        return builder.create();
    }
}
