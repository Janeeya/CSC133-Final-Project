package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundEffectStrategy implements IAudioStrategy {
    private SoundPool soundPool;
    private final int eatSoundId;
    private final int crashSoundId;
    private final int slowmoSoundId;

    public SoundEffectStrategy(Context context) {
        this.soundPool = createSoundPool();
        this.eatSoundId = loadSound(context, R.raw.biscuit_bark);
        this.crashSoundId = loadSound(context, R.raw.dog_death);
        this.slowmoSoundId = loadSound(context, R.raw.slowmo);
    }

    private SoundPool createSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        return new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    private int loadSound(Context context, int soundResource) {
        return soundPool.load(context, soundResource, 1);
    }

    @Override
    public void apply(GameEventType eventType) {
        switch (eventType) {
            case EAT:
                playSound(eatSoundId);
                break;
            case CRASH:
                playSound(crashSoundId);
                break;
            case POWERUP:
                playSound(slowmoSoundId);
                break;
        }
    }

    private void playSound(int soundId) {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    @Override
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}

