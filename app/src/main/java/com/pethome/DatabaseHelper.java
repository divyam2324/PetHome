package com.pethome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Pethome.db";
    private static final int DATABASE_VERSION = 15; // Bumped to 15 to fix missing columns

    private static final String TABLE_PETS = "PETS";
    private static final String TABLE_REQUESTS = "REQUESTS";
    private static final String TABLE_FAVORITES = "FAVORITES";
    private static final String TABLE_DOCTORS = "DOCTORS";
    private static final String TABLE_CHAT_HISTORY = "CHAT_HISTORY";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table with all current columns
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS(" +
                "USERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "EMAIL TEXT," +
                "CONTACT TEXT," +
                "PASSWORD TEXT," +
                "GENDER TEXT," +
                "CITY TEXT," +
                "ROLE TEXT," +
                "MEDICAL_INFO TEXT," +
                "LATITUDE REAL," +
                "LONGITUDE REAL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PETS(" +
                "PETID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PETNAME TEXT," +
                "SPECIES TEXT," +
                "BREED TEXT," +
                "AGE TEXT," +
                "GENDER TEXT," +
                "GOODWITHCHILDREN INTEGER," +
                "GOODWITHOTHERPETS INTEGER," +
                "CONTACTNAME TEXT," +
                "CONTACTINFO TEXT," +
                "IMAGE TEXT," +
                "OWNEREMAIL TEXT," +
                "IS_REQUESTED INTEGER DEFAULT 0," +
                "CARE_INFO TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS REQUESTS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PETID INTEGER," +
                "PETNAME TEXT," +
                "BREED TEXT," +
                "AGE TEXT," +
                "GENDER TEXT," +
                "IMAGE TEXT," +
                "REQUESTUSER TEXT," +
                "OWNEREMAIL TEXT," +
                "STATUS TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS FAVORITES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USEREMAIL TEXT," +
                "PETID INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS DOCTORS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "SPECIALIZATION TEXT," +
                "CONTACT TEXT," +
                "EXPERIENCE TEXT," +
                "SHELTER_EMAIL TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS CHAT_HISTORY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SYMPTOMS TEXT," +
                "DISEASE TEXT," +
                "URGENCY TEXT," +
                "TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)");

        insertDummyPets(db);
        insertDummyShelters(db);
    }

    private void insertDummyPets(SQLiteDatabase db) {
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED, CARE_INFO) " +
                "VALUES ('Buddy', 'Dog', 'Golden Retriever', '2 Years', 'Male', 1, 1, 'PetHome Admin', 'admin@pethome.com', 'dog_buddy', 'system@pethome.com', 0, 'Keep active, regular brushing required.')");
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED, CARE_INFO) " +
                "VALUES ('Luna', 'Cat', 'Persian', '1 Year', 'Female', 1, 1, 'PetHome Admin', 'admin@pethome.com', 'cat_luna', 'system@pethome.com', 0, 'Indoor only, high maintenance fur.')");
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED, CARE_INFO) " +
                "VALUES ('Charlie', 'Dog', 'Beagle', '3 Years', 'Male', 1, 0, 'PetHome Admin', 'admin@pethome.com', 'dog_charlie', 'system@pethome.com', 0, 'Needs long walks, loves to sniff everything!')");
    }

    private void insertDummyShelters(SQLiteDatabase db) {
        // Ensure columns exist before inserting (safety check for upgrade path)
        try {
            db.execSQL("INSERT INTO USERS (NAME, EMAIL, CONTACT, PASSWORD, GENDER, CITY, ROLE, MEDICAL_INFO, LATITUDE, LONGITUDE) " +
                    "VALUES ('Happy Paws Shelter', 'shelter1@pethome.com', '9876543210', '123456', 'Other', 'Mumbai', 'NGO / Shelter', 'Full medical care provided', 19.0760, 72.8777)");

            db.execSQL("INSERT INTO USERS (NAME, EMAIL, CONTACT, PASSWORD, GENDER, CITY, ROLE, MEDICAL_INFO, LATITUDE, LONGITUDE) " +
                    "VALUES ('Meow pets', 'shelter2@pethome.com', '9123456780', '123456', 'Other', 'Ahmedabad', 'NGO / Shelter', 'Emergency rescue services', 23.0075, 72.5560)");

            db.execSQL("INSERT INTO USERS (NAME, EMAIL, CONTACT, PASSWORD, GENDER, CITY, ROLE, MEDICAL_INFO, LATITUDE, LONGITUDE) " +
                    "VALUES ('Malav Pet Clinic & Store', 'shelter3@pethome.com', '9988776655', '123456', 'Other', 'Ahmedabad', 'NGO / Shelter', 'Vaccination and checkups', 23.0153, 72.5232)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 7) {
            db.execSQL("CREATE TABLE IF NOT EXISTS FAVORITES (ID INTEGER PRIMARY KEY AUTOINCREMENT, USEREMAIL TEXT, PETID INTEGER)");
        }
        
        // Safely add columns if they don't exist
        addColumnIfNotExists(db, "USERS", "MEDICAL_INFO", "TEXT");
        addColumnIfNotExists(db, "USERS", "LATITUDE", "REAL");
        addColumnIfNotExists(db, "USERS", "LONGITUDE", "REAL");
        
        if (oldVersion < 9) {
            addColumnIfNotExists(db, "PETS", "CARE_INFO", "TEXT");
        }
        if (oldVersion < 10) {
            db.execSQL("CREATE TABLE IF NOT EXISTS DOCTORS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SPECIALIZATION TEXT, CONTACT TEXT, EXPERIENCE TEXT, SHELTER_EMAIL TEXT)");
        }
        if (oldVersion < 13) {
            insertDummyShelters(db);
        }
        if (oldVersion < 14) {
             db.execSQL("CREATE TABLE IF NOT EXISTS CHAT_HISTORY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SYMPTOMS TEXT," +
                "DISEASE TEXT," +
                "URGENCY TEXT," +
                "TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    private void addColumnIfNotExists(SQLiteDatabase db, String tableName, String columnName, String columnType) {
        try {
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType);
        } catch (Exception ignored) {
            // Column already exists
        }
    }

    // --- CHAT HISTORY SECTION ---

    public void saveChatHistory(String symptoms, String disease, String urgency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SYMPTOMS", symptoms);
        values.put("DISEASE", disease);
        values.put("URGENCY", urgency);
        db.insert(TABLE_CHAT_HISTORY, null, values);
    }

    public Cursor getChatHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM CHAT_HISTORY ORDER BY TIMESTAMP DESC", null);
    }

    // --- DOCTORS SECTION ---

    public boolean insertDoctor(String name, String spec, String contact, String exp, String shelterEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SPECIALIZATION", spec);
        values.put("CONTACT", contact);
        values.put("EXPERIENCE", exp);
        values.put("SHELTER_EMAIL", shelterEmail);
        return db.insert(TABLE_DOCTORS, null, values) != -1;
    }

    public Cursor getDoctorsByShelter(String shelterEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM DOCTORS WHERE SHELTER_EMAIL=?", new String[]{shelterEmail});
    }

    public Cursor getAllDoctorsWithShelter() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT D.*, U.NAME as SHELTER_NAME FROM DOCTORS D INNER JOIN USERS U ON D.SHELTER_EMAIL = U.EMAIL", null);
    }

    public void deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCTORS, "ID=?", new String[]{String.valueOf(id)});
    }

    // --- OTHER METHODS ---

    public boolean insertPet(String petName, String species, String breed, String age, String gender,
                             boolean goodWithChildren, boolean goodWithOtherPets, String contactName,
                             String contactInfo, String imagePath, String ownerEmail, String careInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PETNAME", petName);
        values.put("SPECIES", species);
        values.put("BREED", breed);
        values.put("AGE", age);
        values.put("GENDER", gender);
        values.put("GOODWITHCHILDREN", goodWithChildren ? 1 : 0);
        values.put("GOODWITHOTHERPETS", goodWithOtherPets ? 1 : 0);
        values.put("CONTACTNAME", contactName);
        values.put("CONTACTINFO", contactInfo);
        values.put("IMAGE", imagePath);
        values.put("OWNEREMAIL", ownerEmail);
        values.put("IS_REQUESTED", 0);
        values.put("CARE_INFO", careInfo);
        return db.insert(TABLE_PETS, null, values) != -1;
    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USERS WHERE (EMAIL=? OR CONTACT=?) AND PASSWORD=?", new String[]{email, email, password});
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT NAME FROM USERS WHERE EMAIL=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return null;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USERS WHERE EMAIL=?", new String[]{email});
    }

    public Cursor getAllAvailablePets() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM PETS WHERE IS_REQUESTED = 0", null);
    }

    public Cursor getPetsByOwner(String ownerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM PETS WHERE OWNEREMAIL=?", new String[]{ownerEmail});
    }

    public void deletePet(int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, "PETID=?", new String[]{String.valueOf(petId)});
        db.delete(TABLE_REQUESTS, "PETID=?", new String[]{String.valueOf(petId)});
    }

    public boolean insertRequest(int petId, String name, String breed, String age, String gender, String image, String requestUser, String ownerEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("PETID", petId);
            values.put("PETNAME", name);
            values.put("BREED", breed);
            values.put("AGE", age);
            values.put("GENDER", gender);
            values.put("IMAGE", image);
            values.put("REQUESTUSER", requestUser);
            values.put("OWNEREMAIL", ownerEmail);
            values.put("STATUS", "PENDING");

            long result = db.insert(TABLE_REQUESTS, null, values);

            if (result != -1) {
                ContentValues petValues = new ContentValues();
                petValues.put("IS_REQUESTED", 1);
                db.update(TABLE_PETS, petValues, "PETID=?", new String[]{String.valueOf(petId)});
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public boolean hasAlreadyRequested(int petId, String userEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM REQUESTS WHERE PETID=? AND REQUESTUSER=?", new String[]{String.valueOf(petId), userEmail});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void approveRequest(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS","APPROVED");
        db.update(TABLE_REQUESTS, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void rejectRequest(int id, int petId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("STATUS","REJECTED");
            db.update(TABLE_REQUESTS, values, "ID=?", new String[]{String.valueOf(id)});

            ContentValues petValues = new ContentValues();
            petValues.put("IS_REQUESTED", 0);
            db.update(TABLE_PETS, petValues, "PETID=?", new String[]{String.valueOf(petId)});
            
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getRequestsForOwner(String ownerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM REQUESTS WHERE OWNEREMAIL=?", new String[]{ownerEmail});
    }

    public Cursor getRequestsByUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM REQUESTS WHERE REQUESTUSER=?", new String[]{userEmail});
    }

    public Cursor getAdoptionHistory(String email, boolean isShelter) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (isShelter) {
            return db.rawQuery("SELECT * FROM REQUESTS WHERE OWNEREMAIL=? AND STATUS='APPROVED'", new String[]{email});
        } else {
            return db.rawQuery("SELECT * FROM REQUESTS WHERE REQUESTUSER=? AND STATUS='APPROVED'", new String[]{email});
        }
    }

    public void updateMedicalInfo(String email, String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MEDICAL_INFO", info);
        db.update("USERS", values, "EMAIL=?", new String[]{email});
    }

    public Cursor getAllShelters() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM USERS WHERE ROLE='NGO / Shelter'", null);
    }

    public boolean isFavorite(String email, int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FAVORITES WHERE USEREMAIL=? AND PETID=?", new String[]{email, String.valueOf(petId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void addFavorite(String email, int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USEREMAIL", email);
        values.put("PETID", petId);
        db.insert(TABLE_FAVORITES, null, values);
    }

    public void removeFavorite(String email, int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, "USEREMAIL=? AND PETID=?", new String[]{email, String.valueOf(petId)});
    }

    public Cursor getFavoritePets(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT P.* FROM PETS P INNER JOIN FAVORITES F ON P.PETID = F.PETID WHERE F.USEREMAIL=?", new String[]{email});
    }
}
