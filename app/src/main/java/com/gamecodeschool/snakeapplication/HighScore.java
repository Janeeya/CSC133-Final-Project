package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScore {
    private static final String PREF_NAME = "SnakeHighScores";
    private static final String KEY_HIGH_SCORE = "high_score";

    private final SharedPreferences mSharedPreferences;

    private int mHighScore;

    public HighScore(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mHighScore = mSharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public int getHighScore() {
        return mHighScore;
    }

    public void setHighScore(int score) {
        if (score > mHighScore) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.apply();
            mHighScore = score;
        }
    }

    public boolean isNewHighScore(int score) {
        return score > mHighScore;
    }
}