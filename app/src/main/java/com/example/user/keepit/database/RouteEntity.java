package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverters;

import com.example.user.keepit.database.convertors.MyLegEntityTypeConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteEntity implements Parcelable {
    @SerializedName("legs")
    @Expose
    @TypeConverters(MyLegEntityTypeConverter.class)
    private List<LegEntity> legs;


    protected RouteEntity(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RouteEntity> CREATOR = new Creator<RouteEntity>() {
        @Override
        public RouteEntity createFromParcel(Parcel in) {
            return new RouteEntity(in);
        }

        @Override
        public RouteEntity[] newArray(int size) {
            return new RouteEntity[size];
        }
    };

    public List<LegEntity> getLegs() {
        return legs;
    }

    public void setLegs(List<LegEntity> legs) {
        this.legs = legs;
    }

}
