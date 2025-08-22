package com.example.fitnesstracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btn_back, ivAvatar;
    private TextView tvDisplayName, tvBMI, tvBMIClass;
    private Button btnEditName, btnSaveMetrics, btnSaveGoal, btnStartArms, btnStartLegs, btnStartCore;
    private EditText etHeight, etWeight, etTargetWeight;
    private RadioGroup rgGoal;
    private RadioButton rbGain, rbLose, rbMaintain;
    private Spinner spWeeklyDays;

    private SharedPreferences prefs;
    private final DecimalFormat df1 = new DecimalFormat("#0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        initViews();
        setupWeeklyDaysSpinner();
        loadSavedData();
        setListeners();
    }

    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvDisplayName = findViewById(R.id.tvDisplayName);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        tvBMI = findViewById(R.id.tvBMI);
        tvBMIClass = findViewById(R.id.tvBMIClass);
        btnSaveMetrics = findViewById(R.id.btnSaveMetrics);

        rgGoal = findViewById(R.id.rgGoal);
        rbGain = findViewById(R.id.rbGain);
        rbLose = findViewById(R.id.rbLose);
        rbMaintain = findViewById(R.id.rbMaintain);
        etTargetWeight = findViewById(R.id.etTargetWeight);
        spWeeklyDays = findViewById(R.id.spWeeklyDays);
        btnSaveGoal = findViewById(R.id.btnSaveGoal);

        btnEditName = findViewById(R.id.btnEditName);
        btnStartArms = findViewById(R.id.btnStartArms);
        btnStartLegs = findViewById(R.id.btnStartLegs);
        btnStartCore = findViewById(R.id.btnStartCore);
    }

    private void setupWeeklyDaysSpinner() {
        String[] days = new String[]{"1","2","3","4","5","6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        spWeeklyDays.setAdapter(adapter);
    }

    private void loadSavedData() {
        // Display name
        String name = prefs.getString("display_name", "");
        tvDisplayName.setText(name.isEmpty() ? "Athlete" : name);

        // Metrics
        double h = parseDouble(prefs.getString("height_cm", ""), 0);
        double w = parseDouble(prefs.getString("weight_kg", ""), 0);
        if (h > 0) etHeight.setText(trimZero(h));
        if (w > 0) etWeight.setText(trimZero(w));
        updateBMIViews(h, w);

        // Goal
        String goal = prefs.getString("primary_goal", "maintain");
        switch (goal) {
            case "gain": rbGain.setChecked(true); break;
            case "lose": rbLose.setChecked(true); break;
            default: rbMaintain.setChecked(true);
        }
        double targetW = parseDouble(prefs.getString("target_weight", ""), 0);
        if (targetW > 0) etTargetWeight.setText(trimZero(targetW));

        int weekly = prefs.getInt("weekly_goal_days", 3);
        int pos = Math.min(Math.max(weekly, 1), 6) - 1;
        spWeeklyDays.setSelection(pos);
    }

    private void setListeners() {
        btn_back.setOnClickListener(v -> finish());

        btnEditName.setOnClickListener(v -> showEditNameDialog());

        // Recalculate BMI as user types
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                double h = parseDouble(etHeight.getText().toString().trim(), 0);
                double w = parseDouble(etWeight.getText().toString().trim(), 0);
                updateBMIViews(h, w);
            }
        };
        etHeight.addTextChangedListener(watcher);
        etWeight.addTextChangedListener(watcher);

        btnSaveMetrics.setOnClickListener(v -> {
            double h = parseDouble(etHeight.getText().toString().trim(), 0);
            double w = parseDouble(etWeight.getText().toString().trim(), 0);

            if (h <= 0 || w <= 0) {
                Toast.makeText(this, "Please enter valid height and weight", Toast.LENGTH_SHORT).show();
                return;
            }

            double bmi = calcBMI(h, w);
            String cls = bmiClass(bmi);

            prefs.edit()
                    .putString("height_cm", trimZero(h))
                    .putString("weight_kg", trimZero(w))
                    .putString("bmi_value", df1.format(bmi))
                    .putString("bmi_class", cls)
                    .apply();

            Toast.makeText(this, "Metrics saved", Toast.LENGTH_SHORT).show();
        });

        btnSaveGoal.setOnClickListener(v -> {
            String goal = "maintain";
            int checked = rgGoal.getCheckedRadioButtonId();
            if (checked == R.id.rbGain) goal = "gain";
            else if (checked == R.id.rbLose) goal = "lose";

            double target = parseDouble(etTargetWeight.getText().toString().trim(), 0);
            int weekly = Integer.parseInt(spWeeklyDays.getSelectedItem().toString());

            SharedPreferences.Editor ed = prefs.edit()
                    .putString("primary_goal", goal)
                    .putInt("weekly_goal_days", weekly);

            if (target > 0) ed.putString("target_weight", trimZero(target)); else ed.remove("target_weight");
            ed.apply();

            Toast.makeText(this, "Goal saved", Toast.LENGTH_SHORT).show();
        });

        btnStartArms.setOnClickListener(v -> startActivity(new Intent(this, ArmTrainingActivity.class)));
        btnStartLegs.setOnClickListener(v -> startActivity(new Intent(this, LegTrainingActivity.class)));
        btnStartCore.setOnClickListener(v -> startActivity(new Intent(this, CoreTrainingActivity.class)));
    }

    private void showEditNameDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter your name");
        input.setText(tvDisplayName.getText().toString().equals("Athlete") ? "" : tvDisplayName.getText().toString());
        input.setPadding(24, 24, 24, 24);

        new AlertDialog.Builder(this)
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("Save", (DialogInterface dialog, int which) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) name = "Athlete";
                    tvDisplayName.setText(name);
                    prefs.edit().putString("display_name", name).apply();
                    Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateBMIViews(double heightCm, double weightKg) {
        if (heightCm <= 0 || weightKg <= 0) {
            tvBMI.setText("BMI: --");
            tvBMIClass.setText("  (—)");
            return;
        }
        double bmi = calcBMI(heightCm, weightKg);
        String cls = bmiClass(bmi);
        tvBMI.setText("BMI: " + df1.format(bmi));
        tvBMIClass.setText("  (" + cls + ")");
    }

    private double calcBMI(double heightCm, double weightKg) {
        double m = heightCm / 100.0;
        if (m <= 0) return 0;
        return weightKg / (m * m);
    }

    private String bmiClass(double bmi) {
        if (bmi <= 0) return "—";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    private double parseDouble(String s, double def) {
        try {
            if (s == null || s.isEmpty()) return def;
            return Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }

    private String trimZero(double v) {
        // convert 70.0 -> "70", 70.5 -> "70.5"
        if (Math.floor(v) == v) return String.valueOf((long) v);
        return df1.format(v);
    }
}
