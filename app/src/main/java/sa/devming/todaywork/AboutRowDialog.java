package sa.devming.todaywork;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

public class AboutRowDialog extends DialogFragment {
    public enum ChangeList { ADD_EQUIP, DEL_EQUIP, ADD_WORKER, DEL_WORKER}
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
                }
            }
        });
        return builder.create();
    }
}
