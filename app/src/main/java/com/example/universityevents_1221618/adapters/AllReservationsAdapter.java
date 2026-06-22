package com.example.universityevents_1221618.adapters;

import android.content.Context;
import android.util.Pair;
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
import com.example.universityevents_1221618.models.User;

import java.util.List;

public class AllReservationsAdapter extends RecyclerView.Adapter<AllReservationsAdapter.ViewHolder> {
    private List<Pair<Reservation, User>> reservationList;
    private Context context;

    public AllReservationsAdapter(List<Pair<Reservation, User>> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<Reservation, User> item = reservationList.get(position);
        Reservation reservation = item.first;
        User user = item.second;
        Event event = reservation.getEvent();

        holder.eventName.setText(event.getTitle());
        holder.customerName.setText("Reserved by: " + user.getFirstName() + " " + user.getLastName());
        holder.reservationDate.setText("On: " + reservation.getReservationDate());

        // Load event image using Glide
        Glide.with(context)
                .load(event.getImageUrl())
                .placeholder(R.drawable.ic_event)
                .error(R.drawable.ic_event)
                .into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, customerName, reservationDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.item_event_image);
            eventName = itemView.findViewById(R.id.item_event_name);
            customerName = itemView.findViewById(R.id.item_customer_name);
            reservationDate = itemView.findViewById(R.id.item_reservation_date);
        }
    }
}
