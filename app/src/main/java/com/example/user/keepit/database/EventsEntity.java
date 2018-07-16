package com.example.user.keepit.database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "events")
public class EventsEntity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date sameDate;

    @Ignore
    public EventsEntity(int id, Date sameDate){
        this.id = id;
        this.sameDate = sameDate;
    }

    public EventsEntity(Date sameDate){
        this.sameDate = sameDate;
    }

    protected EventsEntity(Parcel in) {
        id = in.readInt();
    }

    public static final Creator<EventsEntity> CREATOR = new Creator<EventsEntity>() {
        @Override
        public EventsEntity createFromParcel(Parcel in) {
            return new EventsEntity(in);
        }

        @Override
        public EventsEntity[] newArray(int size) {
            return new EventsEntity[size];
        }
    };

    public Date getSameDate() {
        return sameDate;
    }

    public void setSameDate(Date sameDate) {
        this.sameDate = sameDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }
}
