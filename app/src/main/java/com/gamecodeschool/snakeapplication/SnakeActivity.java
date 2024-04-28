package com.gamecodeschool.snakeapplication;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.media.MediaPlayer;

public class SnakeActivity extends Activity {

    SnakeGame mSnakeGame;
    MediaPlayer mediaPlayer;

    // Set the game up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        mSnakeGame = new SnakeGame(this, size);

        setContentView(mSnakeGame);

        mediaPlayer = MediaPlayer.create(this, R.raw.breezy);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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
