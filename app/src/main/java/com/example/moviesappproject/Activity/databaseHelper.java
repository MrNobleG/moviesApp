package com.example.moviesappproject.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String databaseName= "SignUp.db";
    public databaseHelper(@Nullable Context context) {
        super(context, "SignUp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS allusers(username TEXT primary key,fullname TEXT,email TEXT,password TEXT)");
        MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imdbId TEXT , " +
                "username TEXT, " +
                "FOREIGN KEY(username) REFERENCES allusers(username) ON DELETE CASCADE, " +
                "UNIQUE(username, imdbId) ON CONFLICT REPLACE)");
    }



    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {

    }
    public Boolean insertData(String userName,String fullName,String email,String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",userName);
        contentValues.put("fullname",fullName);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long result = MyDatabase.insert("allusers",null,contentValues);
        if (result ==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean checkInputs(String username,String fullName,String email,String password){
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("Select * from allusers where username= ? and fullname= ? and email = ? and password = ?",new String[]{username,fullName,email,password});
        if (cursor.getCount()>0){
            return  true;
        }else{
            return false;
        }
    }
    public Boolean checkusernamePassword(String username,String Password){
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("Select * from allusers where username = ? and password = ?",new String[]{username,Password});
        if (cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * from allusers WHERE email = ?", new String[]{email});
        try {
            if (cursor.moveToFirst()) {
                String userName = cursor.getString(cursor.getColumnIndex("username"));
                String fullName = cursor.getString(cursor.getColumnIndex("fullname"));

                user = new User(userName, fullName);
            }
        } finally {
            cursor.close();
        }
        return user;
    }


}
