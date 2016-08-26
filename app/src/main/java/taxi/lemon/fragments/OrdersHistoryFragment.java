package taxi.lemon.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.lemon.App;
import taxi.lemon.R;
import taxi.lemon.SETTINGS.SettingsApp;
import taxi.lemon.adapters.HistoryAdapter;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.GetOrdersReportResponse;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.api.old_api.ApiClient;
import taxi.lemon.api.old_api.GeoCodingResponse;
import taxi.lemon.data_base.imol.ServiceDImpl;
import taxi.lemon.dialogs.DialogDeleteItemHistory;
import taxi.lemon.events.MessageEvent;
import taxi.lemon.models.HistoryItem;
import taxi.lemon.models.Order;
import taxi.lemon.models.RouteItem;
import taxi.lemon.utils.FormatUtils;
import taxi.lemon.utils.SharedPreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class OrdersHistoryFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
        DialogDeleteItemHistory.ListenerDeleteHistoryItem {

    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;

    public static final String TAG = OrdersHistoryFragment.class.getSimpleName();

    private FragmentsInteractionListener mListener;

    private RecyclerView mRecyclerView;


    private ArrayList<HistoryItem> body;

    private HistoryAdapter.OnItemClickListener onItemClickListener = new HistoryAdapter.OnItemClickListener() {
        @Override
        public void clicked(HistoryItem item) {
            Order order = Order.getInstance();
            order.resetOrder();
            ArrayList<RouteItem> route = item.getRoute();
            for (RouteItem routeItem : route) {
                routeItem.setAddress(routeItem.getStreet() + " " + routeItem.getNumber());
                routeItem.setLatLng(routeItem.getLatLng());
            }
            order.setRoute(route);

            EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_MAKE_ORDER_FROM_HISTORY));
        }

        @Override
        public void deleteItemHistory(HistoryItem mItem) {
            showDialog(mItem);
        }

    };

    private void showDialog(HistoryItem mItem) {
        new DialogDeleteItemHistory(getContext(), mItem, this).show();
    }

    private void getLatLng(final String addr, final RouteItem item) {
        ApiClient.getInstance().getLatLng(addr, new ApiClient.ApiCallback<GeoCodingResponse>() {
            @Override
            public void response(final GeoCodingResponse response) {
                if (response.isOK()) {
                    if (response.getLatLng() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                item.setLatLng(response.getLatLng());
                            }
                        });
                    } else {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.of_wrong_address), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void error() {

            }

            @Override
            public void noInternetConnection() {
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }

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

//        getReportForToday();
        getOrdersHistory();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.history_menu, menu);
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
     *
     * @param calendar calendar instance
     */
    private void showDateDialog(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date = DatePickerDialog.newInstance(this, year, month, day);

        date.setAccentColor(getResources().getColor(R.color.colorAccent));
        date.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_orders_history, container, false);

        sharedPreferences = getActivity().
                getSharedPreferences(SettingsApp.FILE_NAME, Context.MODE_PRIVATE);

        return inflate;
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
        getOrdersReport(date);
    }


    private void getOrdersReport(String date) {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<GetOrdersReportResponse> call = client.getOrdersReport("2016.06.16", "2016.06.21");
        call.enqueue(new Callback<GetOrdersReportResponse>() {
            @Override
            public void onResponse(Call<GetOrdersReportResponse> call, Response<GetOrdersReportResponse> response) {
                mListener.displayProgress(false);
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<GetOrdersReportResponse> call, Throwable t) {
                mListener.displayProgress(false);
            }
        });
    }

    private void getReportForToday() {
        Calendar c = Calendar.getInstance();
        String date = FormatUtils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        getOrdersReport(date);
    }

    private void getOrdersHistory() {
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<ArrayList<HistoryItem>> call = client.getOrdersHistory();
        call.enqueue(new Callback<ArrayList<HistoryItem>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryItem>> call, Response<ArrayList<HistoryItem>> response) {
                if (response.isSuccessful()) {
                    body = response.body();
                    if (body != null) {

                        if (ServiceDImpl.getServise(getActivity()).addFromServer(body)) {
                            updateListHistoryItems();
                        }
                    }
                } else {
                    Toast.makeText(App.getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryItem>> call, Throwable t) {
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }


    // delete from list history items
    @Override
    public void deleteFromList(HistoryItem mItem) {

        ServiceDImpl.getServise(getActivity()).deleteHistoryItem(mItem);
        updateListHistoryItems();
    }

    private ArrayList<HistoryItem> historyItems;
    private RecyclerView.Adapter mAdapter;

    private void updateListHistoryItems() {
        if (historyItems == null) {
            historyItems = new ArrayList<>();
        } else {
            historyItems.clear();
        }
        historyItems = ServiceDImpl.getServise(getActivity()).getHistoryItems();

        if (mAdapter != null) {
            mAdapter = null;
        }
        mAdapter = new HistoryAdapter(historyItems, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}
