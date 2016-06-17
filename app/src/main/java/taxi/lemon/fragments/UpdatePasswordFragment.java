package taxi.lemon.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;

import taxi.lemon.App;
import taxi.lemon.R;
import taxi.lemon.activities.LoginActivity;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.ChangePasswordRequest;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.api.old_api.ApiClient;
import taxi.lemon.utils.SharedPreferencesManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class UpdatePasswordFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = UpdatePasswordFragment.class.getSimpleName();

    private AQuery aq;

    private FragmentsInteractionListener mListener;

    private TextInputLayout tilOldPassword, tilNewPassword, tilConfirmPassword;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilOldPassword.setError(null);
            tilOldPassword.setErrorEnabled(false);
            tilNewPassword.setError(null);
            tilNewPassword.setErrorEnabled(false);
            tilConfirmPassword.setError(null);
            tilConfirmPassword.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        aq.id(R.id.btn_confirm).clicked(this);

        aq.id(R.id.et_old_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_new_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_rep_new_password).getEditText().addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void doBack() {

    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.changeTitle(getString(R.string.upwf_title_update_password));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        tilOldPassword = (TextInputLayout) view.findViewById(R.id.til_old_password);
        tilNewPassword = (TextInputLayout) view.findViewById(R.id.til_new_password);
        tilConfirmPassword = (TextInputLayout) view.findViewById(R.id.til_confirm_password);

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
        switch (v.getId()) {
            case R.id.btn_confirm:
                String oldPassword = aq.id(R.id.et_old_password).getEditable().toString();
                String newPassword = aq.id(R.id.et_new_password).getEditable().toString();
                String confirmPassword = aq.id(R.id.et_rep_new_password).getEditable().toString();

                if (!isFieldsEmpty(oldPassword, newPassword, confirmPassword)) {
                    if (isPasswordsMatch(newPassword, confirmPassword)) {
                        changePassword(oldPassword, newPassword);
                    }
                }
                break;
        }
    }

    private void changePassword(String oldPassword, final String newPassword) {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class, SharedPreferencesManager.getInstance().loadUserLogin(), SharedPreferencesManager.getInstance().loadUserPassword());
        Call<ResponseBody> call = client.changePassword(new ChangePasswordRequest(oldPassword, newPassword, newPassword));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
                    SharedPreferencesManager.getInstance().saveUserPassword("");
                    SharedPreferencesManager.getInstance().saveAutoLoginFlag(false);
                    SharedPreferencesManager.getInstance().saveUserLoggedInFlag(false);

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(App.getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
//        ApiClient.getInstance().changePassword(oldPassword, newPassword, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                mListener.displayProgress(false);
//                if (response.isOK()) {
//                    Log.d(TAG, "response: success");
//                    SharedPreferencesManager.getInstance().saveUserPassword("");
//                    SharedPreferencesManager.getInstance().saveAutoLoginFlag(false);
//                    SharedPreferencesManager.getInstance().saveUserLoggedInFlag(false);
//
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void error() {
//                mListener.displayProgress(false);
//                Log.d(TAG, "error: ");
//            }
//
//            @Override
//            public void noInternetConnection() {
//                mListener.displayProgress(false);
//                ApiClient.getInstance().showAlert(getActivity());
//            }
//        });
    }

    /**
     * Check whether passwords match each other or not
     * @param newPassword new password
     * @param confirmPassword confirmation of the new password
     * @return true if passwords match each other, false - otherwise
     */
    private boolean isPasswordsMatch(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_mismatch));
            return false;
        }

        return true;
    }

    /**
     * Check whether fields are empty or not
     * @param oldPassword old password
     * @param newPassword new password
     * @param confirmPassword confirmation of the new password
     * @return false if fields ain't empty, true - otherwise
     */
    private boolean isFieldsEmpty(String oldPassword, String newPassword, String confirmPassword) {
        TextInputLayout layout = null;

        if (oldPassword.isEmpty()) {
            layout = tilOldPassword;
        } else if (newPassword.isEmpty()) {
            layout = tilNewPassword;
        } else if (confirmPassword.isEmpty()) {
            layout = tilConfirmPassword;
        }

        if (layout != null) {
            layout.setError(getString(R.string.error_field_must_not_be_blank));
            return true;
        } else {
            return false;
        }
    }
}
