package taxi.lemon.api.new_api;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taxi.lemon.models.HistoryItem;
import taxi.lemon.models.UserProfile;

/**
 * Created by Администратор on 10.06.2016.
 */
public interface ApiTaxiClient {
    @POST("account/register/sendConfirmCode")
    Call<ResponseBody> getConfirmCode(@Body GetConfirmCodeRequest request);

    @POST("account/register")
    Call<ResponseBody> registerUser(@Body RegisterUserRequest request);

    @POST("account")
    Call<AuthorizationResponse> authorization(@Body AuthorizationRequest request);

    @GET("clients/profile")
    Call<UserProfile> getUserProfile();

    @PUT("clients/profile")
    Call<ResponseBody> editUserInfo(@Body EditUserInfoRequest request);

    @POST("clients/changePhone/sendConfirmCode")
    Call<ResponseBody> getConfirmCodeForChangePhone(@Body GetConfirmCodeForChangePhoneRequest request);

    @PUT("clients/changePhone")
    Call<ResponseBody> changePhoneRequest(@Body ChangePhoneRequest request);

    @PUT("account/changepassword")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest request);

    @Headers("Accept-Language: RU")
    @GET("geodata/search")
    Call<GetAutoCompleteResponse> getAutocompleteRequest(@Query("q") String search, @Query("limit") int limit);

    @Headers("Accept-Language: RU")
    @GET("geodata/streets/search")
    Call<GetAutoCompleteResponse> getStritsAutocompleteRequest(@Query("q") String search);

    @GET("geodata/search")
    Call<GetAddressResponse> getAddress(@Query("lat") String lat, @Query("lng") String lng);

    @POST("weborders/cost")
    Call<GetOrderCostResponse> getOrderCost(@Body GetOrderCostRequest request);

    @POST("weborders")
    Call<MakeOrderResponse> makeOrder(@Body MakeOrderRequest request);

    @GET("clients/ordersreport")
    Call<GetOrdersReportResponse> getOrdersReport(@Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo);

    @GET("clients/ordershistory")
    Call<ArrayList<HistoryItem>> getOrdersHistory();

    @POST("account/restore/sendConfirmCode")
    Call<ResponseBody> getConfirmCodeForPasswordRecovery(@Body SendConfirmCodeForRestorePasswordRequest request);

    @POST("account/restore")
    Call<ResponseBody> restorePassword(@Body RestorePasswordRequest request);

    @PUT("weborders/cancel/{uid}")
    Call<ResponseBody> cancelOrder(@Path("uid") String uid);
}