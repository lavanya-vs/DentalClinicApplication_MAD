package com.dentalclinic.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.models.Doctor;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<Doctor> doctors;
    private Context context;
    private DoctorListener listener;

    public interface DoctorListener {
        void onEdit(Doctor doctor);
        void onDelete(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctors, Context context) {
        this.doctors = doctors;
        this.context = context;
        this.listener = null;
    }

    public DoctorAdapter(List<Doctor> doctors, Context context, DoctorListener listener) {
        this.doctors = doctors;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);

        holder.tvDoctorName.setText(doctor.getName());
        holder.tvSpecialization.setText(doctor.getSpecialization());
        holder.tvRating.setText(String.valueOf(doctor.getRating()));
        holder.tvAvailableTime.setText("🕐 " + doctor.getAvailableTime());
        holder.tvPhone.setText("📞 " + doctor.getPhone());

        if (listener != null) {
            holder.btnEditDoctor.setVisibility(View.VISIBLE);
            holder.btnDeleteDoctor.setVisibility(View.VISIBLE);

            holder.btnEditDoctor.setOnClickListener(v -> {
                listener.onEdit(doctor);
            });

            holder.btnDeleteDoctor.setOnClickListener(v -> {
                listener.onDelete(doctor);
            });
        } else {
            holder.btnEditDoctor.setVisibility(View.GONE);
            holder.btnDeleteDoctor.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : 0;
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialization, tvRating, tvAvailableTime, tvPhone;
        ImageView btnEditDoctor, btnDeleteDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvAvailableTime = itemView.findViewById(R.id.tvAvailableTime);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnEditDoctor = itemView.findViewById(R.id.btnEditDoctor);
            btnDeleteDoctor = itemView.findViewById(R.id.btnDeleteDoctor);
        }
    }
}