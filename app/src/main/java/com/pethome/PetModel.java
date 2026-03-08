package com.pethome;

public class PetModel {

    int id;
    String name, breed, age, gender, image, ownerEmail;

    public PetModel(int id, String name, String breed, String age, String gender, String image, String ownerEmail) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.image = image;
        this.ownerEmail = ownerEmail;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
    public String getImage() { return image; }
    public String getOwnerEmail() { return ownerEmail; }
}
