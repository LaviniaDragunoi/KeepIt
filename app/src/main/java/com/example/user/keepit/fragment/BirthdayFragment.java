package com.example.user.keepit.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;
import static java.lang.String.valueOf;

public class BirthdayFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected {
    @BindView(R.id.picker_birth_date)
    TextView birthDate;
    @BindView(R.id.birthday_person_name)
    EditText birthdayPersonNameEditText;
    @BindView(R.id.person_age)
    TextView personAgeTextView;
    private Date birthDateDate;
    private String birthDateString;
    public static final String BIRTHDAY_TYPE = "Birthday";
    private final static int DEFAULT_BIRTHDAY_ID = -1;
    private int mBirthdayId = DEFAULT_BIRTHDAY_ID;
    private AppRoomDatabase roomDb;
    private EditEventViewModel mViewModel;
    private AppExecutors executors;
    private int eventId;
    private String eventType;
    private String title;
    private Date date;
    private String dateString;
    private String time;
    private String personName;
    private String location;
    private String note;

    //Empty constructor;
    public BirthdayFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.birthday_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        eventId = bundle.getInt(EVENT_ENTITY_ID);
        roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
//        EditEventModelFactory mViewModelFactory = new EditEventModelFactory(roomDb,eventId);
//        mViewModel = ViewModelProviders.of(this,mViewModelFactory).get(EditEventViewModel.class);
//        mViewModel.getEvent().observe(this, new Observer<EventEntity>() {
//            @Override
//            public void onChanged(@Nullable EventEntity eventEntity) {
//                mViewModel.getEvent().removeObserver(this);
//                populateUI(eventEntity);
//            }
//        });

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);


            }
        });

        return rootView;
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "date picker");

    }

    private void populateUI(EventEntity eventEntity) {
        if(eventEntity == null){
            return;
        }
        birthDate.setText(eventEntity.getDateString());
        birthdayPersonNameEditText.setText(eventEntity.getPersonName());
        personAgeTextView.setText(getAge(birthDateDate));

    }
    @Override
    public void onDateSelected(Date date, String dateString) {
        birthDateDate = date;
        birthDateString = dateString;
        int age = getAge(date);
        personAgeTextView.setText(valueOf(age));
        birthDate.setText(dateString);
    }

    private int getAge(Date date) {
        return 20;
    }


    //Inflating the menu bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                //delete items from meetings list
                return true;
            case R.id.action_save:
                saveBirthday();
                return true;
            case R.id.action_share:
                //share meeting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveBirthday() {

        eventType = BIRTHDAY_TYPE;
        title = "";
        date = birthDateDate;
        dateString = birthDateString;
        time = "";
        personName = birthdayPersonNameEditText.getText().toString();
        location = "";
        note = "";
        EventEntity birthday = new EventEntity(eventType, title,date, dateString, time, personName,
                location, note);
        int id = birthday.getId();
        if (id == DEFAULT_ID) {
            // insert new event
            mViewModel.addEvent(birthday);
        } else {
            //update event
            birthday.setId(id);
            mViewModel.updateEvent(birthday);
        }

    }
}
