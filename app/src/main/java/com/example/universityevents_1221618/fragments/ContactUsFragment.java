package com.example.universityevents_1221618.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universityevents_1221618.R;

public class ContactUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        Button callButton = view.findViewById(R.id.call_us_button);
        Button locateButton = view.findViewById(R.id.locate_us_button);
        Button emailButton = view.findViewById(R.id.email_us_button);

        callButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+970599000000"));
            startActivity(intent);
        });

        locateButton.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:32.0158,35.8709?q=Birzeit University");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });

        emailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:events@birzeit.edu"));
            startActivity(intent);
        });

        return view;
    }
}
