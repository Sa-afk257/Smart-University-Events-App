package com.example.universityevents_1221618.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.example.universityevents_1221618.R;
import com.example.universityevents_1221618.db.DatabaseHelper;
import com.example.universityevents_1221618.fragments.*;
import com.example.universityevents_1221618.models.User;
import com.example.universityevents_1221618.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");

        drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader(navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new StatisticsDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_admin_dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_admin_dashboard) selectedFragment = new StatisticsDashboardFragment();
        else if (itemId == R.id.nav_admin_reservations) selectedFragment = new ViewAllReservationsFragment();
        else if (itemId == R.id.nav_admin_featured) selectedFragment = new ManageFeaturedEventsFragment();
        else if (itemId == R.id.nav_admin_delete_customer) selectedFragment = new DeleteCustomersFragment();
        else if (itemId == R.id.nav_admin_add_admin) selectedFragment = new AddNewAdminFragment();
        else if (itemId == R.id.nav_admin_logout) {
            logout();
            return true;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, 0).replace(R.id.admin_fragment_container, selectedFragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateNavHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.nav_header_name);
        TextView headerEmail = headerView.findViewById(R.id.nav_header_email);
        ImageView headerImage = headerView.findViewById(R.id.nav_header_image);
        User currentUser = new DatabaseHelper(this).getUserById(sessionManager.getUserId());
        if (currentUser != null) {
            headerName.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " (Admin)");
            headerEmail.setText(currentUser.getEmail());
            if (currentUser.getProfilePicUri() != null && !currentUser.getProfilePicUri().isEmpty()) {
                Glide.with(this).load(Uri.parse(currentUser.getProfilePicUri())).into(headerImage);
            }
        }
    }

    private void logout() {
        sessionManager.logoutUser();
        Toast.makeText(this, "Admin Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

