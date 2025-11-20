package com.dentalclinic.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.adapters.PatientAdapter;
import com.dentalclinic.app.database.DatabaseHelper;
import com.dentalclinic.app.models.Patient;
import java.util.List;

public class ManagePatientsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private DatabaseHelper db;
    private List<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patients);

        db = new DatabaseHelper(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerViewPatients);

        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadPatients();
    }

    private void loadPatients() {
        patients = db.getAllPatients();
        adapter = new PatientAdapter(patients, this);
        recyclerView.setAdapter(adapter);
    }
}