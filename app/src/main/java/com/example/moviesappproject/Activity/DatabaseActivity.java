package com.example.moviesappproject.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DatabaseActivity extends AppCompatActivity {
    private static final String TAG = "DatabaseAccess";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the IMDb ID and checkbox state from intent extras
        String imdbId = getIntent().getStringExtra("imdbId");
        String username =getIntent().getStringExtra("username");
        boolean isChecked = getIntent().getBooleanExtra("isChecked", false);

        // Open the database
        databaseHelper favDB = new databaseHelper(this);
        db = favDB.getWritableDatabase();

        // Check the checkbox state and perform insert or delete operation accordingly
        if (isChecked) {
            insertFavorite(imdbId,username);
        } else {
            deleteFavorite(imdbId,username);
        }

        // Close the database

        // Finish the activity
        finish();
    }

    private void insertFavorite(String imdbId,String username) {
        ContentValues values = new ContentValues();
        values.put("imdbId", imdbId);
        values.put("username", username);
        long result = db.insert("favorites", null, values);
        if (result == -1) {
            Log.e(TAG, "Error inserting favorite: " + imdbId+"//"+username);
        } else {
            Log.d(TAG, "Favorite inserted successfully: " + imdbId+"//"+username);
        }
    }

    private void deleteFavorite(String imdbId,String username) {
        int result = db.delete("favorites", "imdbId = ? AND username = ?", new String[]{imdbId, username});
        if (result == 0) {
            Log.e(TAG, "Favorite not found for deletion: " + imdbId+"//"+username);
        } else {
            Log.d(TAG, "Favorite deleted successfully: " + imdbId+"//"+username);
        }
    }



}
