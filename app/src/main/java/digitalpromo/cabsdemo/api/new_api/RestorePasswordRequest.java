package digitalpromo.cabsdemo.api.new_api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 17.06.2016.
 */
public class RestorePasswordRequest {
    @SerializedName("phone")
    private String phone;

    @SerializedName("confirm_code")
    private String confirmCode;

    @SerializedName("password")
    private String password;

    @SerializedName("confirm_password")
    private String confirmPassword;

    public RestorePasswordRequest(String phone, String confirmCode, String password, String confirmPassword) {
        this.phone = phone;
        this.confirmCode = confirmCode;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
