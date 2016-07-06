package taxi.lemon.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.lemon.R;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.models.RouteItem;
import taxi.lemon.utils.SharedPreferencesManager;

public class OrderInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = OrderInfoActivity.class.getName();

    public final static String EXTRA_ORDER_ID = "order id";
    public static final String EXTRA_ADDRESS_FROM = "address from";
    public static final String EXTRA_ADDRESS_TO = "address to";
    public static final String EXTRA_ORDER_INFO = "order info";
    public static final String EXTRA_DRIVER_PHONE = "driver phone";
    public static final String EXTRA_ORDER_TIME = "order time";

    private String orderId;
    private String addressFrom;
    private String addresTo;
    private String orderInfo;
    private String driverPhone;
    private String orderTime;

    private CardView cardFrom;
    private CardView cardTo;
    private CardView cardInfo;
    private CardView cardOrderTime;
    private TextView tvAddressFrom;
    private TextView tvAddressTo;
    private TextView tvOrderInfo;
    private TextView tvOrderTime;
    private Button bCancelOrder;
    private Button bCallToDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        addressFrom = getIntent().getStringExtra(EXTRA_ADDRESS_FROM);
        addressFrom = convert(addressFrom);
        addresTo = getIntent().getStringExtra(EXTRA_ADDRESS_TO);
        addresTo = convert(addresTo);
        orderInfo = getIntent().getStringExtra(EXTRA_ORDER_INFO);
        driverPhone = getIntent().getStringExtra(EXTRA_DRIVER_PHONE);
        orderTime = getIntent().getStringExtra(EXTRA_ORDER_TIME);
    }

    private String convert(String item) {
        if(item != null) {
            RouteItem itemFrom = new Gson().fromJson(item, RouteItem.class);
            String number = itemFrom.getNumber() == null ? ", " : itemFrom.getNumber();
            return itemFrom.getStreet() + " " + number;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardFrom = (CardView) findViewById(R.id.card_from);
        cardTo = (CardView) findViewById(R.id.card_to);
        cardInfo = (CardView) findViewById(R.id.card_order_info);
        cardOrderTime = (CardView) findViewById(R.id.card_order_time);
        tvAddressFrom = (TextView) findViewById(R.id.tv_address_from);
        tvAddressTo = (TextView) findViewById(R.id.tv_address_to);
        tvOrderInfo = (TextView) findViewById(R.id.tv_order_info);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        bCancelOrder = (Button) findViewById(R.id.b_cancel_order);
        bCancelOrder.setOnClickListener(this);
        bCallToDriver = (Button) findViewById(R.id.b_call_to_driver);
        bCallToDriver.setOnClickListener(this);

        initViews();
    }

    private void initViews() {
        if(addressFrom != null) {
            cardFrom.setVisibility(View.VISIBLE);
            tvAddressFrom.setText(addressFrom);
        }
        if(addresTo != null) {
            cardTo.setVisibility(View.VISIBLE);
            tvAddressTo.setText(addresTo);
        }
        if(orderInfo != null) {
            tvOrderInfo.setText(orderInfo);
        } else {
            tvOrderInfo.setText(getResources().getString(R.string.search_car));
        }
        if(driverPhone != null) {
            bCallToDriver.setVisibility(View.VISIBLE);
        }
        if(orderTime != null) {
            cardOrderTime.setVisibility(View.VISIBLE);
            tvOrderTime.setText(orderTime);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_cancel_order:
                cancelOrder();
                break;
            case R.id.b_call_to_driver:
                callToDriver();
                break;
        }
    }

    private void cancelOrder() {
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<ResponseBody> call = client.cancelOrder(orderId);
        final Activity thisActivity = this;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(thisActivity, getResources().getString(R.string.oia_order_cancel_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(thisActivity, getResources().getString(R.string.oia_order_cancel_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(thisActivity, getResources().getString(R.string.oia_order_cancel_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callToDriver() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + driverPhone));
        if(callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        }
    }
}
