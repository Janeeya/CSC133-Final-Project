package com.gamecodeschool.snakeapplication;

import android.media.MediaPlayer;
import android.content.Context;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class BackgroundMusicStrategy {

    private MediaPlayer mediaPlayer;
    private Context context;

    public BackgroundMusicStrategy(Context context){
        mediaPlayer = MediaPlayer.create(context, R.raw.breezy);
        mediaPlayer.setLooping(true);
    }

    public void startMusic(){
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    public void pauseMusic(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public boolean isMusicPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
