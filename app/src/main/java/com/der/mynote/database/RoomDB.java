package com.der.mynote.database;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.der.mynote.model.Note;

@Database(entities = {Note.class}, exportSchema = false, version = 1)
public abstract class RoomDB extends RoomDatabase {

    public static final String DB_NAME = "note_db";
    public static RoomDB database;

    public abstract MainDao mainDao();

    public synchronized static RoomDB getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DB_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }





}
