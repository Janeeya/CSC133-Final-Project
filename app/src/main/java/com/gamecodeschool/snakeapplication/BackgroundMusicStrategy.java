package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusicStrategy implements IAudioStrategy {
    private final MediaPlayer mediaPlayer;

    public BackgroundMusicStrategy(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.breezy);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void apply(GameEventType eventType) {
        switch (eventType) {
            case START:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;

            case STOP:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
                break;

            case PAUSE:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;

            case RESUME:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;

            default:
                break;
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
