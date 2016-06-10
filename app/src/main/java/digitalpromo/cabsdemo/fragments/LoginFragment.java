package digitalpromo.cabsdemo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import digitalpromo.cabsdemo.App;
import digitalpromo.cabsdemo.R;
import digitalpromo.cabsdemo.activities.MainActivity;
import digitalpromo.cabsdemo.activities.RestorePasswordActivity;
import digitalpromo.cabsdemo.api.new_api.ApiTaxiClient;
import digitalpromo.cabsdemo.api.new_api.AuthorizationRequest;
import digitalpromo.cabsdemo.api.new_api.AuthorizationResponse;
import digitalpromo.cabsdemo.api.new_api.ServiceGenerator;
import digitalpromo.cabsdemo.api.old_api.ApiClient;
import digitalpromo.cabsdemo.api.old_api.BaseResponse;
import digitalpromo.cabsdemo.utils.PhoneUtils;
import digitalpromo.cabsdemo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentsInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends BasePagerFragment implements View.OnClickListener {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private AQuery aq;

    private TextInputLayout tilLogin;
    private TextInputLayout tilPassword;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilLogin.setError(null);
            tilLogin.setErrorEnabled(false);
            tilPassword.setError(null);
            tilPassword.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        setAuthenticationFields();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        initViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.skip_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.skip:
                openMainActivity(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String login = aq.id(R.id.et_login).getEditable().toString();
                String password = aq.id(R.id.et_password).getEditable().toString();

                if (!hasDigits(login)) {
                    tilLogin.setError(getString(R.string.error_invalid_phone_format));
                    break;
                }

                login = PhoneUtils.replaceNonDigitCharacters(login);

                if (!isFieldsEmpty(login, password)) {
                    if (PhoneUtils.isPhoneValid(login)) {
                        login(login, password);
                    } else {
                        tilLogin.setError(getString(R.string.error_invalid_phone_format));
                    }
                }
                break;
            case R.id.btn_restore_password:
                Intent intent = new Intent(getContext(), RestorePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean hasDigits(String login) {
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(login);
        return m.find();
    }

    /**
     * Check whether fields are empty or not
     * @param login login field
     * @param password password field
     * @return true if fields ain't empty, false - otherwise
     */
    private boolean isFieldsEmpty(String login, String password) {
        TextInputLayout layout = null;
        if (login.isEmpty()) {
            layout = tilLogin;
        } else if (password.isEmpty()) {
            layout = tilPassword;
        }

        if (layout != null) {
            layout.setError(getString(R.string.error_field_must_not_be_blank));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initiate all views of the fragment
     */
    @Override
    protected void initViews() {
        tilLogin = (TextInputLayout) aq.id(R.id.til_login).getView();
        tilPassword = (TextInputLayout) aq.id(R.id.til_password).getView();

        aq.id(R.id.et_login).getEditText().addTextChangedListener(mTextWatcher);
        aq.id(R.id.et_password).getEditText().addTextChangedListener(mTextWatcher);

        aq.id(R.id.btn_restore_password).clicked(this);
        aq.id(R.id.btn_login).clicked(this);
        aq.id(R.id.cb_auto_login).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesManager.getInstance().saveAutoLoginFlag(isChecked);
            }
        });
    }

    @Override
    protected void doBack() {

    }

    /**
     * Start main activity
     * @param isUserLoggedIn extra flag, true - if user was logged, else - false
     */
    private void openMainActivity(boolean isUserLoggedIn) {
        SharedPreferencesManager.getInstance().saveUserLoggedInFlag(isUserLoggedIn);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_IS_USER_LOGGED_IN, isUserLoggedIn);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public String getPageTitle() {
        return App.getContext().getString(R.string.lf_title_sign_in);
    }

    /**
     * Set authentication fields value
     */
    private void setAuthenticationFields() {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance();

        String login = manager.loadUserLogin();
        String password = manager.loadUserPassword();

        boolean isAutoLoginEnabled = manager.loadAutoLoginFlag();

        aq.id(R.id.et_login).text(login);
//        aq.id(R.id.et_password).text(password);

        if (isAutoLoginEnabled) {
            login(login, password);
        }

        aq.id(R.id.cb_auto_login).checked(isAutoLoginEnabled);
    }

    /**
     * Call user auth request
     * @param login user's login
     * @param password user's password
     */
    private void login(final String login, final String password) {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<AuthorizationResponse> call = client.authorization(new AuthorizationRequest(login, password));
        call.enqueue(new Callback<AuthorizationResponse>() {
            @Override
            public void onResponse(Call<AuthorizationResponse> call, Response<AuthorizationResponse> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
                    SharedPreferencesManager.getInstance().saveUserLogin(login);
                    SharedPreferencesManager.getInstance().saveUserPassword(password);
                    openMainActivity(true);
                } else {
                    Toast.makeText(App.getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthorizationResponse> call, Throwable t) {
                mListener.displayProgress(false);
            }
        });
//        ApiClient.getInstance().authorization(login, password, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                mListener.displayProgress(false);
//                if (response.isOK()) {
//                    SharedPreferencesManager.getInstance().saveUserLogin(login);
//                    SharedPreferencesManager.getInstance().saveUserPassword(password);
//                    openMainActivity(true);
//                } else {
//                    Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void error() {
//                mListener.displayProgress(false);
//            }
//
//            @Override
//            public void noInternetConnection() {
//                mListener.displayProgress(false);
//                ApiClient.getInstance().showAlert(getActivity());
//            }
//        });
    }

}
