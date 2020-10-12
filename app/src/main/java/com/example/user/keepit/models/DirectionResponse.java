package com.example.user.keepit.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.user.keepit.database.RouteEntity;
import com.example.user.keepit.database.convertors.MyRouteEntityTypeConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResponse implements Parcelable {

    @Expose
    @ColumnInfo(name = "routes")
    @SerializedName("routes")
    @TypeConverters(MyRouteEntityTypeConverter.class)
    private List<RouteEntity> routes;


    protected DirectionResponse(Parcel in) {
        routes = in.createTypedArrayList(RouteEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(routes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DirectionResponse> CREATOR = new Creator<DirectionResponse>() {
        @Override
        public DirectionResponse createFromParcel(Parcel in) {
            return new DirectionResponse(in);
        }

        @Override
        public DirectionResponse[] newArray(int size) {
            return new DirectionResponse[size];
        }
    };

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

}
