package taxi.lemon.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = MyInstanceIDListenerService.class.getName();

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GetGcmTokenIntentService.class);
        startService(intent);
    }
}
