package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

public class DistanceEntity implements Parcelable {

    private String distanceText;
    private double distanceValue;

    public DistanceEntity(String distanceText, double distanceValue) {
        this.distanceText = distanceText;
        this.distanceValue = distanceValue;
    }

    protected DistanceEntity(Parcel in) {
        distanceText = in.readString();
        distanceValue = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(distanceText);
        dest.writeDouble(distanceValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DistanceEntity> CREATOR = new Creator<DistanceEntity>() {
        @Override
        public DistanceEntity createFromParcel(Parcel in) {
            return new DistanceEntity(in);
        }

        @Override
        public DistanceEntity[] newArray(int size) {
            return new DistanceEntity[size];
        }
    };

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public double getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(double distanceValue) {
        this.distanceValue = distanceValue;
    }
}
