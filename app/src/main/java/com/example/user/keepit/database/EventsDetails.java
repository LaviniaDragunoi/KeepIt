package com.example.user.keepit.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class EventsDetails {
    @Embedded
    private EventsEntity dateEvents;

    @Relation(parentColumn = "id", entity = MeetingsEntity.class, entityColumn = "dayId")
    private List<MeetingsEntity> meetings = new ArrayList<>();

    @Relation(parentColumn = "id", entity = BirthdayEntity.class, entityColumn = "dayId")
    private List<BirthdayEntity> birthdays = new ArrayList<>();

    @Relation(parentColumn = "id", entity = NoteEntity.class, entityColumn = "dayId")
    private List<NoteEntity> notes = new ArrayList<>();

    public EventsEntity getDateEvents() {
        return dateEvents;
    }

    public void setDateEvents(EventsEntity dateEvents) {
        this.dateEvents = dateEvents;
    }

    public List<BirthdayEntity> getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(List<BirthdayEntity> birthdays) {
        this.birthdays = birthdays;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
    }

    public List<MeetingsEntity> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<MeetingsEntity> meetings) {
        this.meetings = meetings;
    }
}
