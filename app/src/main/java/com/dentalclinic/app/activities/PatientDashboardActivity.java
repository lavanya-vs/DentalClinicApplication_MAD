package com.dentalclinic.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.dentalclinic.app.R;

public class PatientDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        TextView tvPatientName = findViewById(R.id.tvPatientName);
        ImageView btnLogout = findViewById(R.id.btnLogout);
        CardView cardDoctors = findViewById(R.id.cardDoctors);
        CardView cardAppointments = findViewById(R.id.cardAppointments);
        CardView cardBilling = findViewById(R.id.cardBilling);
        CardView cardDentalGuide = findViewById(R.id.cardDentalGuide);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            tvPatientName.setText(userName);
        }

        btnLogout.setOnClickListener(v -> finish());

        cardDoctors.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, ManageDoctorsActivity.class);
            intent.putExtra("USER_TYPE", "patient");
            startActivity(intent);
        });

        cardAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, ManageAppointmentsActivity.class);
            intent.putExtra("USER_TYPE", "patient");
            startActivity(intent);
        });

        cardBilling.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, ManageBillingActivity.class);
            intent.putExtra("USER_TYPE", "patient");
            startActivity(intent);
        });

        cardDentalGuide.setOnClickListener(v -> {
            startActivity(new Intent(PatientDashboardActivity.this, DentalCareGuideActivity.class));
        });
    }
}