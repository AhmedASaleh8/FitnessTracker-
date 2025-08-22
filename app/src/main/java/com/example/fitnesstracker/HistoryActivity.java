package com.example.fitnesstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ListView lvHistory;
    private TextView tvEmpty;
    private Button btnClear;

    private SharedPreferences sp;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.btn_back);
        lvHistory = findViewById(R.id.lvHistory);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnClear = findViewById(R.id.btnClear);

        lvHistory.setEmptyView(tvEmpty);

        sp = getSharedPreferences("history", MODE_PRIVATE);

// Upload the log and prepare the list
    List<String> items = loadHistoryLines();
        adapter = new HistoryAdapter(this, items);
        lvHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnClear.setOnClickListener(v -> {
            sp.edit().remove("workout_log").apply();
            adapter.clear();
            adapter.notifyDataSetChanged();
        });
    }

    private List<String> loadHistoryLines() {
        String log = sp.getString("workout_log", "");
        List<String> list = new ArrayList<>();
        if (log != null && !log.isEmpty()) {
            String[] lines = log.split("\n");
            for (String s : lines) {
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) list.add(s);
                }
            }
        }
        return list;
    }

    private static class HistoryAdapter extends ArrayAdapter<String> {
        private final LayoutInflater inflater;

        HistoryAdapter(Context ctx, List<String> data) {
            super(ctx, 0, data);
            inflater = LayoutInflater.from(ctx);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) v = inflater.inflate(R.layout.item_history, parent, false);

            ImageView iv = v.findViewById(R.id.ivIcon);
            TextView tv1 = v.findViewById(R.id.tvLine1);
            TextView tv2 = v.findViewById(R.id.tvLine2);

            String line = getItem(position);
            if (line == null) line = "";

            String date = "";
            String title = line;

            Matcher m = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})\\s*[—\\-:•]\\s*(.*)$")
                    .matcher(line);
            if (m.find()) {
                date = m.group(1);
                title = m.group(2);
            }

            tv1.setText(title.isEmpty() ? line : title);
            tv2.setText(date);

            // Choose the icon according to the session type mentioned in the title
            int icon = R.drawable.pushup;
            String lower = line.toLowerCase(Locale.ROOT);
            if (lower.contains("core")) {
                icon = R.drawable.situp;
            } else if (lower.contains("leg")) {
                icon = R.drawable.squat;
            } else if (lower.contains("arm")) {
                icon = R.drawable.pushup;
            }
            iv.setImageResource(icon);

            return v;
        }
    }
}
