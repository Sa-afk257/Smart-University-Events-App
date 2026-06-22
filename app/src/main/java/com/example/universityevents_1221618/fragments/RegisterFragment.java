package com.example.universityevents_1221618.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.activities.AuthActivity;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.PasswordUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-zA-Z])" + ".{6,}" + "$");
    private TextInputLayout emailLayout, firstNameLayout, lastNameLayout, passwordLayout, confirmPasswordLayout, phoneLayout;
    private TextInputEditText emailEditText, firstNameEditText, lastNameEditText, passwordEditText, confirmPasswordEditText, phoneEditText;
    private Spinner genderSpinner, majorSpinner;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        setupSpinners();
        registerButton.setOnClickListener(v -> registerUser());
        return view;
    }

    private void registerUser() {
        if (!validateAllInputs()) {
            return;
        }

        String email = emailEditText.getText().toString().trim();
        if (dbHelper.isEmailExists(email)) {
            emailLayout.setError("Email already registered");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstNameEditText.getText().toString().trim());
        user.setLastName(lastNameEditText.getText().toString().trim());

        String hashedPassword = PasswordUtils.hashPassword(passwordEditText.getText().toString().trim());
        user.setPassword(hashedPassword);

        user.setGender(genderSpinner.getSelectedItem().toString());
        user.setMajor(majorSpinner.getSelectedItem().toString());
        user.setPhone(phoneEditText.getText().toString().trim());
        user.setRole("USER");



        if (dbHelper.addUser(user)) {
            Toast.makeText(getContext(), "Registration successful! Please login.", Toast.LENGTH_LONG).show();
            clearInputFields();
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).switchToLoginTab();
            }
        } else {
            Toast.makeText(getContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        emailEditText.setText("");
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        phoneEditText.setText("");
        genderSpinner.setSelection(0);
        majorSpinner.setSelection(0);
        emailLayout.setError(null);
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
        phoneLayout.setError(null);
    }

    private void initializeViews(View view) {
        emailLayout = view.findViewById(R.id.email_layout_register);
        firstNameLayout = view.findViewById(R.id.first_name_layout);
        lastNameLayout = view.findViewById(R.id.last_name_layout);
        passwordLayout = view.findViewById(R.id.password_layout_register);
        confirmPasswordLayout = view.findViewById(R.id.confirm_password_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);

        emailEditText = view.findViewById(R.id.email_register);
        firstNameEditText = view.findViewById(R.id.first_name);
        lastNameEditText = view.findViewById(R.id.last_name);
        passwordEditText = view.findViewById(R.id.password_register);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password);
        phoneEditText = view.findViewById(R.id.phone);

        genderSpinner = view.findViewById(R.id.gender_spinner);
        majorSpinner = view.findViewById(R.id.major_spinner);
        registerButton = view.findViewById(R.id.register_button);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> majorAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.majors_array, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);
    }

    private boolean validateAllInputs() {
        boolean email = isEmailValid();
        boolean fname = isFirstNameValid();
        boolean lname = isLastNameValid();
        boolean pass = isPasswordValid();
        boolean confirmPass = doPasswordsMatch();
        boolean phone = isPhoneValid();
        return email && fname && lname && pass && confirmPass && phone;
    }

    private boolean isPhoneValid() {
        String phone = phoneEditText.getText().toString().trim();

        if (phone.isEmpty()) {
            phoneLayout.setError("Phone number is required");
            return false;
        }

        if (phone.length() < 7) {
            phoneLayout.setError("Enter a valid phone number");
            return false;
        }

        phoneLayout.setError(null);
        return true;
    }
    private boolean isEmailValid() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) { emailLayout.setError("Email cannot be empty"); return false; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailLayout.setError("Invalid email address"); return false; }
        emailLayout.setError(null); return true;
    }
    private boolean isFirstNameValid() {
        String firstName = firstNameEditText.getText().toString().trim();
        if (firstName.length() < 3) { firstNameLayout.setError("First name must be at least 3 characters"); return false; }
        firstNameLayout.setError(null); return true;
    }
    private boolean isLastNameValid() {
        String lastName = lastNameEditText.getText().toString().trim();
        if (lastName.length() < 3) { lastNameLayout.setError("Last name must be at least 3 characters"); return false; }
        lastNameLayout.setError(null); return true;
    }
    private boolean isPasswordValid() {
        String password = passwordEditText.getText().toString().trim();
        if (!PASSWORD_PATTERN.matcher(password).matches()) { passwordLayout.setError("Password must be 6+ characters with at least one letter and one number"); return false; }
        passwordLayout.setError(null); return true;
    }
    private boolean doPasswordsMatch() {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        if (!password.equals(confirmPassword)) { confirmPasswordLayout.setError("Passwords do not match"); return false; }
        confirmPasswordLayout.setError(null); return true;
    }
}
