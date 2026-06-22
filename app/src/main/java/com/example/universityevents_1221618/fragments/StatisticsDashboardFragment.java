package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;

public class StatisticsDashboardFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView totalUsersText, totalReservationsText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        totalUsersText = view.findViewById(R.id.stat_total_users);
        totalReservationsText = view.findViewById(R.id.stat_total_reservations);

        loadStatistics();

        return view;
    }

    private void loadStatistics() {
        totalUsersText.setText(String.valueOf(dbHelper.getNumberOfUsers()));
        totalReservationsText.setText(String.valueOf(dbHelper.getNumberOfReservations()));
    }
}