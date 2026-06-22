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
import com.example.universityevents_1221618.adapters.DeleteCustomerAdapter;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;

import java.util.List;

public class DeleteCustomersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_delete_customers,
                container,
                false
        );

        RecyclerView recyclerView =
                view.findViewById(R.id.delete_customers_recycler_view);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        DatabaseHelper dbHelper =
                new DatabaseHelper(requireContext());

        List<User> customerList =
                dbHelper.getAllCustomers();

        DeleteCustomerAdapter adapter =
                new DeleteCustomerAdapter(
                        customerList,
                        requireContext()
                );

        recyclerView.setAdapter(adapter);

        return view;
    }
}