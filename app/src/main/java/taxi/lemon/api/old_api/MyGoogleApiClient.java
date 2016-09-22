package taxi.lemon.api.old_api;

import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import taxi.lemon.dialogs.ErrorGoogleApiDialog;

/**
 * Client for google api's
 */
public class MyGoogleApiClient {
    private static final String TAG = MyGoogleApiClient.class.getSimpleName();

    private static final long UPDATE_INTERVAL = 10 * 1000;
    private static final long UPDATE_FASTEST_INTERVAL = UPDATE_INTERVAL / 2;

    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    public static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    private AppCompatActivity activity;

    /**
     * Instance of google api client
     */
    private GoogleApiClient mGoogleApiClient;

    public interface ErrorDialogDismissListener {
        /* Called from ErrorDialogFragment when the dialog is dismissed. */
        void onDialogDismissed();
    }

    public MyGoogleApiClient(AppCompatActivity activity, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient
                .Builder(activity, connectionCallbacks, onConnectionFailedListener)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Starts location updates
     */
    public void startLocationUpdates(LocationListener listener) {
        Log.d(TAG, "startLocationUpdates: ");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(UPDATE_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, listener);
        } catch (SecurityException e) {
//            throw new SecurityException("Application has no permissions for Location");
        }
    }

    /**
     * Stops location updates
     */
    public void stopLocationUpdates(LocationListener listener) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, listener);
    }

    /**
     * Connect to google api client
     */
    public void connect() {
        Log.d(TAG, "connect: ");
        if (mGoogleApiClient != null && !mResolvingError && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Disconnect from google api client
     */
    public void disconnect() {
        Log.d(TAG, "disconnect: ");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void connectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorGoogleApiDialog dialogFragment = ErrorGoogleApiDialog.newInstance(new ErrorDialogDismissListener() {
            @Override
            public void onDialogDismissed() {
                mResolvingError = false;
            }
        });
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(activity.getSupportFragmentManager(), "errordialog");
    }
}
