package com.dentalclinic.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.dentalclinic.app.R;

public class DoctorDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        TextView tvDoctorName = findViewById(R.id.tvDoctorName);
        ImageView btnLogout = findViewById(R.id.btnLogout);
        CardView cardAppointments = findViewById(R.id.cardAppointments);
        CardView cardPatientHistory = findViewById(R.id.cardPatientHistory);
        CardView cardBilling = findViewById(R.id.cardBilling);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            tvDoctorName.setText("Dr. " + userName);
        }

        btnLogout.setOnClickListener(v -> {
            finish();
        });

        cardAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDashboardActivity.this, ManageAppointmentsActivity.class);
            intent.putExtra("USER_TYPE", "doctor");
            startActivity(intent);
        });

        cardPatientHistory.setOnClickListener(v -> {
            startActivity(new Intent(DoctorDashboardActivity.this, ManagePatientsActivity.class));
        });

        cardBilling.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDashboardActivity.this, ManageBillingActivity.class);
            intent.putExtra("USER_TYPE", "doctor");
            startActivity(intent);
        });
    }
}