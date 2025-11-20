package com.dentalclinic.app.models;

public class Patient {
    private int id;
    private String name;
    private String phone;
    private String medicalCondition;
    private String lastVisit;

    public Patient() {}

    public Patient(int id, String name, String phone, String medicalCondition, String lastVisit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.medicalCondition = medicalCondition;
        this.lastVisit = lastVisit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    public String getLastVisit() { return lastVisit; }
    public void setLastVisit(String lastVisit) { this.lastVisit = lastVisit; }
}