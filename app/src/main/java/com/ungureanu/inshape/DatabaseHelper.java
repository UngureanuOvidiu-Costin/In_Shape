package com.ungureanu.inshape;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "inshape.db";
    public static final String TABLE_NAME = "ex_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME"; //Name
    public static final String COL_3 = "KG"; //Surname
    public static final String COL_4 = "SETS"; //Marks
    public static final String COL_5 = "REPS"; // :))
    public static final String COL_6 = "DAY"; // :))
    public static final int version = 1;
    private static final String TAG = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
        //Log.d(TAG, "DatabaseHelper: called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(TAG, "onCreate: called");
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, KG TEXT, SETS TEXT, REPS TEXT, DAY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Log.d(TAG, "onUpgrade: called");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Name, String Kg, String Sets, String Reps, String Day){
        //Log.d(TAG, "insertData: called");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Name);
        contentValues.put(COL_3, Kg);
        contentValues.put(COL_4, Sets);
        contentValues.put(COL_5, Reps);
        contentValues.put(COL_6, Day);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        return true;
    }

    public Cursor getAllData(String filterData){
        //Log.d(TAG, "getAllData: called");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE DAY = ?", new String[] {filterData});
        return res;
    }

    public Integer deleteData(String Name, String Kgs, String Sets, String Reps){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ? AND KG = ? AND SETS = ? AND REPS = ?", new String[] {Name, Kgs, Sets, Reps});
    }
}
