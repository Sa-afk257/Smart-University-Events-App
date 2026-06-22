package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.adapters.EventAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;

public class ManageFeaturedEventsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list_base, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.base_recycler_view);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventAdapter adapter = new EventAdapter(dbHelper.getEvents("", null, null, 0, true), getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }
}