package taxi.lemon.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describe city
 */
public class City implements Parcelable {
    /**
     * City's id
     */
    private int id;

    /**
     * City's name
     */
    private String name;

    /**
     * Get city's id
     * @return city's id
     */
    public int getId() {
        return id;
    }

    /**
     * Get city's name
     * @return city's name
     */
    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public City() {
    }

    protected City(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
