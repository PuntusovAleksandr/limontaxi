package digitalpromo.cabsdemo.api.old_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Request for registering a new user
 */
public class RegisterUserRequest extends BaseRequest {
    private static final String SERVICE_NAME = USER + "register";

    /**
     * Phone number of the user (same as user's account login)
     */
    @SerializedName("phone")
    private String userPhone;

    /*
        Confirm code from sms
    */
    @SerializedName("confirm_code")
    private String confirmCode;

    /**
     * Password for the user account
     */
    @SerializedName("password")
    private String userPassword;

    /**
     * Confirm password for the user account
     */
    @SerializedName("confirm_password")
    private String userConfirmPassword;

    /**
     * Name of the user
     */
    @SerializedName("user_first_name")
    private String userName;

    /**
     * Construct the instance of the object
     * @param userName user's name
     * @param userPhone user's phone
     * @param userPassword user's password
     */
    public RegisterUserRequest(@NonNull String userName, @NonNull String userPhone, @NonNull String userPassword, @NonNull String userConfirmPassword, @NonNull String confirmCode) {
        super(SERVICE_NAME);
        this.userPhone = userPhone;
        this.confirmCode = confirmCode;
        this.userPassword = userPassword;
        this.userConfirmPassword = userPassword;
        this.userName = userName;
    }
}
