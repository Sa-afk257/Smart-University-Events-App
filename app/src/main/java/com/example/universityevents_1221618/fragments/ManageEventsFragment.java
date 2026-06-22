package com.example.universityevents_1221618.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.adapters.AdminEventAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageEventsFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_events, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.manage_events_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadEvents();

        FloatingActionButton addBtn = view.findViewById(R.id.add_event_fab);

        addBtn.setOnClickListener(v -> showAddEventDialog());

        return view;
    }

    private void loadEvents() {
        recyclerView.setAdapter(
                new AdminEventAdapter(
                        dbHelper.getEvents("",null, null, 0, false),
                        getContext(),
                        new AdminEventAdapter.OnAdminEventClick() {
                            @Override
                            public void onEdit(Event event) {
                                showEditEventDialog(event);
                            }

                            @Override
                            public void onDelete(Event event) {
                                confirmDeleteEvent(event);
                            }
                        }
                )
        );
    }

    private void showAddEventDialog() {

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText idEt = new EditText(getContext());
        idEt.setHint("Event ID");

        EditText titleEt = new EditText(getContext());
        titleEt.setHint("Title");

        EditText categoryEt = new EditText(getContext());
        categoryEt.setHint("Category");

        EditText locationEt = new EditText(getContext());
        locationEt.setHint("Location");

        EditText dateEt = new EditText(getContext());
        dateEt.setHint("Date (yyyy-mm-dd)");

        EditText timeEt = new EditText(getContext());
        timeEt.setHint("Time (10:00 AM)");

        EditText seatsEt = new EditText(getContext());
        seatsEt.setHint("Seats");

        EditText descEt = new EditText(getContext());
        descEt.setHint("Description");

        EditText imageEt = new EditText(getContext());
        imageEt.setHint("Image URL");

        layout.addView(idEt);
        layout.addView(titleEt);
        layout.addView(categoryEt);
        layout.addView(locationEt);
        layout.addView(dateEt);
        layout.addView(timeEt);
        layout.addView(seatsEt);
        layout.addView(descEt);
        layout.addView(imageEt);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add Event")
                .setView(layout)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

                String id = idEt.getText().toString().trim();
                String title = titleEt.getText().toString().trim();
                String category = categoryEt.getText().toString().trim();
                String location = locationEt.getText().toString().trim();
                String date = dateEt.getText().toString().trim();
                String time = timeEt.getText().toString().trim();
                String seats = seatsEt.getText().toString().trim();
                String description = descEt.getText().toString().trim();
                String imageUrl = imageEt.getText().toString().trim();

                if (id.isEmpty() || title.isEmpty() || category.isEmpty()
                        || location.isEmpty() || date.isEmpty() || time.isEmpty()
                        || seats.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int eventId;
                int seatsNumber;

                try {
                    eventId = Integer.parseInt(id);
                    seatsNumber = Integer.parseInt(seats);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "ID and seats must be numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (eventId <= 0) {
                    Toast.makeText(getContext(), "Event ID must be positive", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (seatsNumber <= 0) {
                    Toast.makeText(getContext(), "Seats must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                Event event = new Event();
                event.setId(eventId);
                event.setTitle(title);
                event.setCategory(category);
                event.setLocation(location);
                event.setDate(date);
                event.setTime(time);
                event.setSeats(seatsNumber);
                event.setDescription(description);
                event.setImageUrl(imageUrl);

                if (dbHelper.addEvent(event)) {
                    Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
                    loadEvents();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed: Event ID may already exist", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
     private void confirmDeleteEvent(Event event) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (dbHelper.deleteEvent(event.getId())) {
                        Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                        loadEvents();
                    } else {
                        Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showEditEventDialog(Event event) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText titleEt = new EditText(getContext());
        titleEt.setHint("Title");
        titleEt.setText(event.getTitle());

        EditText categoryEt = new EditText(getContext());
        categoryEt.setHint("Category");
        categoryEt.setText(event.getCategory());

        EditText locationEt = new EditText(getContext());
        locationEt.setHint("Location");
        locationEt.setText(event.getLocation());

        EditText dateEt = new EditText(getContext());
        dateEt.setHint("Date");
        dateEt.setText(event.getDate());

        EditText timeEt = new EditText(getContext());
        timeEt.setHint("Time");
        timeEt.setText(event.getTime());

        EditText seatsEt = new EditText(getContext());
        seatsEt.setHint("Seats");
        seatsEt.setText(String.valueOf(event.getSeats()));

        EditText descEt = new EditText(getContext());
        descEt.setHint("Description");
        descEt.setText(event.getDescription());

        EditText imageEt = new EditText(getContext());
        imageEt.setHint("Image URL");
        imageEt.setText(event.getImageUrl());

        layout.addView(titleEt);
        layout.addView(categoryEt);
        layout.addView(locationEt);
        layout.addView(dateEt);
        layout.addView(timeEt);
        layout.addView(seatsEt);
        layout.addView(descEt);
        layout.addView(imageEt);

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Event")
                .setView(layout)
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        event.setTitle(titleEt.getText().toString().trim());
                        event.setCategory(categoryEt.getText().toString().trim());
                        event.setLocation(locationEt.getText().toString().trim());
                        event.setDate(dateEt.getText().toString().trim());
                        event.setTime(timeEt.getText().toString().trim());
                        event.setSeats(Integer.parseInt(seatsEt.getText().toString().trim()));
                        event.setDescription(descEt.getText().toString().trim());
                        event.setImageUrl(imageEt.getText().toString().trim());

                        if (dbHelper.updateEvent(event)) {
                            Toast.makeText(getContext(), "Event updated", Toast.LENGTH_SHORT).show();
                            loadEvents();
                        } else {
                            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Invalid data", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}