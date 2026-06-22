package com.example.universityevents_1221618.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.models.Event;

import java.util.List;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.ViewHolder> {

    public interface OnAdminEventClick {
        void onEdit(Event event);
        void onDelete(Event event);
    }

    private final List<Event> eventList;
    private final Context context;
    private final OnAdminEventClick listener;

    public AdminEventAdapter(List<Event> eventList, Context context, OnAdminEventClick listener) {
        this.eventList = eventList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.category.setText(event.getCategory());
        holder.dateTime.setText(event.getDate() + " • " + event.getTime());
        holder.location.setText(event.getLocation());
        holder.seats.setText(event.getSeats() + " seats");

        Glide.with(context)
                .load(event.getImageUrl())
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_home)
                .into(holder.imageView);

        holder.favoriteButton.setImageResource(android.R.drawable.ic_menu_edit);
        holder.reserveButton.setText("Delete Event");

        holder.favoriteButton.setOnClickListener(v -> listener.onEdit(event));
        holder.reserveButton.setOnClickListener(v -> listener.onDelete(event));
    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, description, category, dateTime, location, seats;
        ImageButton favoriteButton;
        Button reserveButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            category = itemView.findViewById(R.id.event_category);
            dateTime = itemView.findViewById(R.id.event_date_time);
            location = itemView.findViewById(R.id.event_location);
            seats = itemView.findViewById(R.id.event_seats);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            reserveButton = itemView.findViewById(R.id.reserve_button);
        }
    }
}