package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.adapters.EventAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;
import java.util.List;

public class EventsMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private EventAdapter eventAdapter;
    private AutoCompleteTextView categoryAutoComplete;
    private EditText locationEditText, seatsEditText;
    private TextView noEventsText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_menu, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.events_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryAutoComplete = view.findViewById(R.id.filter_category_spinner);
        locationEditText = view.findViewById(R.id.filter_location);
        seatsEditText = view.findViewById(R.id.filter_seats);
        noEventsText = view.findViewById(R.id.no_events_text);
        Button filterButton = view.findViewById(R.id.filter_button);

        setupCategoryFilter();
        filterButton.setOnClickListener(v -> loadEvents());
        loadEvents();

        return view;
    }

    private void setupCategoryFilter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.event_types_filter,
                android.R.layout.simple_spinner_dropdown_item
        );
        categoryAutoComplete.setAdapter(adapter);
    }

    private void loadEvents() {
        String category = categoryAutoComplete.getText().toString();
        String location = locationEditText.getText().toString().trim();

        int minSeats = 0;
        try {
            if (!seatsEditText.getText().toString().isEmpty()) {
                minSeats = Integer.parseInt(seatsEditText.getText().toString());
            }
        } catch (NumberFormatException e) {
            minSeats = 0;
        }

        List<Event> eventList = dbHelper.getEvents(category, location, minSeats, false);

        if (eventList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noEventsText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
            eventAdapter = new EventAdapter(eventList, getContext());
            recyclerView.setAdapter(eventAdapter);
        }
    }
}
