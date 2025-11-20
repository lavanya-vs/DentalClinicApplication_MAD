package com.dentalclinic.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.models.Patient;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private List<Patient> patients;
    private Context context;

    public PatientAdapter(List<Patient> patients, Context context) {
        this.patients = patients;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);

        holder.tvPatientName.setText(patient.getName());
        holder.tvPhone.setText("📞 " + patient.getPhone());
        holder.tvMedicalCondition.setText("🏥 " + patient.getMedicalCondition());
        holder.tvLastVisit.setText("📅 Last Visit: " + patient.getLastVisit());
    }

    @Override
    public int getItemCount() {
        return patients != null ? patients.size() : 0;
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvPhone, tvMedicalCondition, tvLastVisit;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvMedicalCondition = itemView.findViewById(R.id.tvMedicalCondition);
            tvLastVisit = itemView.findViewById(R.id.tvLastVisit);
        }
    }
}