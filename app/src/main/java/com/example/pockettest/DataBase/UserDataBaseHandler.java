/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.pockettest.Model.User;
import com.example.pockettest.Util.Constants;

public class UserDataBaseHandler extends SQLiteOpenHelper {
    private Context ctx;

    public UserDataBaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERISON);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.NAME + " VARCHAR(250), " + Constants.EMAIL + " VARCHAR(250), " +
                Constants.MOBILE + " VARCHAR(250), " + Constants.LOC + " VARCHAR(250));";
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    //CRUD OPERATIONS
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.NAME, user.getName());
        values.put(Constants.EMAIL, user.getEmail());
        values.put(Constants.MOBILE, user.getMobileNo());
        values.put(Constants.LOC, user.getLocation());
        db.insert(Constants.TABLE_NAME, null, values);
        db.close();

    }

    public User getUser(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        try{

            String rawQuery = "SELECT * FROM "+ Constants.TABLE_NAME;
            cursor =db.rawQuery(rawQuery, null);
            if(cursor != null)
                cursor.moveToFirst();

            User user = new User();

            user.setEmail(cursor.getString(cursor.getColumnIndex(Constants.EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndex(Constants.NAME)));
            user.setLocation(cursor.getString(cursor.getColumnIndex(Constants.LOC)));
            user.setMobileNo(cursor.getString(cursor.getColumnIndex(Constants.MOBILE)));

            return user;

        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }finally{
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return null;
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.NAME, user.getName());
        values.put(Constants.MOBILE, user.getMobileNo());
        values.put(Constants.LOC, user.getLocation());
        db.update(Constants.TABLE_NAME, values, Constants.EMAIL + "=?", new String[]{user.getEmail()});
        db.close();
    }

    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
        db.close();
    }
}
