package digitalpromo.cabsdemo.api.new_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 13.06.2016.
 */
public class ChangePhoneRequest {
    @SerializedName("phone")
    private String phone;

    @SerializedName("confirm_code")
    private String confirmCode;

    public ChangePhoneRequest(@NonNull String phone, @NonNull String confirmCode) {
        this.phone = phone;
        this.confirmCode = confirmCode;
    }
}
