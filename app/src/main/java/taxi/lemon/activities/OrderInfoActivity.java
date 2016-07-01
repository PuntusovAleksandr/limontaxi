package taxi.lemon.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import taxi.lemon.R;
import taxi.lemon.models.RouteItem;

public class OrderInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = OrderInfoActivity.class.getName();

    public final static String EXTRA_ORDER_ID = "order id";
    public static final String EXTRA_ADDRESS_FROM = "address from";
    public static final String EXTRA_ADDRESS_TO = "address to";
    public static final String EXTRA_ORDER_INFO = "order info";
    public static final String EXTRA_DRIVER_PHONE = "driver phone";

    private String orderId;
    private String addressFrom;
    private String addresTo;
    private String orderInfo;
    private String driverPhone;

    private CardView cardFrom;
    private CardView cardTo;
    private CardView cardInfo;
    private TextView tvAddressFrom;
    private TextView tvAddressTo;
    private TextView tvOrderInfo;
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
    }

    private String convert(String item) {
        RouteItem itemFrom = new Gson().fromJson(item, RouteItem.class);
        String number = itemFrom.getNumber() == null ? ", " : itemFrom.getNumber();
        return itemFrom.getStreet() + " " + number;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardFrom = (CardView) findViewById(R.id.card_from);
        cardTo = (CardView) findViewById(R.id.card_to);
        cardInfo = (CardView) findViewById(R.id.card_order_info);
        tvAddressFrom = (TextView) findViewById(R.id.tv_address_from);
        tvAddressTo = (TextView) findViewById(R.id.tv_address_to);
        tvOrderInfo = (TextView) findViewById(R.id.tv_order_info);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_cancel_order:
                // make api call to cancel this order and finish activity
                finish();
                break;
            case R.id.b_call_to_driver:
                callToDriver();
                break;
        }
    }

    private void cancelOrder() {

    }

    private void callToDriver() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + driverPhone));
        if(callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        }
    }
}
