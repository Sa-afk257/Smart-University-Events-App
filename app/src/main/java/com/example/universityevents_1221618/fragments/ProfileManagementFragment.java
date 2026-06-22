package com.example.universityevents_1221618.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.PasswordUtils;
import com.example.universityevents_1221618.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileManagementFragment extends Fragment {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$");

    private CircleImageView profileImage;
    private TextInputEditText firstNameEt, lastNameEt, passwordEt, confirmPasswordEt, phoneEt;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private User currentUser;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null
                                && result.getData().getData() != null) {

                            selectedImageUri = result.getData().getData();
                            profileImage.setImageURI(selectedImageUri);
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_profile_management,
                container,
                false
        );

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        initializeViews(view);
        loadUserData();

        profileImage.setOnClickListener(v -> openImagePicker());
        view.findViewById(R.id.update_profile_button)
                .setOnClickListener(v -> updateUserProfile());

        return view;
    }

    private void initializeViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        firstNameEt = view.findViewById(R.id.first_name_profile);
        lastNameEt = view.findViewById(R.id.last_name_profile);
        passwordEt = view.findViewById(R.id.password_profile);
        confirmPasswordEt = view.findViewById(R.id.confirm_password_profile);
        phoneEt = view.findViewById(R.id.phone_profile);
    }

    private void loadUserData() {
        currentUser = dbHelper.getUserById(sessionManager.getUserId());

        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        firstNameEt.setText(currentUser.getFirstName());
        lastNameEt.setText(currentUser.getLastName());
        phoneEt.setText(currentUser.getPhone());

        if (currentUser.getProfilePicUri() != null
                && !currentUser.getProfilePicUri().isEmpty()) {

            Glide.with(this)
                    .load(Uri.parse(currentUser.getProfilePicUri()))
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void updateUserProfile() {
        if (currentUser == null) return;

        String firstName = firstNameEt.getText() == null ? "" : firstNameEt.getText().toString().trim();
        String lastName = lastNameEt.getText() == null ? "" : lastNameEt.getText().toString().trim();
        String phone = phoneEt.getText() == null ? "" : phoneEt.getText().toString().trim();
        String password = passwordEt.getText() == null ? "" : passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText() == null ? "" : confirmPasswordEt.getText().toString().trim();

        if (firstName.length() < 3 || lastName.length() < 3) {
            Toast.makeText(requireContext(), "Names must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setPhone(phone);

        if (selectedImageUri != null) {
            currentUser.setProfilePicUri(selectedImageUri.toString());
        }

        if (!password.isEmpty()) {
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                Toast.makeText(requireContext(), "Password must be 6+ characters with at least one letter and one number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setPassword(PasswordUtils.hashPassword(password));        }

        if (dbHelper.updateUser(currentUser)) {
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}