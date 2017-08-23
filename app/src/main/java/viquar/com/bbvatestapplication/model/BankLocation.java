package viquar.com.bbvatestapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BankLocation implements Parcelable{
    double latitude;
    double longitude;
    String address;
    String name;
    public BankLocation() {
    }
    public BankLocation(String name, double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    protected BankLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        name = in.readString();
    }

    public static final Creator<BankLocation> CREATOR = new Creator<BankLocation>() {
        @Override
        public BankLocation createFromParcel(Parcel in) {
            return new BankLocation(in);
        }

        @Override
        public BankLocation[] newArray(int size) {
            return new BankLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(address);
        parcel.writeString(name);
    }
}
