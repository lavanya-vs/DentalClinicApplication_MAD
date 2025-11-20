package com.dentalclinic.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dentalclinic.app.R;
import com.dentalclinic.app.database.DatabaseHelper;
import com.dentalclinic.app.models.User;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvLoginTitle, tvDemoCredentials;
    private DatabaseHelper db;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        userType = getIntent().getStringExtra("USER_TYPE");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        tvDemoCredentials = findViewById(R.id.tvDemoCredentials);

        if ("doctor".equals(userType)) {
            tvLoginTitle.setText("Doctor Login");
            tvDemoCredentials.setText("Email: demo@clinic.com\nPassword: demo123");
        } else {
            tvLoginTitle.setText("Patient Login");
            tvDemoCredentials.setText("Email: patient@clinic.com\nPassword: demo123");
        }

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.validateUser(email, password, userType);

        if (user != null) {
            Intent intent;
            if ("doctor".equals(userType)) {
                intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
            }
            intent.putExtra("USER_NAME", email.split("@")[0]);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}