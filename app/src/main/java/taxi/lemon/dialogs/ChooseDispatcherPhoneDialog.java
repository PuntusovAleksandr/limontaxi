package taxi.lemon.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import taxi.lemon.R;

/**
 * Created by Администратор on 22.06.2016.
 */
public class ChooseDispatcherPhoneDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    public static int ARRAY_PHONE = 1;
    public static int ARRAY_COMMENT = 2;

    public static final String TAG = ChooseDispatcherPhoneDialog.class.getName();

    private List<String> phones;

    private static int arrayKey;

    public static ChooseDispatcherPhoneDialog newInstance(int array) {
        arrayKey = array;
        return new ChooseDispatcherPhoneDialog();
    }

    public ChooseDispatcherPhoneDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setMessage(getActivity().getResources().getString(R.string.choose_dispatcher_phone));
        phones = Arrays.asList(getActivity().getResources().getStringArray(R.array.dispatcher_phone_list));
        if (arrayKey == ARRAY_COMMENT) {
            phones = Arrays.asList(getActivity().getResources().getStringArray(R.array.comments_list));
            ab.setMessage(getActivity().getResources().getString(R.string.choose_sen_comments));
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_choose_dispatcher_phone, null);
        ListView phonesList = (ListView) root.findViewById(R.id.cdpd_phone_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, phones);
        phonesList.setAdapter(adapter);
        phonesList.setOnItemClickListener(this);
        ab.setView(root);
        return ab.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (arrayKey == ARRAY_PHONE) {
            Uri dispatcherNumber = Uri.parse("tel:+" + phones.get(position));
            Intent i = new Intent(Intent.ACTION_DIAL, dispatcherNumber);
            startActivity(i);
        } else {
            switch (position) {
                case 0:             // to Google play
                    rateUpApp();
                    break;

                case 1:             // to mail
                    sendMail();
                    break;

                case 2:             // to site
                    goToSite();
                    break;
            }
        }
        dismiss();
    }

    private void goToSite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lemon.taxi"));
        startActivity(browserIntent);
    }

    private void sendMail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getActivity().getString(R.string.mail_limon_taxi)});
        i.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.comments_text));
        i.putExtra(Intent.EXTRA_TEXT, getActivity().getString(R.string.title_comment));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.client_mail_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * //     * Launch the Play Store with your App page already opened
     * //
     */
    private void rateUpApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getApplicationInfo().packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        goToMarket.addFlags(flags);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getApplicationInfo().packageName)));
        }
    }
}
