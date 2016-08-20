package taxi.lemon.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import taxi.lemon.R;
import taxi.lemon.activities.MainActivity;
import taxi.lemon.activities.OrderInfoActivity;
import taxi.lemon.utils.SharedPreferencesManager;

public class OrderGcmListenerService extends GcmListenerService {

    /*
    Описание статусов закрытия заказа
«close_reason»:
-1 Выполняется
0 Выполнен
1 Отказ клиента
2 Отказ водителя
3 Отмена по вине диспетчера
4 Нет машины
5 Просчет
6 Отказ клиента не устроил тариф
7 Отказ клиента не устроило время
8 Перекинут на СОЗ (выполнен)
9 Перекинут на СОЗ (выполнен)
     */

    public static final String LOG = "PUSH_LOG";

    private static final String CLOSE_REASON_SEARCH_OR_FIND = "-1";
    private static final String CLOSE_REASON_CHANGE = "?";
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
        String orderTime = bundle.getString("required_time");
        String message = null;

        Log.i(LOG, "Start " + closeReason);
        Log.i(LOG, "Message  " + orderCarInfo);

        if (closeReason.equals(CLOSE_REASON_CHANGE)) {
            message = getResources().getString(R.string.change_car);        // Замена машины
        } else if (closeReason.equals(CLOSE_REASON_SEARCH_OR_FIND)) {
            if (orderCarInfo == null) {
                message = getResources().getString(R.string.search_car);    // Поиск машины
            } else {
                message = getResources().getString(R.string.find_car);      // Машина найдена
            }
        } else {
//            message = getResources().getString(R.string.refuse_order);
        }
        Log.i(LOG, "First Step " + closeReason);
        Log.i(LOG, "Message  " + orderCarInfo);

        sendNotification(message, orderId, from, to, orderCarInfo, driverPhone, closeReason, orderTime);
    }

    private void sendNotification(String message, String orderId, String addressFrom, String addressTo,
                                  String orderInfo, String driverPhone, String closeReason, String orderTime) {
        Intent intent = new Intent();
        if (closeReason.equals(CLOSE_REASON_SEARCH_OR_FIND) || closeReason.equals(CLOSE_REASON_CHANGE)) {
            intent.setClass(this, OrderInfoActivity.class);
            intent.putExtra(OrderInfoActivity.EXTRA_ORDER_ID, orderId);
            intent.putExtra(OrderInfoActivity.EXTRA_ADDRESS_FROM, addressFrom);
            intent.putExtra(OrderInfoActivity.EXTRA_ADDRESS_TO, addressTo);
            intent.putExtra(OrderInfoActivity.EXTRA_ORDER_INFO, orderInfo);
            intent.putExtra(OrderInfoActivity.EXTRA_DRIVER_PHONE, driverPhone);
            intent.putExtra(OrderInfoActivity.EXTRA_ORDER_TIME, orderTime);
        } else {
            intent.setClass(this, MainActivity.class);
            switch (closeReason) {
                case CLOSE_REASON_PASSENGER_REFUSES:
                    message = getResources().getString(R.string.refuse_by_passenger);
                    break;
                case CLOSE_REASON_DRIVER_REFUSES:
                    message = getResources().getString(R.string.refuse_by_driver);
                    break;
                case CLOSE_REASON_DISPATCHER_REFUSES:
                    message = getResources().getString(R.string.refuse_by_dispatcher);
                    break;
                case CLOSE_REASON_NO_CAR:
                    message = getResources().getString(R.string.refuse_no_car);
                    break;
            }
        }
        Log.i(LOG, "Last Step " + closeReason);
        Log.i(LOG, "Message  " + orderInfo);

        boolean isUserLoggedIn = SharedPreferencesManager.getInstance().isUserLoggedIn();
        intent.putExtra(MainActivity.EXTRA_IS_USER_LOGGED_IN, isUserLoggedIn);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getResources().getString(R.string.oia_order_info))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
