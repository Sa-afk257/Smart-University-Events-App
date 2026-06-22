package com.example.universityevents_1221618.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.activities.AdminHomeActivity;
import com.example.universityevents_1221618.activities.HomeActivity;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    private TextInputEditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REMEMBER = "remember";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        emailEditText = view.findViewById(R.id.email_login);
        passwordEditText = view.findViewById(R.id.password_login);
        rememberMeCheckBox = view.findViewById(R.id.remember_me_checkbox);
        Button loginButton = view.findViewById(R.id.login_button);

        loadRememberMe();

        loginButton.setOnClickListener(v -> loginUser());

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText() == null ? "" : emailEditText.getText().toString().trim();
        String password = passwordEditText.getText() == null ? "" : passwordEditText.getText().toString().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        User user = dbHelper.checkUser(email, password);

        if (user != null) {
            handleRememberMe(email);
            sessionManager.createLoginSession(user.getId());

            Toast.makeText(requireContext(), "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();

            Intent intent;
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                intent = new Intent(requireActivity(), AdminHomeActivity.class);
            } else {
                intent = new Intent(requireActivity(), HomeActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();

        } else {
            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void loadRememberMe() {
        boolean remember = sharedPreferences.getBoolean(KEY_REMEMBER, false);

        if (remember) {
            emailEditText.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void handleRememberMe(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (rememberMeCheckBox.isChecked()) {
            editor.putString(KEY_EMAIL, email);
            editor.putBoolean(KEY_REMEMBER, true);
        } else {
            editor.remove(KEY_EMAIL);
            editor.remove(KEY_REMEMBER);
        }

        editor.apply();
    }
}