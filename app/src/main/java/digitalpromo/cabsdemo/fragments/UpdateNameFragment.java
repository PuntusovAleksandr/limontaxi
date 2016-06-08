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
public class UpdateNameFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = UpdateNameFragment.class.getSimpleName();

    private FragmentsInteractionListener mListener;

    private TextInputLayout tilFirstName;

    private AQuery aq;

    public UpdateNameFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        aq.id(R.id.btn_confirm).clicked(this);

        aq.id(R.id.et_first_name).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilFirstName.setError(null);
                tilFirstName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        UserProfile profile = UserProfile.getInstance();

        aq.id(R.id.et_first_name).text(profile.getFirstName());
        aq.id(R.id.et_middle_name).text(profile.getMiddleName());
        aq.id(R.id.et_last_name).text(profile.getLastName());
    }

    @Override
    protected void doBack() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.changeTitle(getString(R.string.unf_title_update_name));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_name, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        tilFirstName = (TextInputLayout) view.findViewById(R.id.til_first_name);

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
        String firstName = aq.id(R.id.et_first_name).getEditable().toString();
        String middleName = aq.id(R.id.et_middle_name).getEditable().toString();
        String lastName = aq.id(R.id.et_last_name).getEditable().toString();

        if (!firstName.isEmpty()) {
            editUserInfo(
                    firstName,
                    middleName,
                    lastName,
                    UserProfile.getInstance().getAddress(),
                    UserProfile.getInstance().getEntrance());
        } else {
            tilFirstName.setError(getString(R.string.error_field_must_not_be_blank));
        }
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
}
