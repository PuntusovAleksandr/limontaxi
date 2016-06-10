package digitalpromo.cabsdemo.api.new_api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Администратор on 10.06.2016.
 */
public interface ApiTaxiClient {
    @POST("account/register/sendConfirmCode")
    Call<ResponseBody> getConfirmCode(@Body GetConfirmCodeRequest request);
}
