package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 10.06.2016.
 */
public class EditUserInfoRequest {
    @SerializedName("user_first_name")
    private String userFirstName;

    @SerializedName("user_middle_name")
    private String userMiddleName;

    @SerializedName("user_last_name")
    private String userLastName;

    @SerializedName("route_address_from")
    private String routeAddressFrom;

    @SerializedName("route_address_number_from")
    private String routeAddressNumberFrom;

    @SerializedName("route_address_entrance_from")
    private String routeAddressEntranceFrom;

    @SerializedName("route_address_apartment_from")
    private String routeAddressApartmentFrom;

    public EditUserInfoRequest(String userFirstName, String userMiddleName, String userLastName,
                               String routeAddressFrom, String routeAddressNumberFrom, String routeAddressEntranceFrom, String routeAddressApartmentFrom) {
        this.userFirstName = userFirstName;
        this.userMiddleName = userMiddleName;
        this.userLastName = userLastName;
        this.routeAddressFrom = routeAddressFrom;
        this.routeAddressNumberFrom = routeAddressNumberFrom;
        this.routeAddressEntranceFrom = routeAddressEntranceFrom;
        this.routeAddressApartmentFrom = routeAddressApartmentFrom;
    }
}
