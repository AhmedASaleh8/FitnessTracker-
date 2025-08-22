package com.example.fitnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private ImageView btn_back;

    private EditText etDisplayName;
    private Button btnSaveName, btnLogout, btnAbout;
    private SwitchCompat swDarkMode;
    private RadioGroup rgLanguage;
    private RadioButton rbArabic, rbEnglish;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        initViews();
        loadSavedSettings();
        setListeners();
    }

    private void initViews() {
        btn_back        = findViewById(R.id.btn_back);

        etDisplayName   = findViewById(R.id.etDisplayName);
        btnSaveName     = findViewById(R.id.btnSaveName);
        btnLogout       = findViewById(R.id.btnLogout);
        btnAbout        = findViewById(R.id.btnAbout);

        swDarkMode      = findViewById(R.id.swDarkMode);

        rgLanguage      = findViewById(R.id.rgLanguage);
        rbArabic        = findViewById(R.id.rbArabic);
        rbEnglish       = findViewById(R.id.rbEnglish);
    }

    private void loadSavedSettings() {
        // Name
        etDisplayName.setText(prefs.getString("display_name", ""));

        //  Dark Mode
        boolean dark = prefs.getBoolean("dark_mode", false);
        swDarkMode.setChecked(dark);
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // language
        String lang = prefs.getString("language", "ar");
        if ("ar".equals(lang)) rbArabic.setChecked(true); else rbEnglish.setChecked(true);
    }

    private void setListeners() {

        // Back button
        btn_back.setOnClickListener(v -> finish());
        btnSaveName.setOnClickListener(v -> {
            String name = etDisplayName.getText().toString().trim();
            prefs.edit().putString("display_name", name).apply();
            Toast.makeText(this, "Name saved", Toast.LENGTH_SHORT).show();
        });

        swDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        rgLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            String lang = (checkedId == R.id.rbArabic) ? "ar" : "en";
            prefs.edit().putString("language", lang).apply();
            Toast.makeText(this, "Language set: " + (lang.equals("ar") ? "Arabic" : "English"), Toast.LENGTH_SHORT).show();
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
