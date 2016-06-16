package digitalpromo.cabsdemo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import digitalpromo.cabsdemo.R;

public class ChooseDialog extends DialogFragment {
    public static final String TAG = ChooseDialog.class.getSimpleName();
    private static final String EXTRA_DIALOG_TYPE = "EXTRA_DIALOG_TYPE";
    private static final int TYPE_EDIT_WAY_POINT_MODE = 0x1;
    private static final int TYPE_EDIT_INFO_MODE = 0x2;

    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final int ACTION_TYPE = 0x1;
    public static final int ACTION_MAP = 0x2;
    public static final int ACTION_CITY = 0x3;
    public static final int ACTION_PHONE = 0x4;

    public interface ChooseModeListener {
        void onChoose(DialogInterface dialogInterface, Bundle data);
    }

    private ChooseModeListener mListener;

    public static ChooseDialog newInstanceEditWayPoint(int position) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_DIALOG_TYPE, TYPE_EDIT_WAY_POINT_MODE);
        args.putInt(EXTRA_POSITION, position);

        ChooseDialog fragment = new ChooseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChooseDialog newInstanceEditInfo() {

        Bundle args = new Bundle();
        args.putInt(EXTRA_DIALOG_TYPE, TYPE_EDIT_INFO_MODE);

        ChooseDialog fragment = new ChooseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment target = getTargetFragment();
        if (target instanceof ChooseModeListener) {
            mListener = (ChooseModeListener) target;
        } else {
            throw new RuntimeException(target + "must must implement DialogButtonsListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choose_edit_mode, null);

        int mode = getArguments().getInt(EXTRA_DIALOG_TYPE);
        final int position = getArguments().getInt(EXTRA_POSITION);

        switch (mode) {
            case TYPE_EDIT_WAY_POINT_MODE:
//                view.findViewById(R.id.rb_city).setVisibility(View.GONE);
                view.findViewById(R.id.rb_phone).setVisibility(View.GONE);
                break;
            case TYPE_EDIT_INFO_MODE:
                view.findViewById(R.id.rb_type).setVisibility(View.GONE);
                view.findViewById(R.id.rb_map).setVisibility(View.GONE);
                break;
        }

        adb.setTitle(R.string.cemd_title_edit_mode);

        Resources res = getResources();
        adb.setView(
                view,
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_top_margin),
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_bottom_margin)
        );

        final AlertDialog dialog = adb.create();

        RadioGroup modes = (RadioGroup) view.findViewById(R.id.rg_modes);

        modes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Bundle data = new Bundle();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_type:
                        data.putInt(EXTRA_ACTION, ACTION_TYPE);
                        data.putInt(EXTRA_POSITION, position);
                        mListener.onChoose(dialog, data);
                        break;
                    case R.id.rb_map:
                        data.putInt(EXTRA_ACTION, ACTION_MAP);
                        data.putInt(EXTRA_POSITION, position);
                        mListener.onChoose(dialog, data);
                        break;
//                    case R.id.rb_city:
//                        data.putInt(EXTRA_ACTION, ACTION_CITY);
//                        mListener.onChoose(dialog, data);
//                        break;
                    case R.id.rb_phone:
                        data.putInt(EXTRA_ACTION, ACTION_PHONE);
                        mListener.onChoose(dialog, data);
                        break;
                }
            }
        });

        return dialog;
    }
}
