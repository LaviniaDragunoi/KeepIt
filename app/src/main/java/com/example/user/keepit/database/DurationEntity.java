package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

public class DurationEntity implements Parcelable {

    private String durationText;
    private double durationValue;

    public DurationEntity(String durationText, double durationValue) {
        this.durationText = durationText;
        this.durationValue = durationValue;
    }

    protected DurationEntity(Parcel in) {
        durationText = in.readString();
        durationValue = in.readDouble();
    }

    public static final Creator<DurationEntity> CREATOR = new Creator<DurationEntity>() {
        @Override
        public DurationEntity createFromParcel(Parcel in) {
            return new DurationEntity(in);
        }

        @Override
        public DurationEntity[] newArray(int size) {
            return new DurationEntity[size];
        }
    };

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public double getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(double durationValue) {
        this.durationValue = durationValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(durationText);
        dest.writeDouble(durationValue);
    }
}
