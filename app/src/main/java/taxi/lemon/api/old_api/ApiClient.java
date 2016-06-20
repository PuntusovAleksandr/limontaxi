package taxi.lemon.api.old_api;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import taxi.lemon.App;
import taxi.lemon.R;
import taxi.lemon.models.RouteItem;
import taxi.lemon.utils.SharedPreferencesManager;

/**
 * Client for interaction with remote server
 */
public class ApiClient {
    public static final String TAG = ApiClient.class.getSimpleName();

    /**
     * Base url
     */
    private static final String BASE_URL = "http://95.67.73.202:6969/api/";

    /**
     * Connection timeout
     */
    private static final int TIMEOUT = 3000;

    private static ApiClient ourInstance = new ApiClient();

    /**
     * Instance of AQuery object
     */
    private AQuery aq;

    /**
     * Get instance of ApiClient object
     * @return instance of ApiClient object
     */
    public static ApiClient getInstance() {
        return ourInstance;
    }

    /**
     * Construct the instance of the object
     */
    private ApiClient() {
        aq = new AQuery(App.getContext());
    }

//    /**
//     * Do request to google directions api
//     * @param request object with request parameters
//     * @param callback  ajax callback
//     */
//    private void doDirectionsRequest(DirectionsRequest request, AjaxCallback<DirectionsResponse> callback) {
//        Log.d(TAG, "doDirectionsRequest: url - " + request.getUrlRequest());
//        callback.url(request.getUrlRequest());
//        callback.type(DirectionsResponse.class);
//        callback.timeout(TIMEOUT);
//
//        aq.transformer(new ApiResponseTransformer()).ajax(callback);
//    }
//
//    /**
//     * Create instance of ajax callback, object of request and call {@link #doDirectionsRequest(DirectionsRequest, AjaxCallback)}
//     * @param route list of way points
//     * @param callback callback for ui updates
//     */
//    public void getDirection(ArrayList<RouteItem> route, final ApiCallback<DirectionsResponse> callback) {
//        AjaxCallback<DirectionsResponse> cb = new AjaxCallback<DirectionsResponse>() {
//            @Override
//            public void callback(String url, DirectionsResponse response, AjaxStatus status) {
//                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
//                    callback.response(response);
//                } else {
//                    Log.d(TAG, "callback: error - " + status.getError());
//                    if (!hasActiveInternetConnection()) {
//                        callback.noInternetConnection();
//                    } else {
//                        callback.error();
//                    }
//                }
//            }
//        };
//
//        DirectionsRequest request = new DirectionsRequest(route);
//        doDirectionsRequest(request, cb);
//    }
//
    /**
     * Do request to google geocode api
     * @param request object with request parameters
     * @param callback  ajax callback
     */
    private void doGeoCodingRequest(GeoCodingRequest request, AjaxCallback<GeoCodingResponse> callback) {
        Log.d(TAG, "doGeoCodingRequest: url - " + request.getUrlRequest());
        callback.url(request.getUrlRequest());
        callback.type(GeoCodingResponse.class);
        callback.timeout(TIMEOUT);

        aq.transformer(new ApiResponseTransformer()).ajax(callback);
    }
//
//    /**
//     * Create instance of ajax callback, object of request and call {@link #doGeoCodingRequest(GeoCodingRequest, AjaxCallback)}
//     * @param latLng coordinates of way point
//     * @param callback callback for ui updates
//     */
//    public void getFullAddress(LatLng latLng, final ApiCallback<GeoCodingResponse> callback) {
//        AjaxCallback<GeoCodingResponse> cb = new AjaxCallback<GeoCodingResponse>() {
//            @Override
//            public void callback(String url, GeoCodingResponse response, AjaxStatus status) {
//                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
//                    callback.response(response);
//                } else {
//                    Log.d(TAG, "callback: error - " + status.getError());
//                    if (!hasActiveInternetConnection()) {
//                        callback.noInternetConnection();
//                    } else {
//                        callback.error();
//                    }
//                }
//            }
//        };
//
//        ReverseGeoCodingRequest request = new ReverseGeoCodingRequest(latLng);
//        doGeoCodingRequest(request, cb);
//    }
//
    /**
     * Create instance of ajax callback, object of request and call {@link #doGeoCodingRequest(GeoCodingRequest, AjaxCallback)}
     * @param address address of way point
     * @param callback callback for ui updates
     */
    public void getLatLng(String address, final ApiCallback<GeoCodingResponse> callback) {
        AjaxCallback<GeoCodingResponse> cb = new AjaxCallback<GeoCodingResponse>() {
            @Override
            public void callback(String url, GeoCodingResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        StraightGeoCodingRequest request = new StraightGeoCodingRequest(address);
        doGeoCodingRequest(request, cb);
    }

    /**
     * Do request to remote server
     * @param request object with request parameters
     * @param type type of expected response
     * @param callback ajax callback
     * @param addSecureHeader if true - add security header, else no
     * @param <T> BaseResponse or its inheritors
     */
    private <T> void doRequest(BaseRequest request, Class<T> type, AjaxCallback<T> callback, boolean addSecureHeader) {
        Map<String, String> params = new HashMap<>();
        String value = new Gson().toJson(request);
        Gson gson = new Gson();
        params = gson.fromJson(value, params.getClass());

        Log.d(TAG, "doRequest: url - " + BASE_URL + request.getServiceName());
        Log.d(TAG, "doRequest: params - " + params.toString());

        callback.url(
                BASE_URL + request.getServiceName()
        );
        callback.type(type);
        callback.timeout(TIMEOUT);
        callback.header("Accept", "application/json");
        if (addSecureHeader) {
            callback.header("content-dop", getSecureHeader());
        }
        callback.params(params);

//        aq.transformer(new ApiResponseTransformer()).ajax(callback);
    }

    /**
     * Call back for ui updates
     * @param <T> BaseResponse or its inheritors
     */
    public interface ApiCallback<T> {
        /**
         * Call if status code equal to 200
         * @param response object which holds response
         */
        void response(T response);

        /**
         * Call if status code not equal to 200
         */
        void error();

        /**
         * Call if there is no internet connection
         */
        void noInternetConnection();
    }

    /**
     * Implementation of response transforming
     */
    private class ApiResponseTransformer implements Transformer {
        @Override
        public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {
            String object = new String(data);
            T response = new GsonBuilder().create().fromJson(object, type);

            Log.d(TAG, response.getClass().getSimpleName() + "\n" + new GsonBuilder().setPrettyPrinting().create().toJson(response));

            return response;
        }
    }

    /**
     * Request to confirmation code check
     * @param serviceName name of check confirmation code request
     * @param phone phone for confirmation
     * @param callback callback for ui updates
     */
    public void getConfirmationCode(@NonNull String serviceName, @NonNull String phone, final ApiCallback<BaseResponse> callback) {
        AjaxCallback<BaseResponse> cb = new AjaxCallback<BaseResponse>() {
            @Override
            public void callback(String url, BaseResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        GetConfirmationCodeRequest request = new GetConfirmationCodeRequest(serviceName, phone);
        doRequest(request, BaseResponse.class, cb, true);
    }

    /**
     * Request to edit user profile
     * @param firstName user's first name
     * @param middleName user's middle name
     * @param lastName user's last name
     * @param address user's address
     * @param entrance entrance
     * @param callback callback for ui updates
     */
    public void editUserInfo(String firstName, String middleName, String lastName, String address, String entrance, final  ApiCallback<BaseResponse> callback) {
        AjaxCallback<BaseResponse> cb = new AjaxCallback<BaseResponse>() {
            @Override
            public void callback(String url, BaseResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        EditUserInfoRequest request = new EditUserInfoRequest(firstName, middleName, lastName, address, entrance);
        doRequest(request, BaseResponse.class, cb, true);
    }

    /**
     * Request to change user's password
     * @param oldPassword old user's password
     * @param newPassword new user's password
     * @param callback callback for ui updates
     */
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword, final ApiCallback<BaseResponse> callback) {
        AjaxCallback<BaseResponse> cb = new AjaxCallback<BaseResponse>() {
            @Override
            public void callback(String url, BaseResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        ChangePasswordRequest request = new ChangePasswordRequest(SHA512(oldPassword), SHA512(newPassword));
        doRequest(request, BaseResponse.class, cb, true);
    }

    /**
     * Request to change user's phone
     * @param newUserPhone new user's phone
     * @param callback callback for ui updates
     */
    public void changePhone(String newUserPhone, final ApiCallback<BaseResponse> callback) {
        AjaxCallback<BaseResponse> cb = new AjaxCallback<BaseResponse>() {
            @Override
            public void callback(String url, BaseResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        PhoneChangeRequest request = new PhoneChangeRequest(newUserPhone);
        doRequest(request, BaseResponse.class, cb, true);
    }

    /**
     * Request to get user's orders history
     * @param date date of required history
     * @param callback callback for ui updates
     */
    public void getOrdersHistory(String date, final ApiCallback<GetOrdersHistoryResponse> callback) {
        AjaxCallback<GetOrdersHistoryResponse> cb = new AjaxCallback<GetOrdersHistoryResponse>() {
            @Override
            public void callback(String url, GetOrdersHistoryResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        GetOrdersHistoryRequest request = new GetOrdersHistoryRequest(date);
        doRequest(request, GetOrdersHistoryResponse.class, cb, true);
    }

    /**
     * Request to get list of available cities
     * @param callback callback for ui updates
     */
    public void getCities(final ApiCallback<GetCitiesResponse> callback) {
        AjaxCallback<GetCitiesResponse> cb = new AjaxCallback<GetCitiesResponse>() {
            @Override
            public void callback(String url, GetCitiesResponse response, AjaxStatus status) {
                if (status.getCode() == HttpsURLConnection.HTTP_OK && response != null) {
                    callback.response(response);
                } else {
                    Log.d(TAG, "callback: error - " + status.getError());
                    if (!hasActiveInternetConnection()) {
                        callback.noInternetConnection();
                    } else {
                        callback.error();
                    }
                }
            }
        };

        GetCitiesRequest request = new GetCitiesRequest();
        doRequest(request, GetCitiesResponse.class, cb, false);
    }

    /**
     * Request to get autocomplete suggestions
     * @param city city where should do searching of suggestions
     * @param string string for which we need to get autocomplete
     * @return array with autocomplete suggestions or null in case of error
     */
    public ArrayList<RouteItem> getAutocomplete(int city, String string) {
        AjaxCallback<GetAutocompleteResponse> cb = new AjaxCallback<>();

        GetAutocompleteRequest request = new GetAutocompleteRequest(city, string);

        Map<String, String> params = new HashMap<>();
        String value = new Gson().toJson(request);
        params.put("request", value);

        String url = BASE_URL + request.getServiceName();
        cb.url(url);
        cb.type(GetAutocompleteResponse.class);
        cb.params(params);

        aq.transformer(new ApiResponseTransformer()).sync(cb);

        Log.d(TAG, "getAutocomplete: status error - " + cb.getStatus().getError());
        Log.d(TAG, "getAutocomplete: status msg - " + cb.getStatus().getMessage());
        Log.d(TAG, "getAutocomplete: status code - " + cb.getStatus().getCode());

        int statusCode = cb.getStatus().getCode();

        if (statusCode != AjaxStatus.NETWORK_ERROR) {
            GetAutocompleteResponse response = cb.getResult();
            if (response != null && response.isOK()) {
                return response.getAutocomplete();
            }
        }

        return null;
    }

    /**
     * Get security header
     * @return security header
     */
    private String getSecureHeader() {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance();
        String login = manager.loadUserLogin();
        String password = manager.loadUserPassword();

        if (login.isEmpty() || password.isEmpty()) {
            return "";
        } else {
            Log.d(TAG, "getSecureHeader: " + login + "&" + SHA512(password));
            return login + "&" + SHA512(password);
        }
    }

    /**
     * Encode input parameter into SHA512 and return result of encoding
     * @param sha512 string to encode
     * @return encoded string or null in order to NoSuchAlgorithmException
     */
    private String SHA512(String sha512) {
        try {
            java.security.MessageDigest sha5121 = MessageDigest.getInstance("SHA512");
            byte[] array = sha5121.digest(sha512.getBytes());
            //convert the byte to hex format
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check, whether there is an active internet connection or not
     * @return true if it has, false - otherwise
     */
    private boolean hasActiveInternetConnection() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Show alert dialog to offer user to check his wi-fi settings
     * @param activity activity which dialog should be displayed in
     */
    public void showAlert(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        adb.setTitle(R.string.ad_title_no_active_connection);
        adb.setMessage(R.string.ad_message_no_active_connection);
        adb.setPositiveButton(R.string.ad_pos_btn_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        adb.create().show();
    }
}
