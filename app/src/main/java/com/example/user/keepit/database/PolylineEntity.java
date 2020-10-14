package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

public class PolylineEntity implements Parcelable {

    private String points;

    public PolylineEntity(String points) {
        this.points = points;
    }

    protected PolylineEntity(Parcel in) {
        points = in.readString();
    }

    public static final Creator<PolylineEntity> CREATOR = new Creator<PolylineEntity>() {
        @Override
        public PolylineEntity createFromParcel(Parcel in) {
            return new PolylineEntity(in);
        }

        @Override
        public PolylineEntity[] newArray(int size) {
            return new PolylineEntity[size];
        }
    };

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(points);
    }
}
