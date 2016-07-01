package taxi.lemon.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import taxi.lemon.R;
import taxi.lemon.utils.PhoneUtils;

public class EnterDataDialog extends DialogFragment {
    public static final String TAG = EnterDataDialog.class.getSimpleName();

    public static final String ARGS_TYPE = "ARGS_TYPE";
    public static final String ARGS_DATA = "ARGS_DATA";

    public static final int TYPE_NOTIFY_PHONE = 0x1;
    public static final int TYPE_ADD_COST = 0x2;
    public static final int TYPE_INSERT_ENTRANCE = 0x3;

    private DialogButtonsListener mListener;

    private View view;

    public static EnterDataDialog newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARGS_TYPE, type);

        EnterDataDialog fragment = new EnterDataDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        final TextInputLayout tilData = (TextInputLayout) view.findViewById(R.id.til_data);
        final EditText etData = (EditText) view.findViewById(R.id.et_data);

        final int type = getArguments().getInt(ARGS_TYPE);

        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etData.getText().toString();

                if (data.isEmpty()) return;

                if (type == TYPE_NOTIFY_PHONE && !PhoneUtils.isPhoneValid(data)) {
                    tilData.setError(getString(R.string.error_invalid_phone_format));
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putInt(ARGS_TYPE, type);
                bundle.putString(ARGS_DATA, data);

                mListener.OnDialogPositiveClick(dialog, bundle);
//                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DialogButtonsListener) {
            mListener = (DialogButtonsListener) context;
        } else {
            Fragment fragment = getTargetFragment();
            if (fragment instanceof DialogButtonsListener) {
                mListener = (DialogButtonsListener) fragment;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement DialogButtonsListener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_enter_data, null);

        final TextInputLayout tilData = (TextInputLayout) view.findViewById(R.id.til_data);
        final EditText etData = (EditText) view.findViewById(R.id.et_data);

        final int type = getArguments().getInt(ARGS_TYPE);

        Resources res = getResources();

        switch (type) {
            case TYPE_NOTIFY_PHONE:
                adb.setTitle(R.string.edd_title_notify_phone);
                tilData.setHint(getString(R.string.edd_hint_phone));
                etData.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case TYPE_ADD_COST:
                adb.setTitle(R.string.edd_title_add_cost);
                tilData.setHint(getString(R.string.edd_hint_amount));
                etData.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TYPE_INSERT_ENTRANCE:
                adb.setTitle(R.string.edd_title_insert_entrance);
                tilData.setHint(getString(R.string.edd_hint_entrance));
                etData.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

        adb.setView(
                view,
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_top_margin),
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_bottom_margin)
        );

        adb.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.OnDialogNegativeClick(dialog);
            }
        });
        adb.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String data = etData.getText().toString();
//
//                if (data.isEmpty()) return;
//
//                if (type == TYPE_NOTIFY_PHONE && !PhoneUtils.isPhoneValid(data)) {
//                    tilData.setError(getString(R.string.error_invalid_phone_format));
//                    return;
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putInt(ARGS_TYPE, type);
//                bundle.putString(ARGS_DATA, data);
//
//                mListener.OnDialogPositiveClick(dialog, bundle);
            }
        });


        return adb.create();
    }
}
