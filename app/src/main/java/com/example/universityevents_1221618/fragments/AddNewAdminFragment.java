package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.PasswordUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class AddNewAdminFragment extends Fragment {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$");

    private TextInputEditText emailEt, firstNameEt, lastNameEt, passwordEt;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_admin, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        emailEt = view.findViewById(R.id.email_add_admin);
        firstNameEt = view.findViewById(R.id.first_name_add_admin);
        lastNameEt = view.findViewById(R.id.last_name_add_admin);
        passwordEt = view.findViewById(R.id.password_add_admin);

        Button addButton = view.findViewById(R.id.add_admin_button);
        addButton.setOnClickListener(v -> createNewAdmin());

        return view;
    }

    private void createNewAdmin() {
        String email = emailEt.getText() == null ? "" : emailEt.getText().toString().trim();
        String firstName = firstNameEt.getText() == null ? "" : firstNameEt.getText().toString().trim();
        String lastName = lastNameEt.getText() == null ? "" : lastNameEt.getText().toString().trim();
        String password = passwordEt.getText() == null ? "" : passwordEt.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(requireContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.length() < 3 || lastName.length() < 3) {
            Toast.makeText(requireContext(), "Names must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(requireContext(), "Password must be 6+ characters with at least one letter and one number", Toast.LENGTH_SHORT).show();
            return;
        }

        User newAdmin = new User();
        newAdmin.setEmail(email);
        newAdmin.setFirstName(firstName);
        newAdmin.setLastName(lastName);
        newAdmin.setPassword(PasswordUtils.hashPassword(password));        newAdmin.setGender("Not specified");
        newAdmin.setMajor("University");
        newAdmin.setPhone("");
        newAdmin.setRole("ADMIN");

        if (dbHelper.addUser(newAdmin)) {
            Toast.makeText(requireContext(), "New admin account created successfully!", Toast.LENGTH_LONG).show();

            emailEt.setText("");
            firstNameEt.setText("");
            lastNameEt.setText("");
            passwordEt.setText("");

        } else {
            Toast.makeText(requireContext(), "Failed to create admin account.", Toast.LENGTH_SHORT).show();
        }
    }
}