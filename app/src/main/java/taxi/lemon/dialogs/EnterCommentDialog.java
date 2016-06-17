package taxi.lemon.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import taxi.lemon.R;

/**
 * Dialog for entering comment
 */
public class EnterCommentDialog extends DialogFragment {
    public static final String TAG = EnterAddressDialog.class.getSimpleName();

    public static final String ARGS_COMMENT = "ARGS_COMMENT";

    private DialogButtonsListener mListener;

    public static EnterCommentDialog newInstance(String comment) {
        Bundle args = new Bundle();
        args.putString(ARGS_COMMENT, comment);

        EnterCommentDialog fragment = new EnterCommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DialogButtonsListener) {
            mListener = (DialogButtonsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogButtonsListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_enter_comment, null);

        final EditText etComment = (EditText) view.findViewById(R.id.et_comment);

        String comment = getArguments().getString(ARGS_COMMENT);

        if (comment != null && !comment.isEmpty()) {
            etComment.setText(comment);
        }

        adb.setTitle(R.string.ecd_title_comment);

        Resources res = getResources();
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
                String comment = etComment.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(ARGS_COMMENT, comment);

                mListener.OnDialogPositiveClick(dialog, bundle);
            }
        });

        return adb.create();
    }
}
