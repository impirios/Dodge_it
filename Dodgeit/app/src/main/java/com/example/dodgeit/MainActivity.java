package com.example.dodgeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button Play,Exit,Stats;
    ImageView plane;
    Timer timer;
    Handler handler;
    private int planePosX = 0;
    private int ScreenWidth;
    //private int ScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Play = findViewById(R.id.ClicktoP);
        Exit = findViewById(R.id.exit);
        Stats = findViewById(R.id.stats);
        plane = findViewById(R.id.plane);
        timer = new Timer();
        handler = new Handler();


        //Getting the screen width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        //ScreenHeight = size.y;



        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent intent = new Intent(getBaseContext(), MainGame.class);
                startActivity(intent);
            }
        });


        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });

        Stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent intent = new Intent(getBaseContext(), statistics.class);
                startActivity(intent);
            }
        });



        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePosX();

                    }
                });
            }
        }, 0, 20);








    }

    public void changePosX()
    {
        if(planePosX>ScreenWidth)
        {
            planePosX = 0-plane.getWidth();
        }
        planePosX+=3;
        plane.setX(planePosX);
    }


}
