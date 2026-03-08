package com.pethome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Pethome.db";
    private static final int DATABASE_VERSION = 7; // Bumped version to add FAVORITES table

    private static final String TABLE_PETS = "PETS";
    private static final String TABLE_REQUESTS = "REQUESTS";
    private static final String TABLE_FAVORITES = "FAVORITES";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS(" +
                "USERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "EMAIL TEXT," +
                "CONTACT TEXT," +
                "PASSWORD TEXT," +
                "GENDER TEXT," +
                "CITY TEXT," +
                "ROLE TEXT)");

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
                "IS_REQUESTED INTEGER DEFAULT 0)");

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

        // Insert Dummy Data
        insertDummyPets(db);
    }

    private void insertDummyPets(SQLiteDatabase db) {
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED) " +
                "VALUES ('Buddy', 'Dog', 'Golden Retriever', '2 Years', 'Male', 1, 1, 'PetHome Admin', 'admin@pethome.com', 'dog_buddy', 'system@pethome.com', 0)");
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED) " +
                "VALUES ('Luna', 'Cat', 'Persian', '1 Year', 'Female', 1, 1, 'PetHome Admin', 'admin@pethome.com', 'cat_luna', 'system@pethome.com', 0)");
        db.execSQL("INSERT INTO PETS (PETNAME, SPECIES, BREED, AGE, GENDER, GOODWITHCHILDREN, GOODWITHOTHERPETS, CONTACTNAME, CONTACTINFO, IMAGE, OWNEREMAIL, IS_REQUESTED) " +
                "VALUES ('Charlie', 'Dog', 'Beagle', '3 Years', 'Male', 1, 0, 'PetHome Admin', 'admin@pethome.com', 'dog_charlie', 'system@pethome.com', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 7) {
            db.execSQL("CREATE TABLE IF NOT EXISTS FAVORITES (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "USEREMAIL TEXT," +
                    "PETID INTEGER)");
        }
    }

    public boolean insertPet(String petName, String species, String breed, String age, String gender,
                             boolean goodWithChildren, boolean goodWithOtherPets, String contactName,
                             String contactInfo, String imagePath, String ownerEmail) {
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

    // --- FAVORITES SECTION ---

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
