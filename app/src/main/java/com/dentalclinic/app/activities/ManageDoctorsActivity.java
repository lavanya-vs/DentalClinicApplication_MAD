package com.dentalclinic.app.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.adapters.DoctorAdapter;
import com.dentalclinic.app.database.DatabaseHelper;
import com.dentalclinic.app.models.Doctor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageDoctorsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private DatabaseHelper db;
    private List<Doctor> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_doctors);

        db = new DatabaseHelper(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        FloatingActionButton fabAddDoctor = findViewById(R.id.fabAddDoctor);
        recyclerView = findViewById(R.id.recyclerViewDoctors);

        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Check if this is patient view (no FAB visible means patient)
        String userType = getIntent().getStringExtra("USER_TYPE");
        if ("patient".equals(userType)) {
            fabAddDoctor.hide();
            loadDoctorsPatientView();
        } else {
            loadDoctors();
            fabAddDoctor.setOnClickListener(v -> showAddDoctorDialog(null));
        }
    }

    private void loadDoctors() {
        doctors = db.getAllDoctors();
        adapter = new DoctorAdapter(doctors, this, new DoctorAdapter.DoctorListener() {
            @Override
            public void onEdit(Doctor doctor) {
                showAddDoctorDialog(doctor);
            }

            @Override
            public void onDelete(Doctor doctor) {
                new AlertDialog.Builder(ManageDoctorsActivity.this)
                        .setTitle("Delete Doctor")
                        .setMessage("Are you sure you want to delete " + doctor.getName() + "?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            db.deleteDoctor(doctor.getId());
                            loadDoctors();
                            Toast.makeText(ManageDoctorsActivity.this, "Doctor deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadDoctorsPatientView() {
        doctors = db.getAllDoctors();
        adapter = new DoctorAdapter(doctors, this);
        recyclerView.setAdapter(adapter);
    }

    private void showAddDoctorDialog(Doctor doctor) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_doctor);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etName = dialog.findViewById(R.id.etName);
        EditText etPhone = dialog.findViewById(R.id.etPhone);
        EditText etSpecialization = dialog.findViewById(R.id.etSpecialization);
        EditText etRating = dialog.findViewById(R.id.etRating);
        EditText etAvailableTime = dialog.findViewById(R.id.etAvailableTime);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        if (doctor != null) {
            etName.setText(doctor.getName());
            etPhone.setText(doctor.getPhone());
            etSpecialization.setText(doctor.getSpecialization());
            etRating.setText(String.valueOf(doctor.getRating()));
            etAvailableTime.setText(doctor.getAvailableTime());
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String spec = etSpecialization.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();
            String time = etAvailableTime.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || spec.isEmpty() || ratingStr.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double rating = Double.parseDouble(ratingStr);

                if (rating < 0 || rating > 5) {
                    Toast.makeText(this, "Rating must be between 0 and 5", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (doctor == null) {
                    Doctor newDoctor = new Doctor(0, name, phone, spec, rating, time);
                    long result = db.addDoctor(newDoctor);
                    if (result > 0) {
                        Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    doctor.setName(name);
                    doctor.setPhone(phone);
                    doctor.setSpecialization(spec);
                    doctor.setRating(rating);
                    doctor.setAvailableTime(time);
                    int result = db.updateDoctor(doctor);
                    if (result > 0) {
                        Toast.makeText(this, "Doctor updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                loadDoctors();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid rating (0-5)", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userType = getIntent().getStringExtra("USER_TYPE");
        if ("patient".equals(userType)) {
            loadDoctorsPatientView();
        } else {
            loadDoctors();
        }
    }
}