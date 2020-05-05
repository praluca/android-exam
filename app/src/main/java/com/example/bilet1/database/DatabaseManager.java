package com.example.bilet1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bilet1.Citat;
import com.example.bilet1.database.dao.CitatDao;

@Database(entities = {Citat.class}, exportSchema = false, version = 1)
public abstract class DatabaseManager extends RoomDatabase {
    private static final String DB_NAME="citat_db";
    private static  DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if(databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if(databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, DB_NAME).fallbackToDestructiveMigration().build();
                    return databaseManager;
                }
            }
        }
        return databaseManager;
    }

    public abstract CitatDao getCitatDao();
}
