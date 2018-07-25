package com.example.user.keepit.adapters;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.activities.EditActivity;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;
import static java.lang.String.valueOf;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String LOG_TAG = ListAdapter.class.getSimpleName();
    public static final String EXTRA_EVENT = "currentEvent";
    public static final String EXTRA_EVENT_ID = "eventId";
    public static final String EVENT_TYPE = "eventType";
    private static final int EVENT_DONE = 1;
    private static final int EVENT_NOT_DONE = 0;
    private List<EventEntity> eventsList;
    private Context mContext;
    private EditEventViewModel viewModel;
    private AppRoomDatabase roomDb;
    private AppExecutors executors;
    private Repository mRepository;
    private EditEventModelFactory factory;

    public ListAdapter(Context context, List<EventEntity> eventsList){
        this.mContext = context;
        this.eventsList = eventsList;
    }

    private void initializeACC(){
        roomDb = AppRoomDatabase.getsInstance(mContext);
        executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao());

    }
    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        EventEntity eventEntity = eventsList.get(position);

        ImageView checkedImageView = holder.checkedTV;
        int isDone = eventEntity.getDone();
        if(isDone == EVENT_NOT_DONE){
            checkedImageView.setImageResource(R.drawable.ic_check_box);
        }else if(isDone == EVENT_DONE){
            checkedImageView.setImageResource(R.drawable.ic_done_accent);
        }
        TextView eventTypeTextView = holder.eventTypeTV;
        String eventTypeString = eventEntity.getEventType();
        eventTypeTextView.setText(eventTypeString);

        TextView eventTitleTextView = holder.titleTV;
        String titleString = eventEntity.getTitle();
        eventTitleTextView.setText(titleString);

        TextView dateTextView = holder.dateTv;
        String dateString = eventEntity.getDateString();
        dateTextView.setText(dateString);

        TextView personNameTextView = holder.personNameTV;
        String personNameString = eventEntity.getPersonName();
        personNameTextView.setText(personNameString);

        ImageView editEventImageView = holder.editEventImg;
        editEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EditActivity.class);
                intent.putExtra(EXTRA_EVENT, eventEntity);
                intent.putExtra(EVENT_ENTITY_ID, eventEntity.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        initializeACC();

        checkedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDone == EVENT_NOT_DONE){
                    eventEntity.setDone(EVENT_DONE);
                    checkedImageView.setImageResource(R.drawable.ic_done_accent);
                    mRepository.updateExistingEvent(eventEntity);
                }else if(isDone == EVENT_DONE){
                    eventEntity.setDone(EVENT_NOT_DONE);
                    checkedImageView.setImageResource(R.drawable.ic_check_box);
                    mRepository.updateExistingEvent(eventEntity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(eventsList == null) return 0;
        return eventsList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checked_TV)
        ImageView checkedTV;
        @BindView(R.id.event_type_TV)
        TextView eventTypeTV;
        @BindView(R.id.title_TV)
        TextView titleTV;
        @BindView(R.id.date_TV)
        TextView dateTv;
        @BindView(R.id.person_name_TV)
        TextView personNameTV;
        @BindView(R.id.edit_event)
        ImageView editEventImg;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setEvent(List<EventEntity> events){
        eventsList = events;
        notifyDataSetChanged();
    }


}
