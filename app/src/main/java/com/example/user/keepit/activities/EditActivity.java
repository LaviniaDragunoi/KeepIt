package com.example.user.keepit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.keepit.R;
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.fragment.BirthdayFragment;
import com.example.user.keepit.fragment.IOnBackPressed;
import com.example.user.keepit.fragment.MapTestMeetingFragment;
import com.example.user.keepit.fragment.MeetingFragment;
import com.example.user.keepit.fragment.NoteFragment;

import java.util.List;
import java.util.Objects;

import static com.example.user.keepit.utils.Constants.BIRTHDAY_TYPE;
import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;
import static com.example.user.keepit.utils.Constants.IS_BIRTHDAY;
import static com.example.user.keepit.utils.Constants.IS_MEETING;
import static com.example.user.keepit.utils.Constants.IS_NOTE;
import static com.example.user.keepit.utils.Constants.MEETING_TYPE;
import static com.example.user.keepit.utils.Constants.NOTE_TYPE;

/**
 * Activity that will store fragments to add and edit events
 */
public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        //the fragment will be created only if the saveInstanceSTate will be null
        if (savedInstanceState == null) {
            if (intent != null) {
                if (intent.hasExtra(EVENT_ENTITY_ID)) {
                    int id = intent.getIntExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                    if (id == DEFAULT_ID) {
                        if (intent.hasExtra(IS_MEETING)) {
                            setTitle(R.string.add_meeting);
                            MeetingFragment addMeetingFragment = new MeetingFragment();
                            MapTestMeetingFragment mapFragment = new MapTestMeetingFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addMeetingFragment.setArguments(bundle);
                            mapFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_meeting_container, addMeetingFragment)
                                    .replace(R.id.fragment_container, mapFragment)
                                    .commit();
                        } else if (intent.hasExtra(IS_BIRTHDAY)) {
                            setTitle(R.string.add_birthday);
                            BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addBirthdayFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_birthday_container, addBirthdayFragment)
                                    .commit();
                        } else if (intent.hasExtra(IS_NOTE)) {
                            setTitle(R.string.add_note);
                            NoteFragment addNoteFragment = new NoteFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addNoteFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_note_container, addNoteFragment)
                                    .commit();
                        }
                    } else if (intent.hasExtra(EXTRA_EVENT)) {
                        EventEntity currentEvent = intent.getParcelableExtra(EXTRA_EVENT);
                        String eventType = currentEvent.getEventType();
                        switch (eventType) {
                            case MEETING_TYPE: {
                                setTitle(R.string.edit_meeting);
                                MeetingFragment addMeetingFragment = new MeetingFragment();
                                MapTestMeetingFragment mapFragment = new MapTestMeetingFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addMeetingFragment.setArguments(bundle);
                                mapFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_meeting_container, addMeetingFragment)
                                        .replace(R.id.fragment_container, mapFragment)
                                        .commit();
                                break;
                            }
                            case BIRTHDAY_TYPE: {
                                setTitle(R.string.edit_birthday);
                                BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addBirthdayFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_birthday_container, addBirthdayFragment)
                                        .commit();
                                break;
                            }
                            case NOTE_TYPE: {
                                setTitle(R.string.edit_note);
                                NoteFragment addNoteFragment = new NoteFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addNoteFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_note_container, addNoteFragment)
                                        .commit();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    //Set title on ACtionBar that is received from fragment
    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    /**
     * Method that will help to pop up a dialog box and tell the user that some changes were made on the event
     * the solution was found on:
     * https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments#46425415
     * and here:
     * https://medium.com/@Wingnut/onbackpressed-for-fragments-357b2bf1ce8e
     */
    @Override
    public void onBackPressed() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
                super.onBackPressed();
            }
            //setup the dialog to warn the user about losing changes already made
            DialogInterface.OnClickListener discard = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                    finish();
                }
            };
            showUnsavedDialog(discard);
        }
    }

    public void showUnsavedDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();

                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnBackPressed {
        void onBackPressed();
    }
}
