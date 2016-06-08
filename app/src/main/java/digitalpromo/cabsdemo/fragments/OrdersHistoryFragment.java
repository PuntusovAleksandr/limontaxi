package digitalpromo.cabsdemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.adapters.HistoryAdapter;
import digitalpromo.cabsdemo.api.ApiClient;
import digitalpromo.cabsdemo.api.GetOrdersHistoryResponse;
import digitalpromo.cabsdemo.events.MessageEvent;
import digitalpromo.cabsdemo.models.HistoryItem;
import digitalpromo.cabsdemo.models.Order;
import digitalpromo.cabsdemo.models.RouteItem;
import digitalpromo.cabsdemo.utils.FormatUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class OrdersHistoryFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = OrdersHistoryFragment.class.getSimpleName();

    private FragmentsInteractionListener mListener;

    private RecyclerView mRecyclerView;

    private HistoryAdapter.OnItemClickListener onItemClickListener = new HistoryAdapter.OnItemClickListener() {
        @Override
        public void clicked(HistoryItem item) {
            Order order = Order.getInstance();
            order.resetOrder();
            ArrayList<RouteItem> route = item.getRoute();
            for (RouteItem routeItem : route) {
                routeItem.setAddress(routeItem.getAddress());
                routeItem.setLatLng(routeItem.getLatLng());
            }
            order.setRoute(route);

            EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_MAKE_ORDER_FROM_HISTORY));
        }
    };

    public OrdersHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        if (getView() == null) return;

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_history_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void doBack() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.changeTitle(getString(R.string.ohf_title_orders_history));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getHistoryForToday();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date:
                showDateDialog(Calendar.getInstance());
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
        date.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentsInteractionListener) {
            mListener = (FragmentsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = FormatUtils.getDateString(year, monthOfYear, dayOfMonth);
        Log.d(TAG, "onDateSet: date - " + date);
        getOrdersHistory(date);

    }


    private void getOrdersHistory(String date) {
        mListener.displayProgress(true);
        ApiClient.getInstance().getOrdersHistory(date, new ApiClient.ApiCallback<GetOrdersHistoryResponse>() {
            @Override
            public void response(GetOrdersHistoryResponse response) {
                mListener.displayProgress(false);
                if (response.isOK()) {
                    // specify an adapter (see also next example)
                    RecyclerView.Adapter mAdapter = new HistoryAdapter(response.getHistory(), onItemClickListener);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error() {
                mListener.displayProgress(false);
            }

            @Override
            public void noInternetConnection() {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }

    private void getHistoryForToday() {
        Calendar c = Calendar.getInstance();
        String date = FormatUtils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        getOrdersHistory(date);
    }
 }
