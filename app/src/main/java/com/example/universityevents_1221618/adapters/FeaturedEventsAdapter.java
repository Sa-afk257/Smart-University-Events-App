package com.example.universityevents_1221618.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.List;
import java.util.Locale;

public class FeaturedEventsAdapter extends RecyclerView.Adapter<FeaturedEventsAdapter.ViewHolder> {
    private List<Event> eventList;
    private DatabaseHelper dbHelper;
    private Context context;

    public FeaturedEventsAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_featured_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventPrice.setText(event.getDate() + " • " + event.getTime());
        holder.eventLocation.setText(event.getLocation());

        Glide.with(context)
                .load(event.getImageUrl())
                .placeholder(R.drawable.ic_home)
                .into(holder.eventImage);

        holder.featuredSwitch.setOnCheckedChangeListener(null);
        holder.featuredSwitch.setChecked(dbHelper.isEventFeatured(event.getId()));

        holder.featuredSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.setFeaturedEvent(event.getId(), isChecked);
        });
    }

    @Override
    public int getItemCount() { return eventList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle, eventPrice, eventLocation;
        SwitchMaterial featuredSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.featured_event_image);
            eventTitle = itemView.findViewById(R.id.featured_event_title);
            eventPrice = itemView.findViewById(R.id.featured_event_price);
            eventLocation = itemView.findViewById(R.id.featured_event_location);
            featuredSwitch = itemView.findViewById(R.id.item_featured_switch);
        }
    }
}
