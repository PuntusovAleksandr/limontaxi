package taxi.lemon.api.new_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 13.06.2016.
 */
public class GetConfirmCodeForChangePhoneRequest {
    @SerializedName("phone")
    private String phone;

    public GetConfirmCodeForChangePhoneRequest(@NonNull String phone) {
        this.phone = phone;
    }
}
