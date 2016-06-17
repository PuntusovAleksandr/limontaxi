package digitalpromo.cabsdemo.activities;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.api.new_api.ApiTaxiClient;
import digitalpromo.cabsdemo.api.new_api.RestorePasswordRequest;
import digitalpromo.cabsdemo.api.new_api.SendConfirmCodeForRestorePasswordRequest;
import digitalpromo.cabsdemo.api.new_api.ServiceGenerator;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.BaseResponse;
import digitalpromo.cabsdemo.utils.PhoneUtils;
import digitalpromo.cabsdemo.utils.SharedPreferencesManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestorePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RestorePasswordActivity.class.getSimpleName();

    private AQuery aq;

    private TextInputLayout tilPhone, tilNewPassword, tilConfirmPassword, tilCode;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilPhone.setError(null);
            tilPhone.setErrorEnabled(false);
            tilNewPassword.setError(null);
            tilNewPassword.setErrorEnabled(false);
            tilConfirmPassword.setError(null);
            tilConfirmPassword.setErrorEnabled(false);
            tilCode.setError(null);
            tilCode.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        aq = new AQuery(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }



        initViews();
    }

    private void initViews() {
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);
        tilNewPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirm_password);
        tilCode = (TextInputLayout) findViewById(R.id.til_code);

        aq.id(R.id.et_phone).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_confirm_password).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_code).getEditText().addTextChangedListener(mTextWatcher);

        aq.id(R.id.ll_code_confirm).visible();
        aq.id(R.id.ll_new_password).gone();

        aq.id(R.id.btn_restore).clicked(this);
        aq.id(R.id.btn_confirm).clicked(this);
    }

    @Override
    public void onClick(View v) {
        String phone = aq.id(R.id.et_phone).getEditable().toString();
        phone = PhoneUtils.replaceNonDigitCharacters(phone);

        switch (v.getId()) {
            case R.id.btn_restore:
                if (!phone.isEmpty()) {
                    if (PhoneUtils.isPhoneValid(phone)) {
                        requestCodeForPasswordRestore(phone);
                    } else {
                        tilPhone.setError(getString(R.string.error_invalid_phone_format));
                    }
                } else {
                    tilPhone.setError(getString(R.string.error_field_must_not_be_blank));
                }
                break;
            case R.id.btn_confirm:
                String code = aq.id(R.id.et_code).getEditable().toString();
                String password = aq.id(R.id.et_password).getEditable().toString();
                String confirmPassword = aq.id(R.id.et_confirm_password).getEditable().toString();

                if (!isFieldsEmpty(code, password, confirmPassword) && isPasswordsMatch(password, confirmPassword)) {
                    restorePassword(phone, code, password);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (aq.id(R.id.ll_code_confirm).getView().getVisibility() != View.VISIBLE) {
            aq.id(R.id.ll_code_confirm).visible();
            aq.id(R.id.ll_new_password).gone();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Check whether fields are empty or not
     * @param code code field
     * @param password password field
     * @param confirmPassword confirm password field
     * @return true if fields ain't empty, false - otherwise
     */
    private boolean isFieldsEmpty(String code, String password, String confirmPassword) {
        TextInputLayout layout = null;
        if (code.isEmpty()) {
            layout = tilCode;
        } else if (password.isEmpty()) {
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

    private void requestCodeForPasswordRestore(String phone) {
        final Activity activity = this;
        aq.id(R.id.pb_progress).visible();
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.getConfirmCodeForPasswordRecovery(new SendConfirmCodeForRestorePasswordRequest(phone));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                aq.id(R.id.pb_progress).gone();
                if(response.isSuccessful()) {
                    aq.id(R.id.ll_code_confirm).gone();
                    aq.id(R.id.ll_new_password).visible();
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                aq.id(R.id.pb_progress).gone();
                ApiClient.getInstance().showAlert(activity);
            }
        });

//        ApiClient.getInstance().getPasswordRecoveryConfirmationCode(phone, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                aq.id(R.id.pb_progress).gone();
//                if (response.isOK()) {
//                    Log.d(TAG, "response: success");
//
//                    aq.id(R.id.ll_code_confirm).gone();
//                    aq.id(R.id.ll_new_password).visible();
//                } else {
//                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void error() {
//                Log.d(TAG, "error: ");
//                aq.id(R.id.pb_progress).gone();
//            }
//
//            @Override
//            public void noInternetConnection() {
//                aq.id(R.id.pb_progress).gone();
//                ApiClient.getInstance().showAlert(activity);
//            }
//        });
    }

    private void restorePassword(String phone, String confirmCode, final String password) {
        aq.id(R.id.pb_progress).visible();
        final Activity activity = this;
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.restorePassword(new RestorePasswordRequest(phone, confirmCode, password, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                aq.id(R.id.pb_progress).gone();
                if(response.isSuccessful()) {
                    SharedPreferencesManager.getInstance().saveUserPassword(password);
                    finish();
                } else {
                    Toast.makeText(App.getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                aq.id(R.id.pb_progress).gone();
                ApiClient.getInstance().showAlert(activity);
            }
        });
//        ApiClient.getInstance().recoverPassword(code, newPassword, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                aq.id(R.id.pb_progress).gone();
//                if (response.isOK()) {
//                    Log.d(TAG, "response: success");
//                    SharedPreferencesManager.getInstance().saveUserPassword(newPassword);
//                    finish();
//                } else {
//                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void error() {
//                aq.id(R.id.pb_progress).gone();
//                Log.d(TAG, "error: ");
//            }
//
//            @Override
//            public void noInternetConnection() {
//                aq.id(R.id.pb_progress).gone();
//                ApiClient.getInstance().showAlert(activity);
//            }
//        });
    }
}
