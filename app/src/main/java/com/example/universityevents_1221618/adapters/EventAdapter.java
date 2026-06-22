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
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

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
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = 50;
        layout.setPadding(padding, 20, padding, 10);

        EditText quantityInput = new EditText(context);
        quantityInput.setHint("Participation Count");
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantityInput.setText("1");
        layout.addView(quantityInput);

        Spinner typeSpinner = new Spinner(context);
        String[] types = {"Regular", "VIP", "Workshop", "Volunteer"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                types
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        layout.addView(typeSpinner);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Reservation Form")
                .setMessage("Event: " + event.getTitle())
                .setView(layout)
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_reservations)
                .create();

        dialog.setOnShowListener(d -> {
            Button confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            confirmButton.setOnClickListener(v -> {
                String quantityText = quantityInput.getText().toString().trim();

                if (quantityText.isEmpty()) {
                    quantityInput.setError("Quantity is required");
                    return;
                }

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    quantityInput.setError("Invalid quantity");
                    return;
                }

                if (quantity <= 0) {
                    quantityInput.setError("Quantity must be greater than 0");
                    return;
                }

                if (quantity > event.getSeats()) {
                    quantityInput.setError("Quantity cannot exceed available seats");
                    return;
                }

                String reservationType = typeSpinner.getSelectedItem().toString();
                String currentDate = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                ).format(new Date());

                boolean inserted = dbHelper.addReservation(
                        userId,
                        event.getId(),
                        currentDate,
                        quantity,
                        reservationType
                );

                if (inserted) {
                    Toast.makeText(context, "Event reserved successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Failed to reserve event", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
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
