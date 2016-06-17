package taxi.lemon.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidquery.AQuery;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.util.Calendar;

import taxi.lemon.R;
import taxi.lemon.dialogs.DialogButtonsListener;
import taxi.lemon.dialogs.EnterCommentDialog;
import taxi.lemon.dialogs.EnterDataDialog;
import taxi.lemon.models.Order;
import taxi.lemon.utils.FormatUtils;

public class PreferencesActivity extends AppCompatActivity implements DialogButtonsListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = PreferencesActivity.class.getSimpleName();

    private AQuery aq;

    private Order order;

//    private View.OnTouchListener rbOnTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (((RadioButton) v).isChecked()) {
//                ((RadioGroup) aq.id(R.id.rg_rate_types).getView()).clearCheck();
//                return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onStart() {
        super.onStart();

        initViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        setUpToolbar();

        aq = new AQuery(this);

        order = Order.getInstance();
    }

    /**
     * Set up toolbar
     */
    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.pa_title_preferences);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Initiate all views behaviour
     */
    private void initViews() {
        aq.id(R.id.pre_order).gone();

        initRateType();
        initAddParameters();
        initAddCost();
        initComment();

        aq.id(R.id.cb_pre_order).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                order.setPreOrder(isChecked);
                if (isChecked) {
                    aq.id(R.id.pre_order).visible();
                } else {
                    aq.id(R.id.pre_order).gone();
                }
            }
        });

        initDateAndTimeFields();


        aq.id(R.id.ib_edit_add_cost).clicked(this);
        aq.id(R.id.ib_edit_date).clicked(this);
        aq.id(R.id.ib_edit_time).clicked(this);
        aq.id(R.id.ib_edit_comment).clicked(this);
        aq.id(R.id.btn_save).clicked(this);

        ((RadioGroup) aq.id(R.id.rg_rate_types).getView()).setOnCheckedChangeListener(this);

        aq.id(R.id.cb_empty_trunk).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_baggage).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_animal).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_conditioner).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_courier_delivery).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.route_undefined).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_terminal).getCheckBox().setOnCheckedChangeListener(this);
        aq.id(R.id.cb_receipt).getCheckBox().setOnCheckedChangeListener(this);
//        aq.id(R.id.cb_meet_with_table).getCheckBox().setOnCheckedChangeListener(this);
    }

    private void initComment() {
        String comment = order.getComment();

        if (comment != null && !comment.isEmpty()) {
            aq.id(R.id.tv_comment).text(comment);
        }
    }

    /**
     * Init date and time fields
     */
    private void initDateAndTimeFields() {
        String date = order.getOrderDate();
        String time = order.getOrderTime();

        if (order.isPreOrder() && time != null && date != null) {
            aq.id(R.id.cb_pre_order).getCheckBox().setChecked(true);
            aq.id(R.id.tv_date).text(date);
            aq.id(R.id.tv_time).text(time);
        }
    }

    /**
     * Init add cost filed
     */
    private void initAddCost() {
        aq.id(R.id.tv_add_cost).text(String.valueOf(order.getAddCost()));
    }

    /**
     * Init add parameters check boxes according to order's add parameters values
     */
    private void initAddParameters() {
        aq.id(R.id.cb_empty_trunk).getCheckBox().setChecked(order.isEmptyTrunk());
        aq.id(R.id.cb_baggage).getCheckBox().setChecked(order.isBaggage());
        aq.id(R.id.cb_animal).getCheckBox().setChecked(order.isAnimals());
        aq.id(R.id.cb_conditioner).getCheckBox().setChecked(order.isConditioner());
        aq.id(R.id.cb_courier_delivery).getCheckBox().setChecked(order.isDelivery());
        aq.id(R.id.route_undefined).getCheckBox().setChecked(order.isRouteUndefined());
        aq.id(R.id.cb_terminal).getCheckBox().setChecked(order.isTerminalPay());
        aq.id(R.id.cb_receipt).getCheckBox().setChecked(order.isReceiptNeed());
//        aq.id(R.id.cb_meet_with_table).getCheckBox().setChecked(order.isMeetWithTable());
    }

    /**
     * Init rate types radio buttons according to order's rate field
     */
    private void initRateType() {
        switch (order.getRate()) {
            case Order.RATE_BASE:
                ((RadioButton) aq.id(R.id.rb_base).getView()).setChecked(true);
                break;
            case Order.RATE_PREMIUM:
                ((RadioButton) aq.id(R.id.rb_premium).getView()).setChecked(true);
                break;
//            case Order.RATE_TRUCK:
//                ((RadioButton) aq.id(R.id.rb_truck).getView()).setChecked(true);
//                break;
            case Order.RATE_WAGON:
                ((RadioButton) aq.id(R.id.rb_wagon).getView()).setChecked(true);
                break;
            case Order.RATE_MINIBUS:
                ((RadioButton) aq.id(R.id.rb_minibus).getView()).setChecked(true);
                break;
//            case Order.RATE_BUSINESS:
//                ((RadioButton) aq.id(R.id.rb_business).getView()).setChecked(true);
//                break;
        }
    }

    @Override
    public void OnDialogPositiveClick(DialogInterface dialog, Bundle data) {
        switch (data.getInt(EnterDataDialog.ARGS_TYPE, -1)) {
            case EnterDataDialog.TYPE_ADD_COST:
                String addCost = data.getString(EnterDataDialog.ARGS_DATA);
                if (addCost != null) {
                    aq.id(R.id.tv_add_cost).text(addCost);
                    order.setAddCost(Integer.parseInt(addCost));
                }
                break;
            default:
                String comment = data.getString(EnterCommentDialog.ARGS_COMMENT, "");
                if (comment.isEmpty()) {
                    aq.id(R.id.tv_comment).text(R.string.pa_comment_init_content);
                } else {
                    aq.id(R.id.tv_comment).text(comment);
                }
                order.setComment(comment);
        }

        dialog.dismiss();
    }

    @Override
    public void OnDialogNegativeClick(DialogInterface dialog) {
        dialog.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_edit_add_cost:
                EnterDataDialog.newInstance(EnterDataDialog.TYPE_ADD_COST)
                        .show(getSupportFragmentManager(), EnterDataDialog.TAG);
                break;
            case R.id.ib_edit_date:
                showDateDialog(Calendar.getInstance());
                break;
            case R.id.ib_edit_time:
                showTimeDialog(Calendar.getInstance());
                break;
            case R.id.ib_edit_comment:
                EnterCommentDialog.newInstance(order.getComment())
                        .show(getSupportFragmentManager(), EnterCommentDialog.TAG);
                break;
            case R.id.btn_save:
                Order.getInstance().updateInstance(order);
                finish();
                break;
        }
    }

    /**
     * Show date picker dialog (library)
     * @param calendar calendar instance
     */
    private void showDateDialog(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date = DatePickerDialog.newInstance(this, year, month, day);

        date.setAccentColor(getResources().getColor(R.color.colorAccent));

//        Calendar[] daysRange = new Calendar[3];
//
//        for (int i = 0; i < daysRange.length; i++) {
//            daysRange[i] = calendar;
//            logger(calendar.toString());
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }

        date.setMinDate(calendar);

        date.show(getFragmentManager(), "DatePickerDialog");
    }

    /**
     * Show time picker dialog (library)
     * @param calendar calendar instance
     */
    private void showTimeDialog(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog time = TimePickerDialog.newInstance(this, hour, minute,
                DateFormat.is24HourFormat(this));

        time.setAccentColor(getResources().getColor(R.color.colorAccent));
        time.setMinTime(new Timepoint(hour, minute));

        time.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = FormatUtils.getDateString(year, monthOfYear, dayOfMonth);
        aq.id(R.id.tv_date).text(date);
        order.setOrderDate(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = FormatUtils.getTimeString(hourOfDay, minute, second);
        aq.id(R.id.tv_time).text(time);
        order.setOrderTime(time);
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_base:
                order.setRate(Order.RATE_BASE);
                break;
            case R.id.rb_premium:
                order.setRate(Order.RATE_PREMIUM);
                break;
            case R.id.rb_wagon:
                order.setRate(Order.RATE_WAGON);
                break;
            case R.id.rb_minibus:
                order.setRate(Order.RATE_MINIBUS);
                break;
//            case R.id.rb_truck:
//                order.setRate(Order.RATE_TRUCK);
//                break;
//            case R.id.rb_business:
//                order.setRate(Order.RATE_BUSINESS);
//                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_empty_trunk:
                order.setEmptyTrunk(isChecked);
                break;
            case R.id.cb_baggage:
                order.setBaggage(isChecked);
                break;
            case R.id.cb_animal:
                order.setAnimals(isChecked);
                break;
            case R.id.cb_conditioner:
                order.setConditioner(isChecked);
                break;
            case R.id.cb_courier_delivery:
                order.setDelivery(isChecked);
                break;
            case R.id.route_undefined:
                order.setRouteUndefined(isChecked);
                break;
            case R.id.cb_terminal:
                order.setTerminalPay(isChecked);
                break;
            case R.id.cb_receipt:
                order.setReceiptNeed(isChecked);
                break;
//            case R.id.cb_meet_with_table:
//                order.setMeetWithTable(isChecked);
//                break;
        }
    }
}
