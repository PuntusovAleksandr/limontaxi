package taxi.lemon.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import taxi.lemon.R;
import taxi.lemon.activities.MainActivity;

public class OrderGcmListenerService extends GcmListenerService {
    private static final String CLOSE_REASON_SEARCH_SEARCH_FIND_CHANGE = "-1";
    private static final String CLOSE_REASON_PASSENGER_REFUSES = "1";
    private static final String CLOSE_REASON_DRIVER_REFUSES = "2";
    private static final String CLOSE_REASON_DISPATCHER_REFUSES = "3";
    private static final String CLOSE_REASON_NO_CAR = "4";
    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        String close_reason = bundle.getString("close_reason");
        String order_car_info = bundle.getString("order_car_info");
        String message = null;

        if(close_reason.equals(CLOSE_REASON_SEARCH_SEARCH_FIND_CHANGE)) {
            if(order_car_info == null) {
                message = getResources().getString(R.string.search_car);
            } else {
                message = order_car_info;
            }
        } else {
            message = getResources().getString(R.string.refuse_order);
        }

        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Order info")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
