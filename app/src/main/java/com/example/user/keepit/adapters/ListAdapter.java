package com.example.user.keepit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.keepit.R;
import com.example.user.keepit.activities.EditActivity;
import com.example.user.keepit.database.EventEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String LOG_TAG = ListAdapter.class.getSimpleName();
    private static final String EXTRA_EVENT = "currentEvent";
    private List<EventEntity> eventsList;
    private Context mContext;

    public ListAdapter(Context context, List<EventEntity> eventsList){
        this.mContext = context;
        this.eventsList = eventsList;
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

        TextView checkedTextView = holder.checkedTV;

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
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedTextView.isShown()){
                    checkedTextView.setVisibility(View.GONE);
                }else checkedTextView.setVisibility(View.VISIBLE);
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
        TextView checkedTV;
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

    void setEvent(List<EventEntity> events){
        eventsList = events;
        notifyDataSetChanged();
    }

    /*private String dateToString(Date date){

        String dateFormatted = "";
        String newDate = date.toString().substring(0,10);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date dt = inputFormat.parse(newDate);
            dateFormatted = newFormat.format(dt);
        } catch (ParseException pe) {

            Log.e(LOG_TAG, mContext.getString(R.string.exception_date_format), pe);
        }

        return dateFormatted;
    }*/
}
