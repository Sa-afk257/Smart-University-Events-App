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
import com.example.universityevents_1221618.adapters.EventAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.Event;
import com.example.universityevents_1221618.utils.SessionManager;
import java.util.List;

public class YourFavoritesFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_favorites, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler_view);
        TextView noFavoritesText = view.findViewById(R.id.no_favorites_text);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SessionManager sessionManager = new SessionManager(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Event> favoriteList = dbHelper.getFavoriteEvents(sessionManager.getUserId());
        if(favoriteList.isEmpty()){
            noFavoritesText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            EventAdapter adapter = new EventAdapter(favoriteList, getContext());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
