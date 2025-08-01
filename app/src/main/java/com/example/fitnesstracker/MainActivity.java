package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Exercise LinearLayouts
    private LinearLayout llExercise1, llExercise2, llExercise3;

    // Bottom Navigation
    private LinearLayout llHome, llSettings, llProfile;

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
        llHome = findViewById(R.id.llHome);
        llSettings = findViewById(R.id.llSettings);
        llProfile = findViewById(R.id.llProfile);
    }

    private void setClickListeners() {
        // Exercise clicks
        llExercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the Arm Training page
                Intent intent = new Intent(MainActivity.this, ArmTrainingActivity.class);
                startActivity(intent);
            }
        });

        llExercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExerciseDetail("Leg Training", "Strengthen your legs", "25 mins", 2);
            }
        });

        llExercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExerciseDetail("Core Training", "Strengthen your core", "20 mins", 3);
            }
        });

        // Bottom navigation clicks
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on home, show message
                Toast.makeText(MainActivity.this, "You're already on Home!", Toast.LENGTH_SHORT).show();
            }
        });

        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToProfile();
            }
        });
    }

    private void openExerciseDetail(String exerciseName, String description, String duration, int exerciseId) {
// For other exercises that we haven't created pages for yet
    Toast.makeText(this, "Opening " + exerciseName + " details coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void goToProfile() {
        // We will create the ProfileActivity in the next step
        Toast.makeText(this, "Profile page coming soon!", Toast.LENGTH_SHORT).show();
    }

    // Override back button to prevent going back to login
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
// Display confirmation to exit or minimize the application
    Toast.makeText(this, "Press home button to minimize app", Toast.LENGTH_SHORT).show();
    }
}