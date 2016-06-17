package taxi.lemon.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.common.GoogleApiAvailability;

import taxi.lemon.api.old_api.MyGoogleApiClient;

/**
 * Created by Takeitez on 17.02.2016.
 */
public class ErrorGoogleApiDialog extends DialogFragment {
    private MyGoogleApiClient.ErrorDialogDismissListener mListener;

    public ErrorGoogleApiDialog() {

    }

    public static ErrorGoogleApiDialog newInstance(MyGoogleApiClient.ErrorDialogDismissListener listener) {

        Bundle args = new Bundle();

        ErrorGoogleApiDialog fragment = new ErrorGoogleApiDialog();
        fragment.setArguments(args);
        fragment.setListener(listener);

        return fragment;
    }

    private void setListener(MyGoogleApiClient.ErrorDialogDismissListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the error code and retrieve the appropriate dialog
        int errorCode = this.getArguments().getInt(MyGoogleApiClient.DIALOG_ERROR);
        return GoogleApiAvailability.getInstance().getErrorDialog(
                this.getActivity(), errorCode, MyGoogleApiClient.REQUEST_RESOLVE_ERROR);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mListener.onDialogDismissed();
    }
}
