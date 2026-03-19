package com.pethome;

public class ShelterModel {
    private String name, email, contact, city, medicalInfo;
    private double latitude, longitude;

    public ShelterModel(String name, String email, String contact, String city, String medicalInfo, double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.city = city;
        this.medicalInfo = medicalInfo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getContact() { return contact; }
    public String getCity() { return city; }
    public String getMedicalInfo() { return medicalInfo; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
