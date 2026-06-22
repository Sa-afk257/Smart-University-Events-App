package com.example.universityevents_1221618.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private static final String API_URL = "https://mocki.io/v1/870cd672-f6a0-439f-a31a-6bc9868d5240";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        requestQueue = Volley.newRequestQueue(this);

        if (sessionManager.isLoggedIn()) {
            User currentUser = dbHelper.getUserById(sessionManager.getUserId());
            if (currentUser != null && "ADMIN".equals(currentUser.getRole())) {
                startActivity(new Intent(this, AdminHomeActivity.class));
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
            finish();
            return;
        }

        Button connectButton = findViewById(R.id.connectButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        connectButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            connectButton.setEnabled(false);
            connectButton.setText("");
            fetchAndStoreData(progressBar, connectButton);
        });
    }

    private void fetchAndStoreData(ProgressBar progressBar, Button connectButton) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    try {
                        JSONObject record = response.getJSONObject("record");
                        JSONArray events = record.getJSONArray("events");
                        dbHelper.addEventsFromJSON(events);
                        createAdminAccountIfNeeded();
                        Toast.makeText(this, "Data Synced Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, AuthActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load events data.", Toast.LENGTH_SHORT).show();
                        resetButton(progressBar, connectButton);
                    }
                },
                error -> {
                    Toast.makeText(this, "Connection Failed. Please check your internet.", Toast.LENGTH_LONG).show();
                    resetButton(progressBar, connectButton);
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void createAdminAccountIfNeeded() {
        if (!dbHelper.isEmailExists("admin@admin.com")) {
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setPassword("Admin123!");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole("ADMIN");
            dbHelper.addUser(admin);
        }
    }

    private void resetButton(ProgressBar progressBar, Button connectButton){
        progressBar.setVisibility(View.GONE);
        connectButton.setEnabled(true);
        connectButton.setText(R.string.connect);
    }
}
