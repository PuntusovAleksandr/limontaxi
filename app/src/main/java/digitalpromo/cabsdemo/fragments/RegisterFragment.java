package digitalpromo.cabsdemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.BaseResponse;
import digitalpromo.cabsdemo.api.old_api.GetConfirmationCodeRequest;
import digitalpromo.cabsdemo.utils.PhoneUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationSuccessListener} interface
 * to handle registration success event.
 */
public class RegisterFragment extends BasePagerFragment implements View.OnClickListener {
    public static final String TAG = RegisterFragment.class.getSimpleName();

    private AQuery aq;

    private TextInputLayout tilName, tilPhone, tilPassword, tilConfirmPassword, tilCode;

    private RegistrationSuccessListener mRegSuccessListener;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilName.setError(null);
            tilName.setErrorEnabled(false);
            tilPhone.setError(null);
            tilPhone.setErrorEnabled(false);
            tilPassword.setError(null);
            tilPassword.setErrorEnabled(false);
            tilConfirmPassword.setError(null);
            tilConfirmPassword.setErrorEnabled(false);
            tilCode.setError(null);
            tilCode.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * Handle registration success event
     */
    public interface RegistrationSuccessListener {
        /**
         * Call whether registration was complete successfully
         */
        void onRegSuccess();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationSuccessListener) {
            mRegSuccessListener = (RegistrationSuccessListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement RegistrationSuccessListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mRegSuccessListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        tilName = (TextInputLayout) view.findViewById(R.id.til_name);
        tilPhone = (TextInputLayout) view.findViewById(R.id.til_phone);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_password);
        tilConfirmPassword = (TextInputLayout) view.findViewById(R.id.til_confirm_password);
        tilCode = (TextInputLayout) view.findViewById(R.id.til_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String name = aq.id(R.id.et_name).getEditable().toString();
                String phone = aq.id(R.id.et_phone).getEditable().toString();
                String password = aq.id(R.id.et_password).getEditable().toString();
                String confirmPassword = aq.id(R.id.et_confirm_password).getEditable().toString();
                String confirmCode = aq.id(R.id.et_code).getEditable().toString();

                phone = PhoneUtils.replaceNonDigitCharacters(phone);

                if (!isFieldsEmpty(name, phone, password, confirmPassword, confirmCode) && isPasswordsMatch(password, confirmPassword)) {
                    if (PhoneUtils.isPhoneValid(phone)) {
                        register(name, phone, password, confirmPassword, confirmCode);
                    } else {
                        tilPhone.setError(getString(R.string.error_invalid_phone_format));
                    }
                }
                break;
            case R.id.btn_confirm:
                String phone_confirm = aq.id(R.id.et_phone_for_code).getEditable().toString();
                phone_confirm = PhoneUtils.replaceNonDigitCharacters(phone_confirm);
                if(PhoneUtils.isPhoneValid(phone_confirm)) {
                    getConfirmCode(phone_confirm);
                    aq.id(R.id.et_phone).getEditText().setText(phone_confirm);
                    aq.id(R.id.ll_reg_info_container).visible();
                    aq.id(R.id.code_confirm).gone();
                }

//                String code = aq.id(R.id.et_code).getEditable().toString();

//                if (!code.isEmpty()) {
//                    getConfirmCode(Integer.parseInt(code));
//                } else {
//                    tilCode.setError(getString(R.string.error_field_must_not_be_blank));
//                }
                break;
        }
    }

    /**
     * Initiate all views of the fragment
     */
    protected void initViews() {
        aq.id(R.id.ll_reg_info_container).gone();
        aq.id(R.id.code_confirm).visible();

        aq.id(R.id.et_name).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_phone).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_confirm_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_code).getEditText().addTextChangedListener(mTextWatcher);

        aq.id(R.id.btn_register).clicked(this);
        aq.id(R.id.btn_confirm).clicked(this);
    }

    /**
     * Check whether fields are empty or not
     * @param name name field
     * @param phone phone field
     * @param password password field
     * @param confirmPassword confirm password field
     * @return false if fields ain't empty, true - otherwise
     */
    private boolean isFieldsEmpty(String name, String phone, String password, String confirmPassword, String confirmCode) {
        TextInputLayout layout = null;
        if (name.isEmpty()) {
            layout = tilName;
        } else if (phone.isEmpty()) {
            layout = tilPhone;
        } else if (password.isEmpty()) {
            layout = tilPassword;
        } else if (confirmPassword.isEmpty()) {
            layout = tilConfirmPassword;
        } else if (confirmCode.isEmpty()) {
            layout = tilCode;
        }

        if (layout != null) {
            layout.setError(getString(R.string.error_field_must_not_be_blank));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check whether passwords match each other or not
     * @param password password
     * @param confirmPassword confirmation password
     * @return true if passwords match each other, false - otherwise
     */
    private boolean isPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_mismatch));
            return false;
        }

        return true;
    }

    /**
     * Do back action
     */
    protected void doBack() {
        initViews();
    }

    @Override
    public void onBackPressed() {
        if (aq.id(R.id.ll_reg_info_container).getView().getVisibility() != View.VISIBLE) {
            Log.d(TAG, "onBackPressed: !visible");
            doBack();
        } else {

            mRegSuccessListener.onRegSuccess(); // it`s a trick
        }
    }

    @Override
    public String getPageTitle() {
        return App.getContext().getString(R.string.rf_title_registration);
    }

    /**
     * Register a new user
     * @param userName user's name
     * @param userPhone user's phone
     * @param userPassword user's password
     */
    private void register(@NonNull String userName, @NonNull String userPhone, @NonNull String userPassword, @NonNull String userConfirmPassword, @NonNull String confirmCode) {
        mListener.displayProgress(true);
        ApiClient.getInstance().registerUser(userName, userPhone, userPassword, userConfirmPassword, confirmCode, new ApiClient.ApiCallback<BaseResponse>() {
            @Override
            public void response(BaseResponse response) {
                mListener.displayProgress(false);
                if (response.isOK()) {
                    Log.d(TAG, "response: success");
                    doBack();
                    mRegSuccessListener.onRegSuccess();
                } else {
                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error() {
                mListener.displayProgress(false);
                Log.d(TAG, "error: ");
            }

            @Override
            public void noInternetConnection() {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }

    /**
     * Check confirmation code
     * @param phone phone for confirmation
     */
    private void getConfirmCode(@NonNull String phone) {
        mListener.displayProgress(true);
        ApiClient.getInstance().getConfirmationCode(GetConfirmationCodeRequest.REGISTRATION, phone, new ApiClient.ApiCallback<BaseResponse>() {
            @Override
            public void response(BaseResponse response) {
                mListener.displayProgress(false);
                if (response.isOK()) {
                    Log.d(TAG, "response: success");
                } else {
                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error() {
                mListener.displayProgress(false);
                Log.d(TAG, "error: ");
            }

            @Override
            public void noInternetConnection() {
                mListener.displayProgress(false);
                ApiClient.getInstance().showAlert(getActivity());
            }
        });
    }

}
