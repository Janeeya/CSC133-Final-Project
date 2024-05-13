package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundEffectStrategy implements IAudioStrategy {
    private Context context;
    private SoundPool soundPool;
    private int eatSoundId;
    private int crashSoundId;
    private int slowmoSoundId;

    public SoundEffectStrategy(Context context) {
        this.context = context;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        eatSoundId = soundPool.load(context, R.raw.biscuit_bark, 1);
        crashSoundId = soundPool.load(context, R.raw.dog_death, 1);
        slowmoSoundId = soundPool.load(context, R.raw.slowmo, 1);
    }

    @Override
    public void apply(GameEventType eventType) {
        if (eventType == GameEventType.EAT) {
            soundPool.play(eatSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        } else if (eventType == GameEventType.CRASH) {
            soundPool.play(crashSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        } else if (eventType == GameEventType.POWERUP) {
            soundPool.play(slowmoSoundId, 1.0f, 1.0f, 1, 0, 1.0f);

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
