package taxi.lemon.fragments;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taxi.lemon.App;
import taxi.lemon.R;
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.GetConfirmCodeRequest;
import taxi.lemon.api.new_api.RegisterUserRequest;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.utils.PhoneUtils;

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
                    if (PhoneUtils.isPhoneValid(phone) ) {
                        if (PhoneUtils.isPassValid(password)) {
                            register(name, phone, password, confirmPassword, confirmCode);
                        } else {
                            Toast.makeText(getActivity(), R.string.error_invalid_pass_format, Toast.LENGTH_LONG).show();
                        }
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
                    aq.id(R.id.phone_to_code_confirm).gone();
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
        aq.id(R.id.phone_to_code_confirm).visible();

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
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.registerUser(new RegisterUserRequest(userName, userPhone, userPassword, userConfirmPassword, confirmCode));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    mListener.displayProgress(false);
                    mRegSuccessListener.onRegSuccess();
                } else {
                    System.out.println("REGISTERED ERROR " + response.raw());
                    Toast.makeText(App.getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.displayProgress(false);
            }
        });
    }

    /**
     * Get confirmation code
     * @param phone phone for confirmation
     */
    private void getConfirmCode(@NonNull String phone) {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.getConfirmCode(new GetConfirmCodeRequest(phone));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mListener.displayProgress(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.displayProgress(false);
            }
        });
    }
}
