package taxi.lemon.api.new_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 10.06.2016.
 */
public class GetConfirmCodeRequest {
    @SerializedName("phone")
    private String userPhone;

    public GetConfirmCodeRequest(@NonNull String userPhone) {
        this.userPhone = userPhone;
    }
}
