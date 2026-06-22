package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Pair;
import java.util.Map;
import java.util.Locale;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;

public class StatisticsDashboardFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView totalUsersText;
    private TextView totalReservationsText;
    private TextView malePercentageText;
    private TextView femalePercentageText;
    private TextView majorStatsText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        totalUsersText = view.findViewById(R.id.stat_total_users);
        totalReservationsText = view.findViewById(R.id.stat_total_reservations);
        malePercentageText = view.findViewById(R.id.stat_male_percentage);
        femalePercentageText = view.findViewById(R.id.stat_female_percentage);
        majorStatsText = view.findViewById(R.id.stat_major_reservations);
        loadStatistics();

        return view;
    }

    private void loadStatistics() {

        totalUsersText.setText(
                String.valueOf(dbHelper.getNumberOfUsers())
        );

        totalReservationsText.setText(
                String.valueOf(dbHelper.getNumberOfReservations())
        );

        Pair<Float, Float> genderStats =
                dbHelper.getGenderPercentage();

        malePercentageText.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f%%",
                        genderStats.first
                )
        );

        femalePercentageText.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f%%",
                        genderStats.second
                )
        );

        Map<String, Integer> majorStats =
                dbHelper.getmajorReservationCounts();

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : majorStats.entrySet()) {

            builder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        majorStatsText.setText(builder.toString());
    }
}