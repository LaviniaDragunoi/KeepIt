package com.example.user.keepit.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoordinatesEntity implements Parcelable {

    @SerializedName("lat")
    @Expose
    private double latitude;
    @SerializedName("lng")
    @Expose
    private double longitude;

    protected CoordinatesEntity(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoordinatesEntity> CREATOR = new Creator<CoordinatesEntity>() {
        @Override
        public CoordinatesEntity createFromParcel(Parcel in) {
            return new CoordinatesEntity(in);
        }

        @Override
        public CoordinatesEntity[] newArray(int size) {
            return new CoordinatesEntity[size];
        }
    };

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
}
