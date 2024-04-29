package com.gamecodeschool.snakeapplication;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;



public class SnakeActivity extends Activity {
    BackgroundMusicStrategy musicPlayer;
    SnakeGame mSnakeGame;



    // Set the game up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicPlayer = new BackgroundMusicStrategy(this);
        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        mSnakeGame = new SnakeGame(this, size);

        setContentView(mSnakeGame);
        musicPlayer.startMusic();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSnakeGame.resume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeGame.pause();


    }
}
