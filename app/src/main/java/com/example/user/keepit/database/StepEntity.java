package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.user.keepit.database.entities.CoordinatesEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StepEntity implements Parcelable {

    @SerializedName("end_location")
    @Expose
    private CoordinatesEntity endLocation;

    @SerializedName("start_location")
    @Expose
    private CoordinatesEntity startLocation;


    protected StepEntity(Parcel in) {
        endLocation = in.readParcelable(CoordinatesEntity.class.getClassLoader());
        startLocation = in.readParcelable(CoordinatesEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(endLocation, flags);
        dest.writeParcelable(startLocation, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StepEntity> CREATOR = new Creator<StepEntity>() {
        @Override
        public StepEntity createFromParcel(Parcel in) {
            return new StepEntity(in);
        }

        @Override
        public StepEntity[] newArray(int size) {
            return new StepEntity[size];
        }
    };

    public CoordinatesEntity getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(CoordinatesEntity endLocation) {
        this.endLocation = endLocation;
    }

    public CoordinatesEntity getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(CoordinatesEntity startLocation) {
        this.startLocation = startLocation;
    }
}
