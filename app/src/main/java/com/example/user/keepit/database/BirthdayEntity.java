package com.example.user.keepit.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("id"), @Index("dayId")},
        foreignKeys = @ForeignKey(entity = EventsEntity.class,
                parentColumns = "id",
                childColumns = "dayId",
                onDelete = CASCADE))
public class BirthdayEntity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer dayId;
    private Date date;
    private String birthdaysPersonName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    protected BirthdayEntity(Parcel in) {
        id = in.readInt();
        dayId = in.readInt();
        birthdaysPersonName = in.readString();
    }

    public static final Creator<BirthdayEntity> CREATOR = new Creator<BirthdayEntity>() {
        @Override
        public BirthdayEntity createFromParcel(Parcel in) {
            return new BirthdayEntity(in);
        }

        @Override
        public BirthdayEntity[] newArray(int size) {
            return new BirthdayEntity[size];
        }
    };
    @Ignore
    public BirthdayEntity(int id, Integer dayId, Date date, String birthdaysPersonName){
        this.id = id;
        this.dayId = dayId;
        this.date = date;
        this.birthdaysPersonName = birthdaysPersonName;
    }

    public BirthdayEntity(Integer dayId, Date date, String birthdaysPersonName){
        this.dayId = dayId;
        this.date = date;
        this.birthdaysPersonName = birthdaysPersonName;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date birthdayDate) {
        this.date = birthdayDate;
    }

    public String getBirthdaysPersonName() {
        return birthdaysPersonName;
    }

    public void setBirthdaysPersonName(String birthdaysPersonName) {
        this.birthdaysPersonName = birthdaysPersonName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dayId);
        dest.writeString(birthdaysPersonName);
    }
}
