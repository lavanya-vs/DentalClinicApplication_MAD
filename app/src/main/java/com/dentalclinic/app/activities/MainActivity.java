package com.dentalclinic.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.dentalclinic.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout btnDoctorLogin = findViewById(R.id.btnDoctorLogin);
        LinearLayout btnPatientLogin = findViewById(R.id.btnPatientLogin);

        btnDoctorLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("USER_TYPE", "doctor");
            startActivity(intent);
        });

        btnPatientLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("USER_TYPE", "patient");
            startActivity(intent);
        });
    }
}