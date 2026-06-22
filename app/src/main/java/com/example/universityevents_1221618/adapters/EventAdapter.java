package com.example.universityevents_1221618.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;
import com.example.universityevents_1221618.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> eventList;
    private final Context context;
    private final DatabaseHelper dbHelper;
    private final int userId;

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.userId = new SessionManager(context).getUserId();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
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

        updateFavoriteIcon(holder.favoriteButton, dbHelper.isFavorite(userId, event.getId()));

        holder.favoriteButton.setOnClickListener(v -> {
            boolean isCurrentlyFavorite = dbHelper.isFavorite(userId, event.getId());

            if (isCurrentlyFavorite) {
                dbHelper.removeFavorite(userId, event.getId());
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(userId, event.getId());
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }

            updateFavoriteIcon(holder.favoriteButton, !isCurrentlyFavorite);
        });

        holder.reserveButton.setOnClickListener(v -> showReservationDialog(event));
    }

    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        button.setImageResource(isFavorite ? R.drawable.ic_heart_red : R.drawable.ic_heart_outlined);
    }

    private void showReservationDialog(Event event) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Event Reservation")
                .setMessage("Do you want to reserve a seat for \"" + event.getTitle() + "\"?")
                .setPositiveButton("Reserve", (dialog, which) -> {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

                    if (dbHelper.addReservation(userId, event.getId(), currentDate)) {
                        Toast.makeText(context, "Event reserved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to reserve event", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_reservations)
                .show();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, description, category, dateTime, location, seats;
        ImageButton favoriteButton;
        Button reserveButton;

        EventViewHolder(@NonNull View itemView) {
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
