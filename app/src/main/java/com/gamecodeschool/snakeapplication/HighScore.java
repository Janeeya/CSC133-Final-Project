package com.gamecodeschool.snakeapplication;
import android.content.Context;
import android.content.SharedPreferences;
public class HighScore {
    private static final String PREF_NAME = "SnakeHighScores";
    private static final String KEY_HIGH_SCORE = "high_score";

    private SharedPreferences mSharedPreferences;

    public HighScore(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getHighScore() {
        return mSharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, score);
        editor.apply();
    }

    public boolean isNewHighScore(int score) {
        return score > getHighScore();
    }
}
