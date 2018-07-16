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
public class MeetingsEntity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Integer dayId;
    private Date date;
    private String meetingTitle;
    private String meetingPersonName;
    private String meetingTime;
    private String meetingLocation;

    protected MeetingsEntity(Parcel in) {
        id = in.readInt();
        dayId = in.readInt();
        meetingTitle = in.readString();
        meetingPersonName = in.readString();
        meetingTime = in.readString();
        meetingLocation = in.readString();
    }

    public static final Creator<MeetingsEntity> CREATOR = new Creator<MeetingsEntity>() {
        @Override
        public MeetingsEntity createFromParcel(Parcel in) {
            return new MeetingsEntity(in);
        }

        @Override
        public MeetingsEntity[] newArray(int size) {
            return new MeetingsEntity[size];
        }
    };

    public MeetingsEntity(int id, Integer dayId, Date date, String meetingTitle, String meetingPersonName,
                          String meetingTime, String meetingLocation){
        this.id = id;
        this.dayId = dayId;
        this.date = date;
        this.meetingTitle = meetingTitle;
        this.meetingPersonName = meetingPersonName;
        this.meetingTime = meetingTime;
        this.meetingLocation = meetingLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int meetingId) {
        this.id = meetingId;
    }

    @Ignore
    public MeetingsEntity(Integer dayId, Date date, String meetingTitle, String meetingPersonName,
                          String meetingTime, String meetingLocation){
        this.dayId = dayId;
        this.date = date;
        this.meetingTitle = meetingTitle;
        this.meetingPersonName = meetingPersonName;
        this.meetingTime = meetingTime;
        this.meetingLocation = meetingLocation;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingPersonName() {
        return meetingPersonName;
    }

    public void setMeetingPersonName(String meetingPersonName) {
        this.meetingPersonName = meetingPersonName;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dayId);
        dest.writeString(meetingTitle);
        dest.writeString(meetingPersonName);
        dest.writeString(meetingTime);
        dest.writeString(meetingLocation);
    }
}
