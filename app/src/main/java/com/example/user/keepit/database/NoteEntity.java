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
public class NoteEntity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer dayId;
    private Date date;
    private String noteText;
    private String noteTitle;

    protected NoteEntity(Parcel in) {
        id = in.readInt();
        dayId = in.readInt();
        noteText = in.readString();
        noteTitle = in.readString();
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

    @Ignore
    public NoteEntity(int id, Integer dayId, Date date, String noteTitle, String noteText){
        this.id = id;
        this.dayId = dayId;
        this.date = date;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
    }


    public NoteEntity(Integer dayId, Date date, String noteTitle, String noteText){
        this.dayId = dayId;
        this.date = date;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
    }
    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public int getId() {
        return id;
    }

    public void setId(int noteId) {
        this.id = noteId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date deadlineDate) {
        this.date = deadlineDate;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dayId);
        dest.writeString(noteText);
        dest.writeString(noteTitle);
    }
}
