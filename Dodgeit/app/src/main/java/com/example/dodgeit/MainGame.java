package com.example.dodgeit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity {
    //Declaring all the variables required for the game
    private ImageView Character;
    private ImageView Health;
    private ImageView missiles;
    private ImageView buildings;
    private boolean actn_flag = false;
    private Handler handler = new Handler();
    private Timer timer;
    private int CharacterPosX, CharacterPosY;
    int GameScreenHeight;
    int CharacterHeight;
    int ScreenWidth, ScreenHeight;
    int HealthPosY, HealthPosX;
    int MissilePosX, MissilePosY;
    int buildingsPosX;
    private TextView ScoreBoard;
    private FrameLayout GameScreen;
    private int Score = 0;
    private int minimum = -1;
    private boolean GameStarted = false;
    private static int level;
    private int Mincrementer;
    private int Hincrementer;
    private int CincrementerX;
    private int CincrementerY;
    private int Bincrementer;
    private int oldCoin;
    private int Sincrementer;
    private int Coin;
    SQLiteDatabase mydatabase;
    DodgeitDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //Assigning the views to the variables through there View Id's in the Resources file
        Character = findViewById(R.id.character);
        GameScreen = findViewById(R.id.GameScreen);
        ScoreBoard = findViewById(R.id.Score);
        missiles = findViewById(R.id.missiles);
        Health = findViewById(R.id.health);
        buildings = findViewById(R.id.buildings);
        db = new DodgeitDatabaseHelper(this);
        level = 1;
        CincrementerY=22;
        CincrementerX=14;
        Mincrementer=15;
        Hincrementer=7;
        Bincrementer=1;
        Sincrementer=2;
        Coin = 0;

        oldCoin=0;

        //Getting the screen width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;


        //Running the function check() and ChangePos() every 20 miliseconds

        if (!GameStarted)
        {
            missiles.setVisibility(View.INVISIBLE);
            Health.setVisibility(View.INVISIBLE);
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        check();
                        changePosY();

                    }
                });
            }
        }, 0, 20);



}




//The function that updates the Coordinates of the objects
    public void changePosY()
    {


        MissilePosX-=Mincrementer;
        HealthPosX-=Hincrementer;
        buildingsPosX-=Bincrementer;


        if(level_increased())
        {
            CincrementerX+=5;
            CincrementerY+=5;
            Mincrementer+=5;
            Hincrementer+=5;
            Bincrementer+=3;
            Sincrementer+=1;
        }

        if (buildingsPosX+buildings.getWidth()<0)
        {
            buildingsPosX = ScreenWidth+10;
        }
        if(MissilePosX<0)
        {
            MissilePosX = ScreenWidth + 30;
            MissilePosY = (int) Math.floor(Math.random()*(GameScreenHeight-missiles.getHeight()-50));

        }

        if(HealthPosX<0)
        {
            HealthPosX = ScreenWidth + 50;
            HealthPosY = (int) Math.floor(Math.random()*(GameScreenHeight-Health.getHeight()-50));

        }

        if(!actn_flag)
        {
            CharacterPosY+=CincrementerY;
        }
        else
        {
            CharacterPosY-=CincrementerX;
        }

        if(CharacterPosY<0) CharacterPosY = 0;

        if(CharacterPosY > (GameScreenHeight-CharacterHeight)) CharacterPosY = GameScreenHeight-CharacterHeight;

        Character.setY(CharacterPosY);

        missiles.setX(MissilePosX);
        missiles.setY(MissilePosY);

        Health.setX(HealthPosX);
        Health.setY(HealthPosY);

        buildings.setX(buildingsPosX);

        ScoreBoard.setText(toString().valueOf(Score)+"\n Level "+level);
        //ScoreBoard.setText("Score:-"+Score+","+CharacterPosY+","+GameScreenHeight+","+CharacterHeight);
    }


    private void check()//check for a missile hit or score boost
    {
        if(hitCheck() == 1)
        {

            timer.cancel();

            try {
                mydatabase = db.getWritableDatabase();
                db.insertData(mydatabase,"impirios",Score);
                mydatabase.close();
            }
            catch (SQLException e) {
                Toast toast = Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT);
            }
            finish();
        }

        if(healthCheck() == 1)
        {
            Score = Score+Sincrementer;
            Coin = Coin+1;
            HealthPosX = ScreenWidth + 50;
            HealthPosY = (int) Math.floor(Math.random()*(GameScreenHeight-Health.getHeight()-50));

        }

    }

    private int hitCheck()
    {

        if (CharacterPosY == minimum )
        {
            return 1;
        }
        else if ((MissilePosX <= CharacterPosX+Character.getWidth()) && (MissilePosY>=CharacterPosY&&MissilePosY<(CharacterPosY+CharacterHeight)))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }


    private boolean level_increased()//checks weather the level has increased or not
    {
        float d = Coin%8;
        if(d==0 && Coin!=0)
        {
            if(oldCoin!=Coin) {
                oldCoin = Coin;
                level = level + 1;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private int healthCheck()
    {
        if ((HealthPosX <= CharacterPosX+Character.getWidth()) && (HealthPosY>=CharacterPosY&&HealthPosY<(CharacterPosY+CharacterHeight)))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if(me.getAction() == MotionEvent.ACTION_DOWN)
        {
            GameStarted = true;
            missiles.setVisibility(View.VISIBLE);
            Health.setVisibility(View.VISIBLE);
            actn_flag = true;
            GameScreenHeight = GameScreen.getHeight();
            CharacterHeight = Character.getHeight();
            minimum = GameScreenHeight - CharacterHeight;

        }
        else
        {

            if(me.getAction() == MotionEvent.ACTION_UP)
            {
                actn_flag = false;
            }
        }
        return true;
    }

}
