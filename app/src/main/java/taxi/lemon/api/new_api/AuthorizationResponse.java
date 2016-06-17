package taxi.lemon.api.new_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taxi.lemon.models.Discount;

/**
 * Created by Администратор on 10.06.2016.
 */
public class AuthorizationResponse {
    @SerializedName("user_full_name")
    private String fullName = "";

    @SerializedName("user_phone")
    private String phone = "";

    @SerializedName("route_address_from")
    private String routeAddress = "";

    @SerializedName("route_address_number_from")
    private String houseNumber = "";

    @SerializedName("route_address_entrance_from")
    private Integer porch = 0;

    @SerializedName("route_address_apartment_from")
    private Integer apartment = 0;

    @SerializedName("roles")
    private String roles = "";

    @SerializedName("client_sub_cards")
    private List<Integer> subCards = new ArrayList<>();

    @SerializedName("versions")
    private Integer version = 0;

    @SerializedName("discount")
    private Discount discount = new Discount();

    @SerializedName("payment_type")
    private Integer paymentType = 0;

    @SerializedName("client_bonuses")
    private Integer bonuces = 0;

    @Override
    public String toString() {
        return "AuthorizationResponse{" +
                "fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", routeAddress='" + routeAddress + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", porch=" + porch +
                ", apartment=" + apartment +
                ", roles='" + roles + '\'' +
                ", subCards=" + subCards +
                ", version=" + version +
                ", discount=" + discount +
                ", paymentType=" + paymentType +
                ", bonuces=" + bonuces +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRouteAddress() {
        return routeAddress;
    }

    public void setRouteAddress(String routeAddress) {
        this.routeAddress = routeAddress;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getPorch() {
        return porch;
    }

    public void setPorch(Integer porch) {
        this.porch = porch;
    }

    public Integer getApartment() {
        return apartment;
    }

    public void setApartment(Integer apartment) {
        this.apartment = apartment;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<Integer> getSubCards() {
        return subCards;
    }

    public void setSubCards(Integer[] subCards) {
        this.subCards = Arrays.asList(subCards);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getBonuces() {
        return bonuces;
    }

    public void setBonuces(Integer bonuces) {
        this.bonuces = bonuces;
    }
}
