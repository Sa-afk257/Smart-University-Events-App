package com.example.universityevents_1221618.adapters;

import android.app.AlertDialog;
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
import com.example.universityevents_1221618.models.Event;
import com.example.universityevents_1221618.models.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservationList;
    private final Context context;

    public ReservationAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        Event event = reservation.getEvent();

        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.reservationDate.setText("Reserved on: " + reservation.getReservationDate());
        holder.quantity.setText("Participants: " + reservation.getQuantity());
        holder.type.setText("Type: " + reservation.getReservationType());
        holder.status.setText("Status: " + reservation.getStatus());

        Glide.with(context)
                .load(event.getImageUrl())
                .placeholder(R.drawable.ic_event)
                .error(R.drawable.ic_event)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> showDetailsDialog(event));
    }

    private void showDetailsDialog(Event event) {
        String details =
                "Category: " + event.getCategory() + "\n" +
                        "Date: " + event.getDate() + "\n" +
                        "Time: " + event.getTime() + "\n" +
                        "Location: " + event.getLocation() + "\n" +
                        "Seats: " + event.getSeats() + "\n\n" +
                        event.getDescription();

        new AlertDialog.Builder(context)
                .setTitle(event.getTitle())
                .setMessage(details)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return reservationList == null ? 0 : reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, location, reservationDate, quantity, type, status;

        ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image_reservation);
            title = itemView.findViewById(R.id.event_title_reservation);
            location = itemView.findViewById(R.id.event_location_reservation);
            reservationDate = itemView.findViewById(R.id.reservation_date);
            quantity = itemView.findViewById(R.id.reservation_quantity);
            type = itemView.findViewById(R.id.reservation_type);
            status = itemView.findViewById(R.id.reservation_status);
        }
    }
}