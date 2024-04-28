package com.gamecodeschool.snakeapplication;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.media.MediaPlayer;

public class SnakeActivity extends Activity {

    private SnakeGame snakeGame;
    private MediaPlayer mediaPlayer;

    // Set the game up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGame();
        initializeMediaPlayer();
    }

    private void initializeGame() {
        // Get the pixel dimensions of the screen
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        snakeGame = new SnakeGame(this, size);
        setContentView(snakeGame);
    }

    private void initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.breezy);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();
    }

    private void resumeGame() {
        snakeGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    private void pauseGame() {
        snakeGame.pause();
    }
}