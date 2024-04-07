package com.example.fitfurlife;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class databaseHelper extends SQLiteOpenHelper {
    private Context context = null;
    private static final int DATABASE_VERSION =1;
    
    public databaseHelper(@Nullable Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the table
        String CREATE_ACC_GYRO_TABLE = "CREATE TABLE " + Config.TABLE_SENSOR + " ("
                + Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_ACC + " FLOAT NOT NULL, "
                + Config.COLUMN_GYRO + " FLOAT NOT NULL, "
                + Config.COLUMN_TIME + " FLOAT NOT NULL);";

        String CREATE_DOG_PROFILE_TABLE = "CREATE TABLE dogProfile ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dogName TEXT NOT NULL, "
                + "dogAge INTEGER NOT NULL, "
                + "dogMass FLOAT NOT NULL, "
                + "dogRace TEXT NOT NULL);";

        // Execute the SQL statements to create the tables
        db.execSQL(CREATE_ACC_GYRO_TABLE);
        db.execSQL(CREATE_DOG_PROFILE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int insertAll(accGyro accGyro) {
        int id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ACC, accGyro.getAcceleration());
        contentValues.put(Config.COLUMN_GYRO, accGyro.getGyroscope());
        contentValues.put(Config.COLUMN_TIME, accGyro.getTime());

        try {
            id = (int) db.insertOrThrow(Config.TABLE_SENSOR, null, contentValues);
        } catch (final SQLException e) {
            // Using a Handler to post Toast message to the main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "InsertaccGyro Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            db.close();
        }
        return id;
    }

    public long insertDogProfile(dogProfile profile) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dogName", profile.getDogName());
        contentValues.put("dogAge", profile.getDogAge());
        contentValues.put("dogMass", profile.getDogMass());
        contentValues.put("dogRace", profile.getDogRace());

        try {
            id = db.insertOrThrow("dogProfile", null, contentValues);
        } catch (SQLException e) {
            Toast.makeText(context, "Insert DogProfile Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
        return id;
    }

    public List<accGyro> getAll()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<accGyro> accGyroList = new ArrayList<>();

        try{
            cursor = db.query(Config.TABLE_SENSOR, null, null, null, null, null,null,null);
            if (cursor != null) {
                if (cursor.moveToFirst()){
                    do{
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));
                        @SuppressLint("Range") float acceleration = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_ACC));
                        @SuppressLint("Range") float gyroscope = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_GYRO));
                        @SuppressLint("Range") float time = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_TIME));

                        accGyro accGyro = new accGyro(id, acceleration, gyroscope, time);
                        accGyroList.add(accGyro);
                    }while (cursor.moveToNext());
                    return accGyroList;
                }
            }
        }catch (SQLException e){
            Toast.makeText(context, "Error getting all items " + e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return Collections.emptyList();
    }

    public List<Float> getAllAcceleration() {
        List<Float> accelerationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Config.COLUMN_ACC};

        try (Cursor cursor = db.query(Config.TABLE_SENSOR, columns, null, null, null, null, null)) {
            while (cursor != null && cursor.moveToNext()) {
                @SuppressLint("Range") float acceleration = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_ACC));
                accelerationList.add(acceleration);
            }
            Log.d("databaseHelper", "Acceleration list fetched, size: " + accelerationList.size());
        } catch (SQLException e) {
            Log.e("databaseHelper", "Error getting all acceleration values: " + e.getMessage());
        }

        return accelerationList;
    }
    public List<dogProfile> getAllDogProfiles() {
        List<dogProfile> dogProfileList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("dogProfile", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String dogName = cursor.getString(cursor.getColumnIndex("dogName"));
                    @SuppressLint("Range") int dogAge = cursor.getInt(cursor.getColumnIndex("dogAge"));
                    @SuppressLint("Range") float dogMass = cursor.getFloat(cursor.getColumnIndex("dogMass"));
                    @SuppressLint("Range") String dogRace = cursor.getString(cursor.getColumnIndex("dogRace"));

                    dogProfile profile = new dogProfile(id, dogName, dogAge, dogMass, dogRace);
                    dogProfileList.add(profile);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Error getting all dog profiles: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return dogProfileList;
    }
}

