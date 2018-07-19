package com.example.user.keepit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.keepit.R;
import com.example.user.keepit.database.BirthdayEntity;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.database.NoteEntity;

import java.time.Period;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;

public class ListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MEETING = 0, BIRTHDAY = 1, NOTE = 2, INVALID_OBJECT =-1;
    private List<Object> objectList;
    private Context context;

    public ListsAdapter (Context context, List<Object> objectList){
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof MeetingsEntity) {
            return MEETING;
        } else if (objectList.get(position) instanceof BirthdayEntity) {
            return BIRTHDAY;
        }else if(objectList.get(position) instanceof NoteEntity){
            return NOTE;
        }
        return INVALID_OBJECT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case MEETING:
                View v1 = inflater.inflate(R.layout.meetings_item, parent, false);
                viewHolder = new MeetingViewHolder(v1);
                break;
            case BIRTHDAY:
                View v2 = inflater.inflate(R.layout.birthdays_item, parent, false);
                viewHolder = new BirthdayViewHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.notes_item, parent, false);
                viewHolder = new NoteViewHolder(v3);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case MEETING:
                MeetingViewHolder vh1 = (MeetingViewHolder) holder;
                configureMeeting(vh1, position);
                break;
            case BIRTHDAY:
                BirthdayViewHolder vh2 = (BirthdayViewHolder) holder;
                configureBirthday(vh2, position);
                break;
            default:
                NoteViewHolder vh3 = (NoteViewHolder) holder;
                configureNote(vh3, position);
                break;
        }
    }


    private void configureMeeting(MeetingViewHolder vh1, int position) {
        MeetingsEntity meeting = (MeetingsEntity) objectList.get(position);
        if(meeting != null){
            vh1.getMeetingTitleTV().setText(meeting.getMeetingTitle());
            vh1.getMeetingDateTV().setText(valueOf(meeting.getDate()));
            vh1.getPersonNameMeetingTV().setText(meeting.getMeetingPersonName());
            vh1.getMeetingTimeTV().setText(meeting.getMeetingTime());
            vh1.getMeetingLocationTV().setText(meeting.getMeetingLocation());

        }
    }

    private void configureBirthday(BirthdayViewHolder vh2, int position) {
        BirthdayEntity birthday = (BirthdayEntity) objectList.get(position);
        if (birthday != null) {
            vh2.getCelebratingNameTV().setText(birthday.getBirthdaysPersonName());
            vh2.getBirthdayTV().setText(valueOf(birthday.getDate()));
            String ageString = (valueOf(getAge(birthday.getDate())));
            vh2.getAgeTV().setText(ageString);
        }
    }

    private void configureNote(NoteViewHolder vh3, int position) {
        NoteEntity note = (NoteEntity) objectList.get(position);
        if(note != null){
            vh3.getNoteTitleTV().setText(note.getNoteTitle());
            vh3.getDeadlineDateTV().setText(valueOf(note.getDate()));
        }
    }

    @Override
    public int getItemCount() {
        if (objectList == null) return 0;
        return this.objectList.size();
    }


    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checked_meeting)
        TextView checkedMeeting;
        @BindView(R.id.meetings_title)
        TextView meetingTitleTV;
        @BindView(R.id.person_name)
        TextView personNameMeetingTV;
        @BindView(R.id.location)
        TextView meetingLocationTV;
        @BindView(R.id.meeting_time)
        TextView meetingTimeTV;
        @BindView(R.id.meeting_date)
        TextView meetingDateTV;

        public MeetingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public TextView getMeetingDateTV() {
            return meetingDateTV;
        }

        public void setMeetingDateTV(TextView meetingDateTV) {
            this.meetingDateTV = meetingDateTV;
        }

        public TextView getCheckedMeeting() {
            return checkedMeeting;
        }

        public void setCheckedMeeting(TextView checkedMeeting) {
            this.checkedMeeting = checkedMeeting;
        }

        public TextView getMeetingTitleTV() {
            return meetingTitleTV;
        }

        public void setMeetingTitleTV(TextView meetingTitleTV) {
            this.meetingTitleTV = meetingTitleTV;
        }

        public TextView getPersonNameMeetingTV() {
            return personNameMeetingTV;
        }

        public void setPersonNameMeetingTV(TextView personNameMeetingTV) {
            this.personNameMeetingTV = personNameMeetingTV;
        }

        public TextView getMeetingLocationTV() {
            return meetingLocationTV;
        }

        public void setMeetingLocationTV(TextView meetingLocationTV) {
            this.meetingLocationTV = meetingLocationTV;
        }

        public TextView getMeetingTimeTV() {
            return meetingTimeTV;
        }

        public void setMeetingTimeTV(TextView meetingTimeTV) {
            this.meetingTimeTV = meetingTimeTV;
        }

    }

    public class BirthdayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.celebrating_name)
        TextView celebratingNameTV;
        @BindView(R.id.birth_date)
        TextView birthdayTV;
        @BindView(R.id.age)
        TextView ageTV;
        @BindView(R.id.happy_birthday_message)
        ImageView birthdayMessageTV;


        public BirthdayViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public TextView getCelebratingNameTV() {
            return celebratingNameTV;
        }

        public void setCelebratingNameTV(TextView celebratingNameTV) {
            this.celebratingNameTV = celebratingNameTV;
        }

        public TextView getBirthdayTV() {
            return birthdayTV;
        }

        public void setBirthdayTV(TextView birthdayTV) {
            this.birthdayTV = birthdayTV;
        }

        public TextView getAgeTV() {
            return ageTV;
        }

        public void setAgeTV(TextView ageTV) {
            this.ageTV = ageTV;
        }

        public ImageView getBirthdayMessageTV() {
            return birthdayMessageTV;
        }

        public void setBirthdayMessageTV(ImageView birthdayMessageTV) {
            this.birthdayMessageTV = birthdayMessageTV;
        }

    }


    @SuppressLint("NewApi")
    public int getAge(Date birthdayDate) {

       return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checked_note)
        TextView checkedNoteTV;
        @BindView(R.id.note_title)
        TextView noteTitleTV;
        @BindView(R.id.deadline_date)
        TextView deadlineDateTV;

        public NoteViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public TextView getCheckedNoteTV() {
            return checkedNoteTV;
        }

        public void setCheckedNoteTV(TextView checkedNoteTV) {
            this.checkedNoteTV = checkedNoteTV;
        }

        public TextView getNoteTitleTV() {
            return noteTitleTV;
        }

        public void setNoteTitleTV(TextView noteTitleTV) {
            this.noteTitleTV = noteTitleTV;
        }

        public TextView getDeadlineDateTV() {
            return deadlineDateTV;
        }

        public void setDeadlineDateTV(TextView deadlineDateTV) {
            this.deadlineDateTV = deadlineDateTV;
        }

    }
}
