package digitalpromo.cabsdemo.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.models.City;
import digitalpromo.cabsdemo.models.Order;

/**
 * Dialog to choose city where we work
 */
public class ChooseCityDialog extends DialogFragment {
    public static final String TAG = ChooseCityDialog.class.getSimpleName();
    private static final String EXTRA_CITIES = ChooseCityDialog.class.getPackage().getName() + "." + ChooseCityDialog.class.getSimpleName();
    public static final String ARGS_CITY = "ARGS_CITY";

    private DialogButtonsListener mListener;

    public static ChooseCityDialog newInstance(ArrayList<City> cities) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_CITIES, cities);
        ChooseCityDialog fragment = new ChooseCityDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment target = getTargetFragment();

        if (target instanceof DialogButtonsListener) {
            mListener = (DialogButtonsListener) target;
        } else {
            throw new RuntimeException(target
                    + " must implement DialogButtonsListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getTargetFragment() == null)
            Log.d(TAG, "onCreate: parent - null");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<City> cities = getArguments().getParcelableArrayList(EXTRA_CITIES);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);

        RadioGroup rg = new RadioGroup(getActivity());

        int color = ContextCompat.getColor(getContext(), R.color.textColorSecondary);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (City city : cities) {
            AppCompatRadioButton rb = new AppCompatRadioButton(getActivity());
            rb.setLayoutParams(lp);
            rb.setTextColor(color);
            rb.setId(city.getId());
            rb.setText(city.getName());
            rg.addView(rb);
        }

        adb.setTitle(R.string.ccd_title_choose_city);

        Resources res = getResources();
        adb.setView(
                rg,
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_top_margin),
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_bottom_margin)
        );

        final AlertDialog dialog = adb.create();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (City city : cities) {
                    if (city.getId() == checkedId) {
                        Log.d(TAG, "onCheckedChanged: city " + city.toString());
                        Order.getInstance().setCityId(checkedId);
                        Bundle data = new Bundle();
                        data.putString(ARGS_CITY, city.getName());
                        mListener.OnDialogPositiveClick(dialog, data);
                    }
                }
            }
        });

        return dialog;
    }
}
