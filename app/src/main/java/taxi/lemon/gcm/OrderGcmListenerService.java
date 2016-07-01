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
import com.google.gson.Gson;

import taxi.lemon.R;
import taxi.lemon.activities.MainActivity;
import taxi.lemon.activities.OrderInfoActivity;
import taxi.lemon.models.Order;
import taxi.lemon.models.RouteItem;

public class OrderGcmListenerService extends GcmListenerService {
    private static final String CLOSE_REASON_SEARCH_SEARCH_FIND_CHANGE = "-1";
    private static final String CLOSE_REASON_PASSENGER_REFUSES = "1";
    private static final String CLOSE_REASON_DRIVER_REFUSES = "2";
    private static final String CLOSE_REASON_DISPATCHER_REFUSES = "3";
    private static final String CLOSE_REASON_NO_CAR = "4";
    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        String orderId = bundle.getString("dispatching_order_uid");
        String from = bundle.getString("route_address_from");
        String to = bundle.getString("route_address_to");
        String closeReason = bundle.getString("close_reason");
        String orderCarInfo = bundle.getString("order_car_info");
        String driverPhone = bundle.getString("driver_phone");
        String message = null;

        if(closeReason.equals(CLOSE_REASON_SEARCH_SEARCH_FIND_CHANGE)) {
            if(orderCarInfo == null) {
                message = getResources().getString(R.string.search_car);
            } else {
                message = getResources().getString(R.string.find_car);
            }
        } else {
            message = getResources().getString(R.string.refuse_order);
        }

        sendNotification(message, orderId, from, to, orderCarInfo, driverPhone, closeReason);
    }

    private void sendNotification(String message, String orderId, String addressFrom, String addressTo, String orderInfo, String driverPhone, String reason) {
        Intent intent = new Intent();
        if(reason.equals(CLOSE_REASON_SEARCH_SEARCH_FIND_CHANGE)) {
            intent.setClass(this, OrderInfoActivity.class);
            intent.putExtra(OrderInfoActivity.EXTRA_ORDER_ID, orderId);
            intent.putExtra(OrderInfoActivity.EXTRA_ADDRESS_FROM, addressFrom);
            intent.putExtra(OrderInfoActivity.EXTRA_ADDRESS_TO, addressTo);
            intent.putExtra(OrderInfoActivity.EXTRA_ORDER_INFO, orderInfo);
            intent.putExtra(OrderInfoActivity.EXTRA_DRIVER_PHONE, driverPhone);
        } else {
            intent.setClass(this, MainActivity.class);
        }
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
