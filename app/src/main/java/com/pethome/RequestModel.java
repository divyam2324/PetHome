package com.pethome;

public class RequestModel {

    int id, petId;
    String name, breed, age, gender, image, status, requestUser;

    public RequestModel(int id,
                        int petId,
                        String name,
                        String breed,
                        String age,
                        String gender,
                        String image,
                        String requestUser,
                        String status) {

        this.id = id;
        this.petId = petId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.image = image;
        this.requestUser = requestUser;
        this.status = status;
    }

    public int getId(){ return id; }
    
    public int getPetId(){ return petId; }

    public String getName(){ return name; }

    public String getBreed(){ return breed; }

    public String getAge(){ return age; }

    public String getGender(){ return gender; }

    public String getImage(){ return image; }

    public String getRequestUser(){ return requestUser; }

    public String getStatus(){ return status; }
}
