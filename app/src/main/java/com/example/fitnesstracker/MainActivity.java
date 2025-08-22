package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Exercise LinearLayouts
    private LinearLayout llExercise1, llExercise2, llExercise3;

    // Bottom Navigation
    private LinearLayout llHome, llSettings, llProfile;

    // Top-right history icon
    private ImageView ivHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initViews();

        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        // Exercise items
        llExercise1 = findViewById(R.id.llExercise1);
        llExercise2 = findViewById(R.id.llExercise2);
        llExercise3 = findViewById(R.id.llExercise3);

        // Bottom navigation
        llHome     = findViewById(R.id.llHome);
        llSettings = findViewById(R.id.llSettings);
        llProfile  = findViewById(R.id.llProfile);

        // History icon (added in headerSection)
        ivHistory  = findViewById(R.id.ivHistory);
    }

    private void setClickListeners() {
        // Exercise clicks

        // Arm Training
        llExercise1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ArmTrainingActivity.class);
            startActivity(intent);
        });

        // Leg Training
        llExercise2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LegTrainingActivity.class);
            startActivity(intent);
        });

        // Core Training
        llExercise3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CoreTrainingActivity.class);
            startActivity(intent);
        });

        // Bottom navigation clicks
        llHome.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "You're already on Home!", Toast.LENGTH_SHORT).show()
        );

        // Settings page
        llSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Profile page
        llProfile.setOnClickListener(v -> goToProfile());

        // Open History page (icon at top-right)
        if (ivHistory != null) {
            ivHistory.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        }
    }

    private void goToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press home button to minimize app", Toast.LENGTH_SHORT).show();
    }
}
