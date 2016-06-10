package digitalpromo.cabsdemo.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Администратор on 10.06.2016.
 */
public class Discount {
    @SerializedName("value")
    Double value = 0.0;

    @SerializedName("unit")
    String unit = "";

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
