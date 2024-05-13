package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.util.Log;

class GameState {
    private boolean paused = true;
    private boolean playing = false;
    private int score = 0;
    private HighScore highScore;
    private int mFPS = 10;
    private String playerName = "";
    private int poweredUp = 0;
    //private static final String TAG = "GameState";

    public GameState(Context context){
        //Log.d(TAG, "Initializing GameState");
        this.highScore = new HighScore(context);
    }

    public boolean isPlaying() {
        //Log.d(TAG, "isPlaying: " + playing);
        return playing;
    }

    public boolean isPaused() {
        //Log.d(TAG, "isPaused: " + paused);
        return paused;
    }

    public void pause(){
        paused = true;
        //Log.d(TAG, "setPaused: " + true);
    }

    public void resume(){
        paused = false;
        //Log.d(TAG, "setPaused: " + false);
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        //Log.d(TAG, "setPlaying: " + playing);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getmFPS() { return mFPS; }

    public int getHighScore(){
        return highScore.getHighScore();
    }

    public String getHighScoreName(){ return highScore.getHighScoreName(); }

    public void slowmo(){
        poweredUp = 1;
        mFPS = mFPS/2;
    }
    public boolean isHighScore(){
        if(highScore.isNewHighScore(this.score)){
            return true;
        }
        else {
            return false;
        }
    }

    public void nullFix(){
        highScore.setHighScoreOverride(0, "Austin");
    }
    public void setHighScore(){
        highScore.setHighScore(this.score, playerName);
    }

    public void increaseScore(){
        score++;
        if(score%2==0){
            if(poweredUp<1) {
                mFPS++;
            }
            else{
                poweredUp--;
            }
        }
    }

    public void setmFPS(int FPS){ //unused currently, but adds function for slowdown effect
        this.mFPS = FPS;
    }

    public void resetGame(){
        score = 0;
        paused = true;
        mFPS = 10;
        poweredUp = 0;
        //Log.d(TAG, "resetGame");
    }

    public void newGame(){
        score = 0;
        mFPS = 10;
        poweredUp = 0;
        //Log.d(TAG, "newGame");
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String name){
        playerName = name;
    }
}
