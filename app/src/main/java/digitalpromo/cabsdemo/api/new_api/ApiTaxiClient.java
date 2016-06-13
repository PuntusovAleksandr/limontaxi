package digitalpromo.cabsdemo.api.new_api;

import digitalpromo.cabsdemo.models.UserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
}