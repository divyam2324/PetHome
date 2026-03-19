package com.pethome;

public class DoctorModel {
    private int id;
    private String name, specialization, contact, experience, shelterEmail, shelterName;

    public DoctorModel(int id, String name, String specialization, String contact, String experience, String shelterEmail) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
        this.experience = experience;
        this.shelterEmail = shelterEmail;
    }

    public DoctorModel(int id, String name, String specialization, String contact, String experience, String shelterEmail, String shelterName) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
        this.experience = experience;
        this.shelterEmail = shelterEmail;
        this.shelterName = shelterName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getContact() { return contact; }
    public String getExperience() { return experience; }
    public String getShelterEmail() { return shelterEmail; }
    public String getShelterName() { return shelterName; }
}
