package com.dentalclinic.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dentalclinic.app.models.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DentalClinic.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DOCTORS = "doctors";
    private static final String TABLE_PATIENTS = "patients";
    private static final String TABLE_APPOINTMENTS = "appointments";
    private static final String TABLE_BILLING = "billing";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "userType TEXT)");

        // Create Doctors Table
        db.execSQL("CREATE TABLE " + TABLE_DOCTORS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "phone TEXT," +
                "specialization TEXT," +
                "rating REAL," +
                "availableTime TEXT)");

        // Create Patients Table
        db.execSQL("CREATE TABLE " + TABLE_PATIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "phone TEXT," +
                "medicalCondition TEXT," +
                "lastVisit TEXT)");

        // Create Appointments Table
        db.execSQL("CREATE TABLE " + TABLE_APPOINTMENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patientName TEXT," +
                "doctorName TEXT," +
                "date TEXT," +
                "time TEXT," +
                "status TEXT)");

        // Create Billing Table
        db.execSQL("CREATE TABLE " + TABLE_BILLING + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patientName TEXT," +
                "doctorName TEXT," +
                "amount REAL," +
                "date TEXT," +
                "paymentStatus TEXT)");

        // Insert demo users
        ContentValues values = new ContentValues();
        values.put("email", "demo@clinic.com");
        values.put("password", "demo123");
        values.put("userType", "doctor");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put("email", "patient@clinic.com");
        values.put("password", "demo123");
        values.put("userType", "patient");
        db.insert(TABLE_USERS, null, values);

        // Insert demo doctors
        insertDemoDoctor(db, "Dr. Sarah Johnson", "+1 234 567 8901", "Orthodontist", 4.8, "9:00 AM - 5:00 PM");
        insertDemoDoctor(db, "Dr. Michael Chen", "+1 234 567 8902", "Periodontist", 4.9, "10:00 AM - 6:00 PM");
        insertDemoDoctor(db, "Dr. Emily Davis", "+1 234 567 8903", "Endodontist", 4.7, "8:00 AM - 4:00 PM");

        // Insert demo patients
        insertDemoPatient(db, "John Doe", "+1 234 567 8900", "Cavity Treatment", "2024-11-15");
        insertDemoPatient(db, "Jane Smith", "+1 234 567 8901", "Teeth Whitening", "2024-11-10");
        insertDemoPatient(db, "Robert Brown", "+1 234 567 8902", "Root Canal", "2024-11-05");

        // Insert demo appointments
        insertDemoAppointment(db, "John Doe", "Dr. Sarah Johnson", "2024-11-25", "10:00 AM", "Pending");
        insertDemoAppointment(db, "Jane Smith", "Dr. Michael Chen", "2024-11-26", "2:00 PM", "Accepted");

        // Insert demo billing
        insertDemoBilling(db, "John Doe", "Dr. Sarah Johnson", 150.0, "2024-11-20", "Unpaid");
        insertDemoBilling(db, "Jane Smith", "Dr. Michael Chen", 200.0, "2024-11-18", "Paid");
    }

    private void insertDemoDoctor(SQLiteDatabase db, String name, String phone, String spec, double rating, String time) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("specialization", spec);
        values.put("rating", rating);
        values.put("availableTime", time);
        db.insert(TABLE_DOCTORS, null, values);
    }

    private void insertDemoPatient(SQLiteDatabase db, String name, String phone, String condition, String lastVisit) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("medicalCondition", condition);
        values.put("lastVisit", lastVisit);
        db.insert(TABLE_PATIENTS, null, values);
    }

    private void insertDemoAppointment(SQLiteDatabase db, String patient, String doctor, String date, String time, String status) {
        ContentValues values = new ContentValues();
        values.put("patientName", patient);
        values.put("doctorName", doctor);
        values.put("date", date);
        values.put("time", time);
        values.put("status", status);
        db.insert(TABLE_APPOINTMENTS, null, values);
    }

    private void insertDemoBilling(SQLiteDatabase db, String patient, String doctor, double amount, String date, String status) {
        ContentValues values = new ContentValues();
        values.put("patientName", patient);
        values.put("doctorName", doctor);
        values.put("amount", amount);
        values.put("date", date);
        values.put("paymentStatus", status);
        db.insert(TABLE_BILLING, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLING);
        onCreate(db);
    }

    // User Methods
    public User validateUser(String email, String password, String userType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                "email=? AND password=? AND userType=?",
                new String[]{email, password, userType},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    // Doctor CRUD
    public long addDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", doctor.getName());
        values.put("phone", doctor.getPhone());
        values.put("specialization", doctor.getSpecialization());
        values.put("rating", doctor.getRating());
        values.put("availableTime", doctor.getAvailableTime());
        return db.insert(TABLE_DOCTORS, null, values);
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOCTORS, null);

        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                );
                doctors.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return doctors;
    }

    public int updateDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", doctor.getName());
        values.put("phone", doctor.getPhone());
        values.put("specialization", doctor.getSpecialization());
        values.put("rating", doctor.getRating());
        values.put("availableTime", doctor.getAvailableTime());
        return db.update(TABLE_DOCTORS, values, "id=?", new String[]{String.valueOf(doctor.getId())});
    }

    public void deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCTORS, "id=?", new String[]{String.valueOf(id)});
    }

    // Patient CRUD
    public long addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("phone", patient.getPhone());
        values.put("medicalCondition", patient.getMedicalCondition());
        values.put("lastVisit", patient.getLastVisit());
        return db.insert(TABLE_PATIENTS, null, values);
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PATIENTS, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return patients;
    }

    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("phone", patient.getPhone());
        values.put("medicalCondition", patient.getMedicalCondition());
        values.put("lastVisit", patient.getLastVisit());
        return db.update(TABLE_PATIENTS, values, "id=?", new String[]{String.valueOf(patient.getId())});
    }

    public void deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PATIENTS, "id=?", new String[]{String.valueOf(id)});
    }

    // Appointment CRUD
    public long addAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patientName", appointment.getPatientName());
        values.put("doctorName", appointment.getDoctorName());
        values.put("date", appointment.getDate());
        values.put("time", appointment.getTime());
        values.put("status", appointment.getStatus());
        return db.insert(TABLE_APPOINTMENTS, null, values);
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS, null);

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    public int updateAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patientName", appointment.getPatientName());
        values.put("doctorName", appointment.getDoctorName());
        values.put("date", appointment.getDate());
        values.put("time", appointment.getTime());
        values.put("status", appointment.getStatus());
        return db.update(TABLE_APPOINTMENTS, values, "id=?", new String[]{String.valueOf(appointment.getId())});
    }

    public void deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPOINTMENTS, "id=?", new String[]{String.valueOf(id)});
    }

    // Billing CRUD
    public long addBilling(Billing billing) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patientName", billing.getPatientName());
        values.put("doctorName", billing.getDoctorName());
        values.put("amount", billing.getAmount());
        values.put("date", billing.getDate());
        values.put("paymentStatus", billing.getPaymentStatus());
        return db.insert(TABLE_BILLING, null, values);
    }

    public List<Billing> getAllBilling() {
        List<Billing> billings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BILLING, null);

        if (cursor.moveToFirst()) {
            do {
                Billing billing = new Billing(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                billings.add(billing);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return billings;
    }

    public int updateBilling(Billing billing) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patientName", billing.getPatientName());
        values.put("doctorName", billing.getDoctorName());
        values.put("amount", billing.getAmount());
        values.put("date", billing.getDate());
        values.put("paymentStatus", billing.getPaymentStatus());
        return db.update(TABLE_BILLING, values, "id=?", new String[]{String.valueOf(billing.getId())});
    }

    public void deleteBilling(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILLING, "id=?", new String[]{String.valueOf(id)});
    }
}