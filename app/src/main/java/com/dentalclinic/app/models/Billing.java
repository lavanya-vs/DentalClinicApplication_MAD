package com.dentalclinic.app.models;

public class Billing {
    private int id;
    private String patientName;
    private String doctorName;
    private double amount;
    private String date;
    private String paymentStatus; // "Paid" or "Unpaid"

    public Billing() {}

    public Billing(int id, String patientName, String doctorName, double amount, String date, String paymentStatus) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.amount = amount;
        this.date = date;
        this.paymentStatus = paymentStatus;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}