package com.example.user.keepit.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.user.keepit.database.RouteEntity;
import com.example.user.keepit.database.convertors.MyRouteEntityTypeConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * EventEntity is stored in RoomDb
 */
@Entity(tableName = "events")
public class EventEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "eventType")
    private String eventType;
    private String title;
    private Date date;
    private String dateString;
    private String time;
    private String personName;
    private String meetingLocation;
    private String location;
    private String note;
    // this variable will store only 0 or 1, if the event was checked as done the
    // variable will store the 1 value, otherwise it will store value 0;
    private int done;
    private int age;
    @SerializedName("routes")
    @Expose
    @ColumnInfo(name = "routes")
    @TypeConverters(MyRouteEntityTypeConverter.class)
    private List<RouteEntity> routes;

    public EventEntity(int id, String eventType, String title, Date date, String dateString, String time, String personName,
                       String meetingLocation, String location, String note, int done, int age) {
        this.id = id;
        this.eventType = eventType;
        this.title = title;
        this.date = date;
        this.dateString = dateString;
        this.time = time;
        this.personName = personName;
        this.meetingLocation = meetingLocation;
        this.location = location;
        this.note = note;
        this.done = done;
        this.age = age;
    }

    @Ignore
    public EventEntity(String eventType, String title, Date date, String dateString, String time, String personName,
                       String meetingLocation, String location, String note, int done, int age, List<RouteEntity> routeEntities) {

        this.eventType = eventType;
        this.title = title;
        this.date = date;
        this.dateString = dateString;
        this.time = time;
        this.personName = personName;
        this.meetingLocation = meetingLocation;
        this.location = location;
        this.note = note;
        this.done = done;
        this.age = age;
        this.routes = routeEntities;
    }

    @Ignore
    public EventEntity(String eventType, String title, Date date, String dateString, String time, String personName,
                       String meetingLocation, String location, String note, int done, int age) {
        this.eventType = eventType;
        this.title = title;
        this.date = date;
        this.dateString = dateString;
        this.time = time;
        this.personName = personName;
        this.meetingLocation = meetingLocation;
        this.location = location;
        this.note = note;
        this.done = done;
        this.age = age;
    }

    protected EventEntity(Parcel in) {
        id = in.readInt();
        eventType = in.readString();
        title = in.readString();
        dateString = in.readString();
        time = in.readString();
        personName = in.readString();
        meetingLocation = in.readString();
        location = in.readString();
        note = in.readString();
        done = in.readInt();
        age = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(eventType);
        dest.writeString(title);
        dest.writeString(dateString);
        dest.writeString(time);
        dest.writeString(personName);
        dest.writeString(meetingLocation);
        dest.writeString(location);
        dest.writeString(note);
        dest.writeInt(done);
        dest.writeInt(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventEntity> CREATOR = new Creator<EventEntity>() {
        @Override
        public EventEntity createFromParcel(Parcel in) {
            return new EventEntity(in);
        }

        @Override
        public EventEntity[] newArray(int size) {
            return new EventEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }
}
