package com.gamecodeschool.snakeapplication;
import android.content.Context;
import android.content.SharedPreferences;

public class HighScore {
    private static final String PREF_NAME = "SnakeHighScores";
    private static final String KEY_HIGH_SCORE = "high_score";

    private final SharedPreferences sharedPreferences;
    private int highScore;

    public HighScore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadHighScore();
    }

    private void loadHighScore() {
        highScore = sharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        if (isNewHighScore(score)) {
            highScore = score;
            saveHighScore();
        }
    }

    private void saveHighScore() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, highScore);
        editor.apply();
    }

    public boolean isNewHighScore(int score) {
        return score > highScore;
    }
}