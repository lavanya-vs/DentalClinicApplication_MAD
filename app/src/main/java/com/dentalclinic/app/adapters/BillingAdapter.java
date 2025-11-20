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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.dentalclinic.app.R;
import com.dentalclinic.app.activities.ManageBillingActivity;
import com.dentalclinic.app.models.Billing;
import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingViewHolder> {
    private List<Billing> billings;
    private Context context;
    private String userType;
    private BillingListener listener;

    public interface BillingListener {
        void onEdit(Billing billing);
        void onDelete(Billing billing);
        void onPay(Billing billing);
    }

    public BillingAdapter(List<Billing> billings, Context context, String userType, BillingListener listener) {
        this.billings = billings;
        this.context = context;
        this.userType = userType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_billing, parent, false);
        return new BillingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        Billing billing = billings.get(position);

        holder.tvPatientName.setText(billing.getPatientName());
        holder.tvDoctorName.setText(billing.getDoctorName());
        holder.tvAmount.setText("💰 $" + String.format("%.2f", billing.getAmount()));
        holder.tvDate.setText("📅 " + billing.getDate());
        holder.tvPaymentStatus.setText(billing.getPaymentStatus());

        // Set payment status color
        if ("Paid".equals(billing.getPaymentStatus())) {
            holder.tvPaymentStatus.setBackgroundColor(context.getResources().getColor(R.color.status_accepted));
        } else {
            holder.tvPaymentStatus.setBackgroundColor(context.getResources().getColor(R.color.status_pending));
        }

        // Show pay button only for patients with unpaid bills
        if ("patient".equals(userType) && "Unpaid".equals(billing.getPaymentStatus())) {
            holder.btnPayNow.setVisibility(View.VISIBLE);
            holder.qrCodeLayout.setVisibility(View.GONE);

            holder.btnPayNow.setOnClickListener(v -> {
                holder.btnPayNow.setVisibility(View.GONE);
                holder.qrCodeLayout.setVisibility(View.VISIBLE);

                // Generate QR code
                String qrContent = "Payment: $" + billing.getAmount() + " to " + billing.getDoctorName() + " for " + billing.getPatientName();
                holder.ivQRCode.setImageBitmap(ManageBillingActivity.generateQRCode(qrContent));
            });

            holder.btnConfirmPayment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPay(billing);
                }
            });
        } else {
            holder.btnPayNow.setVisibility(View.GONE);
            holder.qrCodeLayout.setVisibility(View.GONE);
        }

        // Only doctors can edit/delete billing
        if ("doctor".equals(userType)) {
            holder.btnEditBilling.setVisibility(View.VISIBLE);
            holder.btnDeleteBilling.setVisibility(View.VISIBLE);

            holder.btnEditBilling.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(billing);
                }
            });

            holder.btnDeleteBilling.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(billing);
                }
            });
        } else {
            holder.btnEditBilling.setVisibility(View.GONE);
            holder.btnDeleteBilling.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return billings != null ? billings.size() : 0;
    }

    static class BillingViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDoctorName, tvAmount, tvDate, tvPaymentStatus;
        CardView btnPayNow;
        LinearLayout qrCodeLayout;
        ImageView ivQRCode, btnEditBilling, btnDeleteBilling;
        Button btnConfirmPayment;

        public BillingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            btnPayNow = itemView.findViewById(R.id.btnPayNow);
            qrCodeLayout = itemView.findViewById(R.id.qrCodeLayout);
            ivQRCode = itemView.findViewById(R.id.ivQRCode);
            btnConfirmPayment = itemView.findViewById(R.id.btnConfirmPayment);
            btnEditBilling = itemView.findViewById(R.id.btnEditBilling);
            btnDeleteBilling = itemView.findViewById(R.id.btnDeleteBilling);
        }
    }
}