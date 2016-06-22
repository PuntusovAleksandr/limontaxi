package taxi.lemon.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taxi.lemon.R;

/**
 * Created by Администратор on 22.06.2016.
 */
public class ChooseDispatcherPhoneDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = ChooseDispatcherPhoneDialog.class.getName();

    private List<String> phones;

    public static ChooseDispatcherPhoneDialog newInstance() {
        return new ChooseDispatcherPhoneDialog();
    }

    public ChooseDispatcherPhoneDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        phones = Arrays.asList(getActivity().getResources().getStringArray(R.array.dispatcher_phone_list));
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_choose_dispatcher_phone, null);
        ListView phonesList = (ListView) root.findViewById(R.id.cdpd_phone_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, phones);
        phonesList.setAdapter(adapter);
        phonesList.setOnItemClickListener(this);
        ab.setView(root);
        ab.setMessage(getActivity().getResources().getString(R.string.choose_dispatcher_phone));
        return ab.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Uri dispatcherNumber = Uri.parse("tel:" + phones.get(position));
        Intent i = new Intent(Intent.ACTION_DIAL, dispatcherNumber);
        startActivity(i);
        dismiss();
    }
}
