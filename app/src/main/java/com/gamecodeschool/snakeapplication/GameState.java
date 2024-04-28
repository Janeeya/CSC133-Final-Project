package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.content.SharedPreferences;

final class GameState {
    private static volatile boolean mThreadRunning = false; //is the thread running?
    private static volatile boolean mPaused = true; //is the game paused?
    private static volatile boolean mGameOver = true; //is the game over?
    private static volatile boolean mDrawing = false; //should the game be drawing?
    private GameStarter gameStarter;
    private int mScore; //current score
    private int mHighScore; //best Score?
    private SharedPreferences.Editor mEditor; //to save high scores

    GameState(Context context){


        SharedPreferences prefs = context.getSharedPreferences("HiScore", Context.MODE_PRIVATE);

        mEditor = prefs.edit();

        mHighScore = prefs.getInt("hi_score", 0);
    }

    private void endGame(){
        mGameOver = true;
        mPaused = true;
        if(mScore > mHighScore){
            mEditor.putInt("hi_score", mHighScore);
            mEditor.commit();
        }
    }

    void startNewGame(){ //also
        mScore = 0;
        stopDrawing();
        gameStarter.deSpawnReSpawn();
        resume();
        startDrawing();
    }

    void increaseScore(){
        mScore++;
    }

    int getScore(){
        return mScore;
    }

    int getHighScore(){
        return mHighScore;
    }

    void pause(){
        mPaused = true;
    }

    void resume(){
        mGameOver = false;
        mPaused = false;
    }

    void stopEverything(){
        mPaused = true;
        mGameOver = true;
        mThreadRunning = false;
    }

    boolean getThreadRunning(){
        return mThreadRunning;

    }
    void stopThread() {
        mThreadRunning = false;
    }

    void startThread(){
        mThreadRunning = true;
    }
    private void stopDrawing(){
        mDrawing = false;
    }
    private void startDrawing(){
        mDrawing = true;
    }
    boolean getDrawing() {
        return mDrawing;
    }
    boolean getPaused(){
        return mPaused;
    }
    boolean getGameOver(){
        return mGameOver;
    }

}
