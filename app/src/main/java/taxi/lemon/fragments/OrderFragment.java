package taxi.lemon.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import taxi.lemon.R;
import taxi.lemon.activities.MapActivity;
import taxi.lemon.activities.PreferencesActivity;
import taxi.lemon.adapters.OnDataChanged;
import taxi.lemon.adapters.RouteAdapter;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.GetOrderCostRequest;
import taxi.lemon.api.new_api.GetOrderCostResponse;
import taxi.lemon.api.new_api.MakeOrderRequest;
import taxi.lemon.api.new_api.MakeOrderResponse;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.api.old_api.ApiClient;
import taxi.lemon.dialogs.ChooseDialog;
import taxi.lemon.dialogs.DialogButtonsListener;
import taxi.lemon.dialogs.EnterAddressDialog;
import taxi.lemon.dialogs.EnterDataDialog;
import taxi.lemon.events.MessageEvent;
import taxi.lemon.helper.ItemTouchHelperCallback;
import taxi.lemon.models.Order;
import taxi.lemon.models.RouteItem;
import taxi.lemon.models.UserProfile;
import taxi.lemon.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class OrderFragment
        extends BaseFragment
        implements View.OnClickListener, DialogButtonsListener, OnDataChanged, ChooseDialog.ChooseModeListener {
    public static final String TAG = OrderFragment.class.getSimpleName();

    private RouteAdapter mAdapter;

    private FragmentsInteractionListener mListener;

    private TextView tvOrderCost;
    private TextView tvCity;
    private TextView tvUserPhone;

    private boolean isMenuOpen;

    private RecyclerView mRecyclerView;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        View v = getView();

        if (v == null) return;

        isMenuOpen = false;

        final FloatingActionButton fabMenu = (FloatingActionButton) v.findViewById(R.id.fab_menu);
        fabMenu.setOnClickListener(this);

        v.findViewById(R.id.fab_open_map).setOnClickListener(this);
        v.findViewById(R.id.fab_type_address).setOnClickListener(this);

        ((FloatingActionButton) v.findViewById(R.id.fab_type_address)).hide();
        ((FloatingActionButton) v.findViewById(R.id.fab_open_map)).hide();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_route_list);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                logger("onScrollStateChanged: state - " + newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (fabMenu.isShown()) {
                            if (isMenuOpen) {
                                animateFabMenuAppearance();
                            }
                            fabMenu.hide();
                        }
                        break;
                    default:
                        if (!isLastItemDisplaying(recyclerView) && !fabMenu.isShown()) {
                            fabMenu.show();
                        }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        Toolbar bottomToolbar = (Toolbar) v.findViewById(R.id.bottom_toolbar);

        tvCity = (TextView) bottomToolbar.findViewById(R.id.tv_city);
        tvCity.setText("Киев");
        tvUserPhone = (TextView) bottomToolbar.findViewById(R.id.tv_phone);
        tvOrderCost = (TextView) bottomToolbar.findViewById(R.id.tv_cost);

        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogFragment dialog;
                switch (item.getItemId()) {
                    case R.id.item_edit_info:
                        dialog = ChooseDialog.newInstanceEditInfo();
                        dialog.setTargetFragment(getTarget(), 0);
                        dialog.show(getFragmentManager(), ChooseDialog.TAG);
                        break;
                    case R.id.item_make_order:
                        String phone = Order.getInstance().getPhone();

                        if (phone == null || phone.isEmpty()) {
                            dialog = EnterDataDialog.newInstance(EnterDataDialog.TYPE_NOTIFY_PHONE);
                            dialog.setTargetFragment(getTarget(), 0);
                            dialog.show(getFragmentManager(), EnterDataDialog.TAG);
                        }
//                        else if (!isCityChosen()) {
//                            getCities();
//                        }
                        else if (Order.getInstance().getRoute().isEmpty()) {
                            setTutorial();
                        } else {
                            makeOrder();
                        }
                        break;
                }
                return true;
            }
        });
        bottomToolbar.inflateMenu(R.menu.menu_bottom_toolbar);

//        ItemTouchHelper.SimpleCallback callback =
//                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
////                    private Paint p = new Paint();
////                    private Bitmap icon = getIcon();
//
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        mAdapter.deleteItem(viewHolder.getAdapterPosition());
//                    }
//
////                    @Override
////                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
////                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
////                            // Get RecyclerView item from the ViewHolder
////                            View itemView = viewHolder.itemView;
////
////                            if (dX > 0) {
////                                // Set icon for positive displacement
////
////                                /* Set your color for positive displacement */
////                                p.setColor(Color.RED);
////                                // Draw Rect with varying right side, equal to displacement dX
////                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
////                                        (float) itemView.getBottom(), p);
////
////                                // Set the image icon for right swipe
////                                c.drawBitmap(icon,
////                                        (float) itemView.getLeft() + FormatUtils.dpToPx(24),
////                                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
////                                        p);
////                            } else {
////                                // Set icon for negative displacement
////
////                                /* Set your color for negative displacement */
////                                p.setColor(Color.GREEN);
////                                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
////                                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
////                                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
////
////                                // Set the image icon for right swipe
////                                c.drawBitmap(icon,
////                                        (float) itemView.getRight() - FormatUtils.dpToPx(24) - icon.getWidth(),
////                                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
////                                        p);
////                            }
////
////                            // Fade out the view as it is swiped out of the parent's bounds
////                            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
////                            viewHolder.itemView.setAlpha(alpha);
////                            viewHolder.itemView.setTranslationX(dX);
////                        } else {
////                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
////                        }
////                    }
//
//                };

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));

        setAdapter();
    }

    /**
     * Set adapter for list of user's way points
     */
    private void setAdapter() {
        // specify an adapter (see also next example)
        mAdapter = new RouteAdapter(Order.getInstance().getRoute(), new RouteAdapter.OnEditButtonClicked() {
            @Override
            public void clicked(final int position) {
                ChooseDialog dialog = ChooseDialog.newInstanceEditWayPoint(position);
                dialog.setTargetFragment(getTarget(), 0);
                dialog.show(getFragmentManager(), ChooseDialog.TAG);
            }
        });
        mAdapter.setDataChangedListener(this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(mAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(mRecyclerView);

        dataChanged();
    }

    /**
     * Check whether the last item in RecyclerView is being displayed or not
     *
     * @param recyclerView which you would like to check
     * @return true if last position was Visible and false Otherwise
     */
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

            if (firstVisiblePosition > 0
                    && lastVisibleItemPosition != RecyclerView.NO_POSITION
                    && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    /**
     * Animate fab's appearance
     */
    private void animateFabMenuAppearance() {
        View view = getView();

        if (view == null) return;

        FloatingActionButton menu = (FloatingActionButton) view.findViewById(R.id.fab_menu);
        FloatingActionButton openMap = (FloatingActionButton) view.findViewById(R.id.fab_open_map);
        FloatingActionButton typeAddress = (FloatingActionButton) view.findViewById(R.id.fab_type_address);

        if (isMenuOpen) {
            logger("close menu");
            ViewCompat.animate(menu)
                    .rotation(0)
                    .withLayer()
                    .setDuration(300)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
            typeAddress.hide();
            openMap.hide();
            openMap.setClickable(false);
            typeAddress.setClickable(false);
            isMenuOpen = false;
        } else {
            logger("open menu");
            ViewCompat.animate(menu)
                    .rotation(45)
                    .withLayer()
                    .setDuration(300)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
            openMap.show();
            typeAddress.show();
            openMap.setClickable(true);
            typeAddress.setClickable(true);
            isMenuOpen = true;
        }

    }

    /**
     * Set tutorial overlay
     */
    private void setTutorial() {
        View view = getView();

        if (view == null) return;

        MaterialShowcaseView.resetSingleUse(getActivity(), "Tutorial");

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(view.findViewById(R.id.fab_menu))
                .setDismissText(R.string.btn_ok)
                .setContentText(R.string.showcase_text)
                .setDelay(200) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Tutorial") // provide a unique ID used to ensure it is only shown once
                .setMaskColour(getResources().getColor(R.color.showcase))
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
//                        if (!isCityChosen()) {
//                            getCities();
//                        }
                    }
                })
                .show();

    }
    @Override
    protected void doBack() {

    }

    /**
     * Init icon for under layer views
     * @return Bitmap with icon or null if something went wrong
     */
    private Bitmap getIcon() {
        Bitmap icon = null;
        Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_delete);

        if (drawable != null) {
            icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(icon);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return icon;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.changeTitle(getString(R.string.of_title_order_fragment));

        dataChanged();

        if (mAdapter != null && mAdapter.getItemCount() < 1) {
            setTutorial();
        }

        mListener.displayProgress(false);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event) {
        if (event.getEvent() == MessageEvent.EVENT_SET_USER_PHONE) {
            setUserPhone(Order.getInstance().getPhone());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        Order.getInstance().setCityId(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.preferences_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                getActivity().startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
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
    public void onClick(View v) {
        final View view = getView();

        if (view == null) return;

        switch (v.getId()) {
            case R.id.fab_menu:
                animateFabMenuAppearance();
                break;
            case R.id.fab_type_address:
                animateFabMenuAppearance();
                openEnterAddressDialog(null);
                break;
            case R.id.fab_open_map:
                openMap(null);
                animateFabMenuAppearance();
                break;
        }

    }

    @Override
    public void OnDialogPositiveClick(DialogInterface dialog, Bundle data) {
        logger("positive click");
        int type = data.getInt(EnterDataDialog.ARGS_TYPE, -1);

        if (type > 0 && type == EnterDataDialog.TYPE_NOTIFY_PHONE) {
            String phone = data.getString(EnterDataDialog.ARGS_DATA, "");

            if (!phone.isEmpty()) {
                setUserPhone(phone);
            }
        } else {
//            String city = data.getString(ChooseCityDialog.ARGS_CITY, "");

//            if (!city.isEmpty()) {
//                setCity(city);
//            } else {
                int index = data.getInt(EnterAddressDialog.ARGS_INDEX, -1);

                String address = data.getString(EnterAddressDialog.ARGS_ADDRESS);
                LatLng latLng = data.getParcelable(EnterAddressDialog.ARGS_LAT_LNG);

                RouteItem item = new RouteItem(address, latLng);

                if (index < 0) {
                    mAdapter.addItem(item);
                } else {
                    mAdapter.updateItem(item, index);
                }
//            }
        }
        dialog.cancel();
    }

    @Override
    public void OnDialogNegativeClick(DialogInterface dialog) {
        dialog.cancel();
    }

    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void dataChanged() {
        if (mAdapter.getItemCount() > 0) {
            getOrderCost();
        } else {
            setOrderCost("0");
        }
        Log.d(TAG, "dataChanged: " + mAdapter.getRoute().toString());
    }

    private void getOrderCost() {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<GetOrderCostResponse> call = client.getOrderCost(new GetOrderCostRequest(Order.getInstance()));
        call.enqueue(new Callback<GetOrderCostResponse>() {
            @Override
            public void onResponse(Call<GetOrderCostResponse> call, Response<GetOrderCostResponse> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
                    Order.getInstance().setCost(response.body().getOrderCost());
                    setOrderCost(String.valueOf(response.body().getOrderCost() + " " + response.body().getCurrency()));
                }
            }

            @Override
            public void onFailure(Call<GetOrderCostResponse> call, Throwable t) {
                mListener.displayProgress(false);
            }
        });
    }

    /**
     * Check whether city was chosen or not.
     * @return true if it was and false - otherwise
     */
//    private boolean isCityChosen() {
//        return Order.getInstance().getCityId() != null;
//    }

//    private void getCities() {
//        ApiClient.getInstance().getCities(new ApiClient.ApiCallback<GetCitiesResponse>() {
//            @Override
//            public void response(GetCitiesResponse response) {
//                if (response.isOK()) {
//                    ChooseCityDialog fragment = ChooseCityDialog.newInstance(response.getCities());
//                    fragment.setTargetFragment(getTarget(), 0);
//                    fragment.show(getFragmentManager(), ChooseCityDialog.TAG);
//                } else {
//
//                }
//            }
//
//            @Override
//            public void error() {
//
//            }
//
//            @Override
//            public void noInternetConnection() {
//                ApiClient.getInstance().showAlert(getActivity());
//            }
//        });
//    }

    private void makeOrder() {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<MakeOrderResponse> call = client.makeOrder(new MakeOrderRequest(Order.getInstance(), UserProfile.getInstance().getFullName(), SharedPreferencesManager.getInstance().loadGcmToken()));
        call.enqueue(new Callback<MakeOrderResponse>() {
            @Override
            public void onResponse(Call<MakeOrderResponse> call, Response<MakeOrderResponse> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
//                    NotificationCompat.Builder builder =
//                            new NotificationCompat.Builder(getContext())
//                            .setSmallIcon(R.drawable.ic_notification)
//                            .setContentTitle("Заказ успешно отправлен")
//                            .setContentText("Ожидайте уведомления или звонка от диспетчера")
//                            .setDefaults(Notification.DEFAULT_SOUND);
//
//                    NotificationManager notificationManager =
//                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(1, builder.build());
                    Order.getInstance().resetOrder();
                    setAdapter();
                } else {

                }
            }

            @Override
            public void onFailure(Call<MakeOrderResponse> call, Throwable t) {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapActivity.REQUEST_ADDRESS) {
            if (resultCode == MapActivity.RESULT_OK) {
                int index = data.getIntExtra(MapActivity.EXTRA_INDEX, -1);

                logger("index - " + index);

                String address = data.getStringExtra(MapActivity.EXTRA_ADDRESS);
                LatLng latLng = data.getParcelableExtra(MapActivity.EXTRA_LAT_LNG);

                RouteItem item = new RouteItem(address,latLng);

                if (index >= 0) {
                    mAdapter.updateItem(item, index);
                } else {
                    mAdapter.addItem(item);
                }
            }
        }
    }

    /**
     * Open dialog to enter an address
     * @param index - need for edit mode, can be null for add mode
     */
    private void openEnterAddressDialog(@Nullable Integer index) {
//        if (isCityChosen()) {
            if (index != null) {
                EnterAddressDialog dialog = EnterAddressDialog.newInstanceEdit(index);
                dialog.setCancelable(true);
                dialog.setTargetFragment(getTarget(), 1);
                dialog.show(getFragmentManager(), EnterAddressDialog.TAG);
            } else {
                EnterAddressDialog dialog = EnterAddressDialog.newInstanceAdd(
                        (mAdapter.getItemCount() > 0) ?
                                R.string.ead_title_end_address : R.string.ead_title_start_address
                );
                dialog.setCancelable(false);
                dialog.setTargetFragment(getTarget(), 1);
                dialog.show(getFragmentManager(), EnterAddressDialog.TAG);
            }
//        } else {
//            getCities();
//        }
    }

    /**
     * Open map to choose an address
     * @param index - need for edit mode, can be null for add mode
     */
    private void openMap(@Nullable Integer index) {
//        if (isCityChosen()) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_INDEX, index);
            startActivityForResult(intent, MapActivity.REQUEST_ADDRESS);
//        } else {
//            getCities();
//        }
    }

//    private void setCity(String city) {
//        tvCity.setText(city);
//    }

    private void setUserPhone(String phone) {
        Order.getInstance().setPhone(phone);
        tvUserPhone.setText(phone);
    }

    private void setOrderCost(String cost) {
        tvOrderCost.setText(cost);
    }

    private Fragment getTarget() {
        return this;
    }

    @Override
    public void onChoose(DialogInterface dialogInterface, Bundle data) {
        dialogInterface.cancel();
        int action = data.getInt(ChooseDialog.EXTRA_ACTION, -1);
        int position = data.getInt(ChooseDialog.EXTRA_POSITION);
        switch (action) {
            case ChooseDialog.ACTION_TYPE:
                openEnterAddressDialog(position);
                break;
            case ChooseDialog.ACTION_MAP:
                openMap(position);
                break;
//            case ChooseDialog.ACTION_CITY:
//                getCities();
//                break;
            case ChooseDialog.ACTION_PHONE:
                EnterDataDialog dialog = EnterDataDialog.newInstance(EnterDataDialog.TYPE_NOTIFY_PHONE);
                dialog.setTargetFragment(getTarget(), 0);
                dialog.show(getFragmentManager(), EnterDataDialog.TAG);
                break;
        }
    }
}
