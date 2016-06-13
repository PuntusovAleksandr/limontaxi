package digitalpromo.cabsdemo.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * User's profile
 */
public class UserProfile {
    private static final String TAG = UserProfile.class.getSimpleName();

    private static UserProfile ourInstance;

    private ArrayList<DataChanged> subscribers = new ArrayList<>();

    @SerializedName("user_login")
    private String userLogin;

    @SerializedName("user_first_name")
    private String firstName;

    @SerializedName("user_middle_name")
    private String middleName;

    @SerializedName("user_last_name")
    private String lastName;

    @SerializedName("user_phone")
    private String phone;

    @SerializedName("user_balance")
    private Integer userBalance;

    @SerializedName("route_address_from")
    private String userAddressFrom;

    @SerializedName("route_address_number_from")
    private String routeAddressNumberFrom;

    @SerializedName("route_address_entrance_from")
    private Integer routeAddressEntranceFrom;

    @SerializedName("route_address_apartment_from")
    private Integer routeAddressApartmentFrom;

    @SerializedName("orders_count")
    private Integer ordersCount;

    @SerializedName("discount")
    private Discount discount;

    @SerializedName("payment_type")
    private Integer paymentType;

    @SerializedName("client_bonuses")
    private Double clientBonuses;

    /**
     * Interface for data changing notifications
     */
    public interface DataChanged {
        void onDataChanged();
    }

    /**
     * Get instance of the object
     * @return instance
     */
    public static UserProfile getInstance() {
        return ourInstance;
    }

    /**
     * Init instance
     * @param userProfile profile info holder
     */
    public static void initInstance(UserProfile userProfile) {
        ourInstance = new UserProfile(
                userProfile.getUserLogin(),
                userProfile.getFirstName(),
                userProfile.getMiddleName(),
                userProfile.getLastName(),
                userProfile.getPhone(),
                userProfile.getUserBalance(),
                userProfile.getUserAddressFrom(),
                userProfile.getRouteAddressNumberFrom(),
                userProfile.getRouteAddressEntranceFrom(),
                userProfile.getRouteAddressApartmentFrom(),
                userProfile.getOrdersCount(),
                userProfile.getDiscount(),
                userProfile.getPaymentType(),
                userProfile.getClientBonuses()
        );
    }

    /**
     * Update profile data
     * @param userProfile profile info holder
     */
    public void updateData(UserProfile userProfile) {
        this.userLogin = userProfile.getUserLogin();
        this.firstName = userProfile.getFirstName();
        this.middleName = userProfile.getMiddleName();
        this.lastName = userProfile.getLastName();
        this.phone = userProfile.getPhone();
        this.userBalance = userProfile.getUserBalance();
        this.userAddressFrom = userProfile.getUserAddressFrom();
        this.routeAddressNumberFrom = userProfile.getRouteAddressNumberFrom();
        this.routeAddressEntranceFrom = userProfile.getRouteAddressEntranceFrom();
        this.routeAddressApartmentFrom = userProfile.getRouteAddressApartmentFrom();
        this.ordersCount = userProfile.getOrdersCount();
        this.discount = userProfile.getDiscount();
        this.paymentType = userProfile.getPaymentType();
        this.clientBonuses = userProfile.getClientBonuses();

        for (DataChanged subscriber : subscribers) {
            subscriber.onDataChanged();
        }
    }

    /**
     * Construct the instance of the object
     * @param firstName user's first name
     * @param middleName user's middle name
     * @param lastName user's last name
     * @param phone user's phone
     */
    public UserProfile(String userLogin, String firstName, String middleName, String lastName, String phone,
                       Integer userBalance, String userAddressFrom, String routeAddressNumberFrom,
                       Integer routeAddressEntranceFrom, Integer routeAddressApartmentFrom, Integer ordersCount,
                       Discount discount, Integer paymentType, Double clientBonuses) {
        this.userLogin = userLogin;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.userBalance = userBalance;
        this.userAddressFrom = userAddressFrom;
        this.routeAddressNumberFrom = routeAddressNumberFrom;
        this.routeAddressEntranceFrom = routeAddressEntranceFrom;
        this.routeAddressApartmentFrom = routeAddressApartmentFrom;
        this.ordersCount = ordersCount;
        this.discount = discount;
        this.paymentType = paymentType;
        this.clientBonuses = clientBonuses;
    }

    /**
     * Subscribe fragment to notify about instance changing
     * @param subscriber subscriber
     */
    public void subscribe(DataChanged subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribe fragment from notifications
     * @param subscriber victim
     */
    public void unsubscirbe(DataChanged subscriber) {

        subscribers.remove(subscribers.indexOf(subscriber));
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getUserBalance() {
        return userBalance;
    }

    public String getUserAddressFrom() {
        return userAddressFrom;
    }

    public String getRouteAddressNumberFrom() {
        return routeAddressNumberFrom;
    }

    public Integer getRouteAddressEntranceFrom() {
        return routeAddressEntranceFrom;
    }

    public Integer getRouteAddressApartmentFrom() {
        return routeAddressApartmentFrom;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public Discount getDiscount() {
        return discount;
    }

    public Double getClientBonuses() {
        return clientBonuses;
    }

    public String getName() {
        return this.firstName + " " + this.middleName + " " + this.lastName;
    }

    public String getFullAddress() {
        if(this.userAddressFrom == null && this.routeAddressNumberFrom == null
                && this.routeAddressEntranceFrom == null && this.routeAddressApartmentFrom == null)
            return "";

        else return this.userAddressFrom + " " + this.routeAddressNumberFrom + " " + this.routeAddressEntranceFrom + " " + this.routeAddressApartmentFrom;
    }
}
