package taxi.lemon.api.new_api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import taxi.lemon.utils.EncodingSHA512;

/**
 * Created by Администратор on 10.06.2016.
 */
public class AuthorizationRequest {
    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    public AuthorizationRequest(@NonNull String login, @NonNull String password) {
        this.login = login;
        this.password = EncodingSHA512.generateSHA512(password);
    }
}
