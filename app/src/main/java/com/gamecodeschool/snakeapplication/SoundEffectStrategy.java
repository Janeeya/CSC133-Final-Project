package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundEffectStrategy implements IAudioStrategy {
    private final Context context;
    private SoundPool soundPool;
    private int eatSoundId;
    private int crashSoundId;

    public SoundEffectStrategy(Context context) {
        this.context = context;
        initSoundPool();
        loadSounds();
    }

    private void initSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    private void loadSounds() {
        eatSoundId = soundPool.load(context, R.raw.biscuit_bark, 1);
        crashSoundId = soundPool.load(context, R.raw.dog_death, 1);
    }

    @Override
    public void apply(GameEventType eventType) {
        playSound(eventType);
    }

    private void playSound(GameEventType eventType) {
        switch (eventType) {
            case EAT:
                soundPool.play(eatSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
                break;
            case CRASH:
                soundPool.play(crashSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
                break;
        }
    }

    @Override
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}