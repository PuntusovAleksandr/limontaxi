package digitalpromo.cabsdemo.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.model.LatLng;

import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.adapters.PlaceAutocompleteAdapter;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.GeoCodingResponse;
import digitalpromo.cabsdemo.models.Order;
import digitalpromo.cabsdemo.models.RouteItem;
import digitalpromo.cabsdemo.views.DelayAutoCompleteTextView;

public class EnterAddressDialog extends DialogFragment implements PlaceAutocompleteAdapter.NetworkError {
    public static final String TAG = EnterAddressDialog.class.getSimpleName();

    public static final String ARGS_TITLE = "DIALOG_TITLE";
    public static final String ARGS_ADDRESS = "ADDRESS";
    public static final String ARGS_LAT_LNG = "LAT_LNG";
    public static final String ARGS_INDEX = "EDIT";

    private DialogButtonsListener mListener;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilAddress.setError(null);
            tilAddress.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private DelayAutoCompleteTextView mAutoCompleteTextView;
    private PlaceAutocompleteAdapter mAdapter;
    private TextInputLayout tilAddress;

    private String address;
    private LatLng latLng;

    private int index;

    /**
     * Create new instance of EnterAddressDialog and take title as argument
     * @param title - title of dialog
     * @return instance of EnterAddressDialog
     */
    public static EnterAddressDialog newInstanceAdd(int title) {
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE, title);

        EnterAddressDialog fragment = new EnterAddressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create new instance of EnterAddressDialog and take index of edited item as argument
     * @param index - index of edited item
     * @return instance of EnterAddressDialog
     */
    public static EnterAddressDialog newInstanceEdit(int index) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INDEX, index);

        EnterAddressDialog fragment = new EnterAddressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Check whether lat and lng valid or not
     * @return true if valid, false - otherwise
     */
    private boolean isLatLngValid() {
        return latLng != null && latLng.latitude != 0 && latLng.longitude != 0;
    }

    /**
     * Create bundle with data
     * @return bundle with address and its coordinates
     */
    private Bundle createBundle() {
        Bundle data = new Bundle();
        data.putString(ARGS_ADDRESS, address);
        data.putParcelable(ARGS_LAT_LNG, latLng);
        data.putInt(ARGS_INDEX, index);
        return data;
    }

    /**
     * Check whether input string contains house number
     * @param string input string
     * @return true if string contains house number, false - otherwise
     */
    private boolean containHouseNumber(String string) {
        return string.matches(".*\\d+[a-zA-Zа-яА-ЯёЁ-]{0,2}");
    }

    /**
     * Check whether input string contains street name
     * @param string input string
     * @return true if string contains street name, false - otherwise
     */
    private boolean containStreetName(String string) {
        return string.matches(".*[a-zA-Zа-яА-ЯёЁ]+(\\s*[a-zA-Zа-яА-ЯёЁ]*)*.*");
    }

    private void getLatLng(String addr) {
        ApiClient.getInstance().getLatLng(addr, new ApiClient.ApiCallback<GeoCodingResponse>() {
            @Override
            public void response(GeoCodingResponse response) {
                if (response.isOK()) {
                    address = response.getAddress();
                    latLng = response.getLatLng();

                    if (isLatLngValid()) {
                        mListener.OnDialogPositiveClick(getDialog(), createBundle());
                    } else {
                        setError(R.string.err_cant_get_lat_lng);
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

    private void setError(int errorRes) {
        latLng = null;
        tilAddress.setError(getString(errorRes));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(ARGS_TITLE, -1);
        index = getArguments().getInt(ARGS_INDEX, -1);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_enter_address, null);

        tilAddress = (TextInputLayout) view.findViewById(R.id.til_address);

        mAutoCompleteTextView = (DelayAutoCompleteTextView) view.findViewById(R.id.actv_address);
        mAutoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAutoCompleteTextView.addTextChangedListener(mTextWatcher);

        mAdapter = new PlaceAutocompleteAdapter(getActivity()/*, Order.getInstance().getCityId()*/);
        mAdapter.setNetworkErrorListener(this);
        mAutoCompleteTextView.setAdapter(mAdapter);

        if (index >= 0) {
            adb.setTitle(getString(R.string.ead_title_edit));
        } else if (title > 0) {
            adb.setTitle(title);
        }

        if (getTargetFragment() instanceof DialogButtonsListener) {
            logger("listener added");
            mListener = (DialogButtonsListener) getTargetFragment();
        } else {
            throw new RuntimeException(getTargetFragment().toString()
                    + " must implement DialogButtonsListener");
        }

        Resources res = getResources();
        adb.setView(
                view,
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_top_margin),
                (int) res.getDimension(R.dimen.dialog_content_horizontal_margin),
                (int) res.getDimension(R.dimen.dialog_content_bottom_margin)
        );
        adb.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logger("negative click");
                mListener.OnDialogNegativeClick(dialog);
            }
        });

        return adb.create();
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final RouteItem item = mAdapter.getItem(position);
            address = item.getAddress() == null ? item.getStreet() : item.getAddress();
            latLng = item.getLatLng();
            Log.i(TAG, "Autocomplete item selected: " + address);

            mAutoCompleteTextView.setText(address);
            mAutoCompleteTextView.setSelection(address.length());

            if (!containHouseNumber(address)) {
                setError(R.string.error_no_street);
            } else {
                mListener.OnDialogPositiveClick(getDialog(), createBundle());
            }
        }
    };



    private void logger(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void noInternet() {
        tilAddress.post(new Runnable() {
            @Override
            public void run() {
                tilAddress.setError(getString(R.string.error_no_internet));
            }
        });
    }
}
