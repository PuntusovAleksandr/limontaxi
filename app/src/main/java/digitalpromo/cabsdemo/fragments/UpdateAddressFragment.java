package digitalpromo.cabsdemo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.api.ApiClient;
import digitalpromo.cabsdemo.api.BaseResponse;
import digitalpromo.cabsdemo.models.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class UpdateAddressFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = UpdateAddressFragment.class.getSimpleName();

    private FragmentsInteractionListener mListener;

    private AQuery aq;

    private TextInputLayout tilStreet, tilHouse;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilStreet.setError(null);
            tilStreet.setErrorEnabled(false);
            tilHouse.setError(null);
            tilHouse.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public UpdateAddressFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        aq.id(R.id.btn_confirm).clicked(this);

        aq.id(R.id.et_address_from).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_address_number_from).getEditText().addTextChangedListener(mTextWatcher);

        String address = UserProfile.getInstance().getAddress();

        String[] parts = address.split(", ");

        if (parts.length > 1) {
            aq.id(R.id.et_address_from).text(parts[0]);
            aq.id(R.id.et_address_number_from).text(parts[1]);
        }

        aq.id(R.id.et_address_entrance_from).text(UserProfile.getInstance().getEntrance());
    }

    @Override
    protected void doBack() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.changeTitle(getString(R.string.uaf_title_update_address));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_address, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        tilStreet = (TextInputLayout) view.findViewById(R.id.til_street);
        tilHouse = (TextInputLayout) view.findViewById(R.id.til_house);

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
        String street = aq.id(R.id.et_address_from).getEditable().toString();
        String house = aq.id(R.id.et_address_number_from).getEditable().toString();
        String entrance = aq.id(R.id.et_address_entrance_from).getEditable().toString();

        if (!isFieldsEmpty(street, house)) {
            editUserInfo(
                    UserProfile.getInstance().getFirstName(),
                    UserProfile.getInstance().getMiddleName(),
                    UserProfile.getInstance().getLastName(),
                    makeFullAddress(street, house),
                    entrance
            );
        }
    }

    /**
     * Concat street and house with separator
     * @param street street
     * @param house house
     * @return full address
     */
    private String makeFullAddress(String street, String house) {
        return street + ", " + house;
    }

    private void editUserInfo(String firstName, String middleName, String lastName, String address, String entrance) {
        mListener.displayProgress(true);
        ApiClient.getInstance().editUserInfo(firstName, middleName, lastName, address, entrance, new ApiClient.ApiCallback<BaseResponse>() {
            @Override
            public void response(BaseResponse response) {
                mListener.displayProgress(false);
                Log.d(TAG, "response() called with: " + "response = [" + response + "]");
                if (response.isOK()) {
                    getActivity().finish();
                } else {
                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error() {
                Log.d(TAG, "error: ");
                mListener.displayProgress(false);
            }

            @Override
            public void noInternetConnection() {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }

    /**
     * Check whether fields are empty or not
     * @param street street
     * @param house house number
     * @return false if fields ain't empty, true - otherwise
     */
    private boolean isFieldsEmpty(String street, String house) {
        TextInputLayout layout = null;
        if (street.isEmpty()) {
            layout = tilStreet;
        } else if (house.isEmpty()) {
            layout = tilHouse;
        }

        if (layout != null) {
            layout.setError(getString(R.string.error_field_must_not_be_blank));
            return true;
        } else {
            return false;
        }
    }
}
