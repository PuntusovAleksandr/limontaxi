package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 17.06.2016.
 */
public class SendConfirmCodeForRestorePasswordRequest {
    @SerializedName("phone")
    private String phone;

    public SendConfirmCodeForRestorePasswordRequest(String phone) {
        this.phone = phone;
    }
}
