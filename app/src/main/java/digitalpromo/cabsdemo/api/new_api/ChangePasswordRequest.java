package digitalpromo.cabsdemo.api.new_api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 13.06.2016.
 */
public class ChangePasswordRequest {
    @SerializedName("oldPassword")
    private String oldPassword;

    @SerializedName("newPassword")
    private String newPassword;

    @SerializedName("repeatNewPassword")
    private String repeatNewPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword, String repeatNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repeatNewPassword = repeatNewPassword;
    }
}
