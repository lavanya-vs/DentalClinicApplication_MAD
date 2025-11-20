package com.dentalclinic.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.models.Appointment;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointments;
    private Context context;
    private String userType;
    private AppointmentListener listener;

    public interface AppointmentListener {
        void onEdit(Appointment appointment);
        void onDelete(Appointment appointment);
        void onAccept(Appointment appointment);
        void onReschedule(Appointment appointment);
    }

    public AppointmentAdapter(List<Appointment> appointments, Context context, String userType, AppointmentListener listener) {
        this.appointments = appointments;
        this.context = context;
        this.userType = userType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.tvPatientName.setText(appointment.getPatientName());
        holder.tvDoctorName.setText("with " + appointment.getDoctorName());
        holder.tvDateTime.setText("📅 " + appointment.getDate() + " | 🕐 " + appointment.getTime());
        holder.tvStatus.setText(appointment.getStatus());

        // Set status color
        int statusColor;
        switch (appointment.getStatus()) {
            case "Accepted":
                statusColor = context.getResources().getColor(R.color.status_accepted);
                break;
            case "Rejected":
                statusColor = context.getResources().getColor(R.color.status_rejected);
                break;
            default: // Pending
                statusColor = context.getResources().getColor(R.color.status_pending);
                break;
        }
        holder.tvStatus.setBackgroundColor(statusColor);

        // Show accept/reschedule buttons only for doctors with pending appointments
        if ("doctor".equals(userType) && "Pending".equals(appointment.getStatus())) {
            holder.layoutDoctorActions.setVisibility(View.VISIBLE);

            holder.btnAccept.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAccept(appointment);
                }
            });

            holder.btnReschedule.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReschedule(appointment);
                }
            });
        } else {
            holder.layoutDoctorActions.setVisibility(View.GONE);
        }

        // Edit and delete buttons
        holder.btnEditAppointment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(appointment);
            }
        });

        holder.btnDeleteAppointment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments != null ? appointments.size() : 0;
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDoctorName, tvDateTime, tvStatus;
        LinearLayout layoutDoctorActions;
        Button btnAccept, btnReschedule;
        ImageView btnEditAppointment, btnDeleteAppointment;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layoutDoctorActions = itemView.findViewById(R.id.layoutDoctorActions);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReschedule = itemView.findViewById(R.id.btnReschedule);
            btnEditAppointment = itemView.findViewById(R.id.btnEditAppointment);
            btnDeleteAppointment = itemView.findViewById(R.id.btnDeleteAppointment);
        }
    }
}