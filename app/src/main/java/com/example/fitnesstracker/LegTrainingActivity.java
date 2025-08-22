package com.example.fitnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LegTrainingActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnStartWorkout;
    private TextView tvExercises, tvMinutes, tvDiffStars, tvDiffLabel;

    private SharedPreferences prefs;
    private static final String SP_NAME = "workout_prefs";
    private static final String KEY_PREFIX = "leg_";

    private final int[] EX_OPTS  = {1, 2, 3};    // Number of exercises
    private final int[] MIN_OPTS = {1, 3, 5};  // Minutes per exercise
    private final String[] DIFF_LABELS = {"Easy", "Medium", "Hard"};
    private final String[] DIFF_STARS  = {"★☆☆", "★★☆", "★★★"};

    private int exIdx = 0, minIdx = 1, diffIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leg_training);

        prefs = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        btnBack = findViewById(R.id.btn_back);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        tvExercises = findViewById(R.id.tv_exercises_value);
        tvMinutes   = findViewById(R.id.tv_minutes_value);
        tvDiffStars = findViewById(R.id.tv_diff_stars);
        tvDiffLabel = findViewById(R.id.tv_diff_label);

        int ex   = prefs.getInt(KEY_PREFIX + "exercises", EX_OPTS[exIdx]);
        int mins = prefs.getInt(KEY_PREFIX + "minutes",   MIN_OPTS[minIdx]);
        int diff = prefs.getInt(KEY_PREFIX + "difficulty", diffIdx);
        exIdx   = indexOf(EX_OPTS,  ex,   exIdx);
        minIdx  = indexOf(MIN_OPTS, mins, minIdx);
        diffIdx = Math.max(0, Math.min(2, diff));

        bindUI();

        tvExercises.setOnClickListener(v -> {
            exIdx = (exIdx + 1) % EX_OPTS.length;
            saveAndBind();
            Toast.makeText(this, "Exercises: " + EX_OPTS[exIdx], Toast.LENGTH_SHORT).show();
        });

        tvMinutes.setOnClickListener(v -> {
            minIdx = (minIdx + 1) % MIN_OPTS.length;
            saveAndBind();
            Toast.makeText(this, "Minutes: " + MIN_OPTS[minIdx], Toast.LENGTH_SHORT).show();
        });

        final android.view.View.OnClickListener diffClick = v -> {
            diffIdx = (diffIdx + 1) % 3;
            saveAndBind();
            Toast.makeText(this, "Difficulty: " + DIFF_LABELS[diffIdx], Toast.LENGTH_SHORT).show();
        };
        tvDiffStars.setOnClickListener(diffClick);
        tvDiffLabel.setOnClickListener(diffClick);

        btnBack.setOnClickListener(v -> finish());

        btnStartWorkout.setOnClickListener(v -> {
            Intent i = new Intent(LegTrainingActivity.this, WorkoutSessionActivity.class);
            i.putExtra("session_type", "leg");
            i.putExtra("session_title", "Leg Workout");
            startActivity(i);
        });
    }

    private void bindUI() {
        tvExercises.setText(String.valueOf(EX_OPTS[exIdx]));
        tvMinutes.setText(String.valueOf(MIN_OPTS[minIdx]));
        tvDiffStars.setText(DIFF_STARS[diffIdx]);
        tvDiffLabel.setText(DIFF_LABELS[diffIdx]);
    }

    private void saveAndBind() {
        prefs.edit()
                .putInt(KEY_PREFIX + "exercises", EX_OPTS[exIdx])
                .putInt(KEY_PREFIX + "minutes",   MIN_OPTS[minIdx])
                .putInt(KEY_PREFIX + "difficulty", diffIdx)
                .apply();
        bindUI();
    }

    private int indexOf(int[] arr, int val, int defIdx) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == val) return i;
        return defIdx;
    }
}
