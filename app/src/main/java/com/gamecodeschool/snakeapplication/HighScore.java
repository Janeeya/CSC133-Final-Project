package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScore {
    private static final String PREF_NAME = "SnakeHighScores";
    private static final String KEY_HIGH_SCORE = "high_score";

    private static final String KEY_HIGH_SCORE_NAME = "high_score_name";

    private final SharedPreferences mSharedPreferences;

    private int mHighScore;
    private String mHighScoreName;

    public HighScore(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mHighScore = mSharedPreferences.getInt(KEY_HIGH_SCORE, 0);
        mHighScoreName = mSharedPreferences.getString(KEY_HIGH_SCORE_NAME, "No High Score Set");
    }

    public int getHighScore() {
        return mHighScore;
    }

    public void setHighScore(int score, String name) {
        if (isNewHighScore(score)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.apply();
            mHighScore = score;
            setHighScoreName(name);
        }
    }

    public void setHighScoreName(String name) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_HIGH_SCORE_NAME, name);
            editor.apply();
            mHighScoreName = name;
    }
    //Set HighScoreOverride is identical to setHighScore without checking if score is a new highscore
    public void setHighScoreOverride(int score, String name){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, score);
        editor.apply();
        mHighScore = score;
        setHighScoreName(name);
    }
    public String getHighScoreName(){
        return mHighScoreName;
    }

    public boolean isNewHighScore(int score) {
        return score > mHighScore;
    }

}