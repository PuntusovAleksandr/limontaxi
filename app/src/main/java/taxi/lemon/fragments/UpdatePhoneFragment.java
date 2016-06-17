package taxi.lemon.fragments;

import android.content.Context;
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
import taxi.lemon.api.new_api.ApiTaxiClient;
import taxi.lemon.api.new_api.ChangePhoneRequest;
import taxi.lemon.api.new_api.GetConfirmCodeForChangePhoneRequest;
import taxi.lemon.api.new_api.ServiceGenerator;
import taxi.lemon.api.old_api.ApiClient;
import taxi.lemon.utils.PhoneUtils;
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
public class UpdatePhoneFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = UpdatePhoneFragment.class.getSimpleName();

    private AQuery aq;

    private FragmentsInteractionListener mListener;

    private TextInputLayout tilNewPhone, tilCode;

    private String newPhone;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tilNewPhone.setError(null);
            tilNewPhone.setErrorEnabled(false);
            tilCode.setError(null);
            tilCode.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * On click listener with logic for the navigation button
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doBack();
        }
    };

    public UpdatePhoneFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initViews() {
        aq.id(R.id.get_code).visible();
        aq.id(R.id.phone_to_code_confirm).gone();

//        aq.id(R.id.et_phone).getEditText().addTextChangedListener(mTextWatcher);
//        aq.id(R.id.et_code).getEditText().addTextChangedListener(mTextWatcher);

        aq.id(R.id.btn_get_code).clicked(this);
        aq.id(R.id.btn_confirm).clicked(this);
    }

    @Override
    protected void doBack() {
        if (aq.id(R.id.get_code).getView().getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            aq.id(R.id.get_code).visible();
            aq.id(R.id.phone_to_code_confirm).gone();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.changeTitle(getString(R.string.upf_title_update_phone));
        mListener.showBackButton(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_phone, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aq = new AQuery(view);

        tilNewPhone = (TextInputLayout) view.findViewById(R.id.til_new_phone);
        tilCode = (TextInputLayout) view.findViewById(R.id.til_code);

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
            case R.id.btn_get_code:
                newPhone = aq.id(R.id.et_phone).getEditable().toString();

                newPhone = PhoneUtils.replaceNonDigitCharacters(newPhone);

                if (!newPhone.isEmpty()) {
                    if (PhoneUtils.isPhoneValid(newPhone)) {
                        getConfirmCode();
                    } else {
                        tilNewPhone.setError(getString(R.string.error_invalid_phone_format));
                    }
                } else {
                    tilNewPhone.setError(getString(R.string.error_field_must_not_be_blank));
                }
                break;
            case R.id.btn_confirm:
                String code = aq.id(R.id.et_phone_for_code).getEditable().toString();
                
                if (!code.isEmpty()) {
                    confirmPhoneChanging(code);
                } else {
                    tilCode.setError(getString(R.string.error_field_must_not_be_blank));
                }
                break;
        }
    }

    private void confirmPhoneChanging(String code) {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.changePhoneRequest(new ChangePhoneRequest(newPhone, code));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
                    SharedPreferencesManager.getInstance().saveUserLogin(newPhone);
                    getActivity().finish();
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
//        ApiClient.getInstance().getConfirmationCode(GetConfirmationCodeRequest.PHONE_CHANGE, code, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                mListener.displayProgress(false);
//                if (response.isOK()) {
//                    Log.d(TAG, "response: success");
//                    SharedPreferencesManager.getInstance().saveUserLogin(newPhone);
//                    getActivity().finish();
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

    private void getConfirmCode() {
        mListener.displayProgress(true);
        ApiTaxiClient client = ServiceGenerator.createTaxiService(ApiTaxiClient.class);
        Call<ResponseBody> call = client.getConfirmCodeForChangePhone(new GetConfirmCodeForChangePhoneRequest(SharedPreferencesManager.getInstance().loadUserLogin()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mListener.displayProgress(false);
                if(response.isSuccessful()) {
                    aq.id(R.id.get_code).gone();
                    aq.id(R.id.phone_to_code_confirm).visible();
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
//        ApiClient.getInstance().changePhone(phone, new ApiClient.ApiCallback<BaseResponse>() {
//            @Override
//            public void response(BaseResponse response) {
//                mListener.displayProgress(false);
//                if (response.isOK()) {
//                    Log.d(TAG, "response: success");
//                    aq.id(R.id.get_code).gone();
//                    aq.id(R.id.code_confirm).visible();
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

    @Override
    public void onBackPressed() {
        doBack();
    }
}
