package com.dentalclinic.app.models;

public class Doctor {
    private int id;
    private String name;
    private String phone;
    private String specialization;
    private double rating;
    private String availableTime;

    public Doctor() {}

    public Doctor(int id, String name, String phone, String specialization, double rating, String availableTime) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.rating = rating;
        this.availableTime = availableTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getAvailableTime() { return availableTime; }
    public void setAvailableTime(String availableTime) { this.availableTime = availableTime; }
}