package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.adapters.AllReservationsAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Reservation;
import com.example.universityevents_1221618.models.User;

import java.util.List;

public class ViewAllReservationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_view_all_reservations,
                container,
                false
        );

        RecyclerView recyclerView =
                view.findViewById(R.id.all_reservations_recycler_view);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        DatabaseHelper dbHelper =
                new DatabaseHelper(requireContext());

        List<Pair<Reservation, User>> reservationList =
                dbHelper.getAllReservationsWithUsers();

        AllReservationsAdapter adapter =
                new AllReservationsAdapter(
                        reservationList,
                        requireContext()
                );

        recyclerView.setAdapter(adapter);

        return view;
    }
}