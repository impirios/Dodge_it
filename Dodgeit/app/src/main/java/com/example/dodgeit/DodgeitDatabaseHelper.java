package com.example.dodgeit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class DodgeitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Dodgeit.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "User_data";
    private static final String COLUMNSCORE = "score";
    private static final String COLUMNNAME = "user_name";
    private static final String _ID = "_ID";
    private Cursor res;

    DodgeitDatabaseHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "create table "+TABLE_NAME+"("+_ID+" integer primary key autoincrement,"+COLUMNNAME+" text,"+COLUMNSCORE+" integer"+")";
        db.execSQL(CREATE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public void insertData(SQLiteDatabase db,String user,int Score)
    {
        ContentValues info = new ContentValues();
        info.put("user_name",user);
        info.put("score",Score);
        db.insert(TABLE_NAME,null,info);
    }




}
