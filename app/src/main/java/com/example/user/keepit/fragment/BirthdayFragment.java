package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.user.keepit.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BirthdayFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected {
    @BindView(R.id.picker_birth_date)
    TextView birthDate;
    private Date birthDateDate;

    //Empty constructor;
    public BirthdayFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.birthday_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        birthDate = rootView.findViewById(R.id.picker_birth_date);
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


    @Override
    public void onDateSelected(Date date, String dateString) {
        birthDateDate = date;
        birthDate.setText(dateString);
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



    }
}
