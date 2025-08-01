package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ArmTrainingActivity extends AppCompatActivity {

    // تعريف المتغيرات للعناصر
    private ImageView btnBack;
    private ImageView ivExerciseMain;
    private Button btnStartWorkout;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm_training); // اسم ملف الـ XML

        // ربط العناصر بالكود
        initViews();

        // تعيين المستمعين للأزرار
        setClickListeners();
    }

    /**
     * ربط العناصر من الـ XML بالكود
     */
    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        ivExerciseMain = findViewById(R.id.iv_exercise_main);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        tvTitle = findViewById(R.id.tv_title);
    }

    /**
     * تعيين المستمعين للأزرار
     */
    private void setClickListeners() {

        // زر الرجوع
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // العودة للصفحة السابقة
                finish();
            }
        });

        // زر بدء التمرين
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال لصفحة التمرين الفعلي
                startWorkoutActivity();
            }
        });

        // إضافة تأثير عند الضغط على الصورة (اختياري)
        ivExerciseMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // يمكن إضافة عرض الصورة بحجم كامل هنا
                showFullScreenImage();
            }
        });
    }

    /**
     * بدء نشاط التمرين الفعلي
     */
    private void startWorkoutActivity() {
        // عرض رسالة للمستخدم
        Toast.makeText(this, "The workout starts now!", Toast.LENGTH_SHORT).show();
    }

    /**
     * عرض الصورة بحجم كامل (اختياري)
     */
    private void showFullScreenImage() {
        // عرض رسالة مؤقتة
        Toast.makeText(this, "View image full size", Toast.LENGTH_SHORT).show();
    }
}