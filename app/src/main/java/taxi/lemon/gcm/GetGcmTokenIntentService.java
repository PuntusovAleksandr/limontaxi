package taxi.lemon.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import taxi.lemon.R;
import taxi.lemon.utils.SharedPreferencesManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */

public class GetGcmTokenIntentService extends IntentService {


    private static final String TAG = GetGcmTokenIntentService.class.getName();

    public GetGcmTokenIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_senderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("Get token", token);
            SharedPreferencesManager.getInstance().saveGcmToken(token);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
