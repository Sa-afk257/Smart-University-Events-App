package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.adapters.ReservationAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Reservation;
import com.example.universityevents_1221618.utils.SessionManager;
import java.util.List;

public class YourReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noReservationsText;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_your_reservations, container, false);

        recyclerView = view.findViewById(R.id.reservations_recycler_view);
        noReservationsText = view.findViewById(R.id.no_reservations_text);

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadReservations();

        return view;
    }

    private void loadReservations() {
        int userId = sessionManager.getUserId();

        List<Reservation> reservationList = dbHelper.getReservations(userId);

        if (reservationList == null || reservationList.isEmpty()) {
            noReservationsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noReservationsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            ReservationAdapter adapter =
                    new ReservationAdapter(reservationList, requireContext());

            recyclerView.setAdapter(adapter);
        }
    }
}