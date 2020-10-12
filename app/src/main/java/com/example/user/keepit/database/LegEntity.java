package com.example.user.keepit.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverters;

import com.example.user.keepit.database.convertors.MyStepEntityTypeConverter;
import com.example.user.keepit.database.entities.CoordinatesEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LegEntity implements Parcelable {

    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_location")
    @Expose
    private CoordinatesEntity endLocation;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_location")
    @Expose
    private CoordinatesEntity startLocation;
    @SerializedName("steps")
    @Expose
    @TypeConverters(MyStepEntityTypeConverter.class)
    private List<StepEntity> steps;

    public LegEntity(String endAddress, CoordinatesEntity endLocation, String startAddress, CoordinatesEntity startLocation,
                     List<StepEntity> steps) {

        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.steps = steps;
    }


    protected LegEntity(Parcel in) {
        endAddress = in.readString();
        endLocation = in.readParcelable(LatLng.class.getClassLoader());
        startAddress = in.readString();
        startLocation = in.readParcelable(LatLng.class.getClassLoader());
        steps = in.createTypedArrayList(StepEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(endAddress);
        dest.writeParcelable(endLocation, flags);
        dest.writeString(startAddress);
        dest.writeParcelable(startLocation, flags);
        dest.writeTypedList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LegEntity> CREATOR = new Creator<LegEntity>() {
        @Override
        public LegEntity createFromParcel(Parcel in) {
            return new LegEntity(in);
        }

        @Override
        public LegEntity[] newArray(int size) {
            return new LegEntity[size];
        }
    };

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public CoordinatesEntity getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(CoordinatesEntity endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public CoordinatesEntity getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(CoordinatesEntity startLocation) {
        this.startLocation = startLocation;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntity> steps) {
        this.steps = steps;
    }

}


