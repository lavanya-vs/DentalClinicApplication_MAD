package com.dentalclinic.app.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import com.dentalclinic.app.adapters.AppointmentAdapter;
import com.dentalclinic.app.database.DatabaseHelper;
import com.dentalclinic.app.models.Appointment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ManageAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private DatabaseHelper db;
    private List<Appointment> appointments;
    private String userType;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);

        db = new DatabaseHelper(this);
        userType = getIntent().getStringExtra("USER_TYPE");

        ImageView btnBack = findViewById(R.id.btnBack);
        FloatingActionButton fabAddAppointment = findViewById(R.id.fabAddAppointment);
        recyclerView = findViewById(R.id.recyclerViewAppointments);

        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAppointments();

        fabAddAppointment.setOnClickListener(v -> showAddAppointmentDialog(null, false));
    }

    private void loadAppointments() {
        appointments = db.getAllAppointments();
        adapter = new AppointmentAdapter(appointments, this, userType, new AppointmentAdapter.AppointmentListener() {
            @Override
            public void onEdit(Appointment appointment) {
                showAddAppointmentDialog(appointment, false);
            }

            @Override
            public void onDelete(Appointment appointment) {
                new AlertDialog.Builder(ManageAppointmentsActivity.this)
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this appointment?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            db.deleteAppointment(appointment.getId());
                            loadAppointments();
                            Toast.makeText(ManageAppointmentsActivity.this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onAccept(Appointment appointment) {
                appointment.setStatus("Accepted");
                db.updateAppointment(appointment);
                loadAppointments();
                Toast.makeText(ManageAppointmentsActivity.this, "Appointment accepted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(Appointment appointment) {
                showAddAppointmentDialog(appointment, true);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showAddAppointmentDialog(Appointment appointment, boolean isReschedule) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_appointment);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etPatientName = dialog.findViewById(R.id.etPatientName);
        EditText etDoctorName = dialog.findViewById(R.id.etDoctorName);
        EditText etDate = dialog.findViewById(R.id.etDate);
        EditText etTime = dialog.findViewById(R.id.etTime);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Make EditTexts non-editable but clickable
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etTime.setFocusable(false);
        etTime.setClickable(true);

        if (appointment != null) {
            etPatientName.setText(appointment.getPatientName());
            etDoctorName.setText(appointment.getDoctorName());
            etDate.setText(appointment.getDate());
            etTime.setText(appointment.getTime());
        }

        // Date picker
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        etDate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Time picker
        etTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int hour = hourOfDay > 12 ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
                        etTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
            );
            timePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {
            String patientName = etPatientName.getText().toString().trim();
            String doctorName = etDoctorName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (patientName.isEmpty() || doctorName.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (appointment == null) {
                Appointment newAppointment = new Appointment(0, patientName, doctorName, date, time, "Pending");
                long result = db.addAppointment(newAppointment);
                if (result > 0) {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                }
            } else {
                appointment.setPatientName(patientName);
                appointment.setDoctorName(doctorName);
                appointment.setDate(date);
                appointment.setTime(time);
                if (isReschedule) {
                    appointment.setStatus("Pending");
                }
                int result = db.updateAppointment(appointment);
                if (result > 0) {
                    Toast.makeText(this, "Appointment updated successfully!", Toast.LENGTH_SHORT).show();
                }
            }

            loadAppointments();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }
}