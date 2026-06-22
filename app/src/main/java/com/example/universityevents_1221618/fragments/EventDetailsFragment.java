package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;

public class EventDetailsFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "description";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_SEATS = "seats";
    private static final String ARG_IMAGE = "image";

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_ID, event.getId());
        args.putString(ARG_TITLE, event.getTitle());
        args.putString(ARG_DESC, event.getDescription());
        args.putString(ARG_CATEGORY, event.getCategory());
        args.putString(ARG_DATE, event.getDate());
        args.putString(ARG_TIME, event.getTime());
        args.putString(ARG_LOCATION, event.getLocation());
        args.putInt(ARG_SEATS, event.getSeats());
        args.putString(ARG_IMAGE, event.getImageUrl());

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        ImageView image = view.findViewById(R.id.details_image);
        TextView title = view.findViewById(R.id.details_title);
        TextView category = view.findViewById(R.id.details_category);
        TextView dateTime = view.findViewById(R.id.details_date_time);
        TextView location = view.findViewById(R.id.details_location);
        TextView seats = view.findViewById(R.id.details_seats);
        TextView description = view.findViewById(R.id.details_description);
        Button backBtn = view.findViewById(R.id.details_back_btn);

        Bundle args = getArguments();

        if (args != null) {
            title.setText(args.getString(ARG_TITLE));
            category.setText("Category: " + args.getString(ARG_CATEGORY));
            dateTime.setText("Date & Time: " + args.getString(ARG_DATE) + " • " + args.getString(ARG_TIME));
            location.setText("Location: " + args.getString(ARG_LOCATION));
            seats.setText("Seats: " + args.getInt(ARG_SEATS));
            description.setText(args.getString(ARG_DESC));

            Glide.with(requireContext())
                    .load(args.getString(ARG_IMAGE))
                    .placeholder(R.drawable.ic_home)
                    .error(R.drawable.ic_home)
                    .into(image);
        }

        backBtn.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());

        return view;
    }
}