package com.pethome;

public class PetModel {

    private int id;
    private String name, species, breed, age, gender, contactName, contactInfo, image, ownerEmail;
    private boolean goodWithChildren, goodWithOtherPets;

    public PetModel(int id, String name, String species, String breed, String age, String gender, 
                    boolean goodWithChildren, boolean goodWithOtherPets, String contactName, 
                    String contactInfo, String image, String ownerEmail) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.goodWithChildren = goodWithChildren;
        this.goodWithOtherPets = goodWithOtherPets;
        this.contactName = contactName;
        this.contactInfo = contactInfo;
        this.image = image;
        this.ownerEmail = ownerEmail;
    }

    // Constructor for simpler usage if needed (backward compatibility)
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
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
    public boolean isGoodWithChildren() { return goodWithChildren; }
    public boolean isGoodWithOtherPets() { return goodWithOtherPets; }
    public String getContactName() { return contactName; }
    public String getContactInfo() { return contactInfo; }
    public String getImage() { return image; }
    public String getOwnerEmail() { return ownerEmail; }
}
