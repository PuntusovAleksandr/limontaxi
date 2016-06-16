package digitalpromo.cabsdemo.api.new_api;

import digitalpromo.cabsdemo.models.UserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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

    @GET("geodata/streets/search")
    Call<GetAutoCompleteResponse> getAutocompleteRequest(@Query("q") String search);

    @GET("geodata/search")
    Call<GetAddressResponse> getAddress(@Query("lat") String lat, @Query("lng") String lng);

    @POST("weborders/cost")
    Call<GetOrderCostResponse> getOrderCost(@Body GetOrderCostRequest request);

    @POST("weborders")
    Call<MakeOrderResponse> makeOrder(@Body MakeOrderRequest request);
}