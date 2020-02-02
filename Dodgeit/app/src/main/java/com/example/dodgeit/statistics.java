package com.example.dodgeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class statistics extends AppCompatActivity {

    TextView highscore,TotalGames,Average;
    SQLiteDatabase mydatabase;
    SQLiteOpenHelper db;
    String UserName;
    int Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        db = new DodgeitDatabaseHelper(this);
        highscore = findViewById(R.id.hsno);
        TotalGames = findViewById(R.id.TotalGames);
        Average = findViewById(R.id.average);

        try {
            mydatabase = db.getReadableDatabase();
            Cursor cursor = mydatabase.query("user_data", new String[]{"MAX(score) AS avgscore"}, null, null, null, null, null);
            if (cursor.moveToLast()) {
                int max = cursor.getInt(0);
                highscore.setText(String.valueOf(max));
            }

            cursor = mydatabase.query("user_data", new String[]{"COUNT(_ID) as count"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                TotalGames.setText("Total Flights: " + count);
            }

            cursor = mydatabase.query("user_data", new String[]{"AVG(score) as score"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                float average = cursor.getInt(0);
                Average.setText("Average: " + average);
            }
            db.close();
            cursor.close();
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
        }


    }
}
