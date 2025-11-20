package com.dentalclinic.app.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.dentalclinic.app.adapters.BillingAdapter;
import com.dentalclinic.app.database.DatabaseHelper;
import com.dentalclinic.app.models.Billing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ManageBillingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BillingAdapter adapter;
    private DatabaseHelper db;
    private List<Billing> billings;
    private String userType;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_billing);

        db = new DatabaseHelper(this);
        userType = getIntent().getStringExtra("USER_TYPE");

        ImageView btnBack = findViewById(R.id.btnBack);
        FloatingActionButton fabAddBilling = findViewById(R.id.fabAddBilling);
        recyclerView = findViewById(R.id.recyclerViewBilling);

        btnBack.setOnClickListener(v -> finish());

        // Only doctors can add billing
        if ("patient".equals(userType)) {
            fabAddBilling.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadBilling();

        fabAddBilling.setOnClickListener(v -> showAddBillingDialog(null));
    }

    private void loadBilling() {
        billings = db.getAllBilling();
        adapter = new BillingAdapter(billings, this, userType, new BillingAdapter.BillingListener() {
            @Override
            public void onEdit(Billing billing) {
                showAddBillingDialog(billing);
            }

            @Override
            public void onDelete(Billing billing) {
                new AlertDialog.Builder(ManageBillingActivity.this)
                        .setTitle("Delete Billing")
                        .setMessage("Are you sure you want to delete this billing record?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            db.deleteBilling(billing.getId());
                            loadBilling();
                            Toast.makeText(ManageBillingActivity.this, "Billing deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onPay(Billing billing) {
                billing.setPaymentStatus("Paid");
                db.updateBilling(billing);
                loadBilling();
                Toast.makeText(ManageBillingActivity.this, "Payment successful! ✅", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showAddBillingDialog(Billing billing) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_billing);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etPatientName = dialog.findViewById(R.id.etPatientName);
        EditText etDoctorName = dialog.findViewById(R.id.etDoctorName);
        EditText etAmount = dialog.findViewById(R.id.etAmount);
        EditText etDate = dialog.findViewById(R.id.etDate);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        etDate.setFocusable(false);
        etDate.setClickable(true);

        if (billing != null) {
            etPatientName.setText(billing.getPatientName());
            etDoctorName.setText(billing.getDoctorName());
            etAmount.setText(String.valueOf(billing.getAmount()));
            etDate.setText(billing.getDate());
        } else {
            // Set today's date by default
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            etDate.setText(sdf.format(calendar.getTime()));
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
            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {
            String patientName = etPatientName.getText().toString().trim();
            String doctorName = etDoctorName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            if (patientName.isEmpty() || doctorName.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                if (amount <= 0) {
                    Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (billing == null) {
                    Billing newBilling = new Billing(0, patientName, doctorName, amount, date, "Unpaid");
                    long result = db.addBilling(newBilling);
                    if (result > 0) {
                        Toast.makeText(this, "Bill created successfully!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    billing.setPatientName(patientName);
                    billing.setDoctorName(doctorName);
                    billing.setAmount(amount);
                    billing.setDate(date);
                    int result = db.updateBilling(billing);
                    if (result > 0) {
                        Toast.makeText(this, "Bill updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                }

                loadBilling();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static Bitmap generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBilling();
    }
}