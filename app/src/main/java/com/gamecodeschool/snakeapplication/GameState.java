package com.gamecodeschool.snakeapplication;

import android.content.Context;

class GameState {
    private boolean paused = true;
    private boolean playing = true;
    private int score = 0;
    private HighScore highScore;
    private String playerName = "";

    public GameState(Context context){
        this.highScore = new HighScore(context);
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getHighScore(){
        return highScore.getHighScore();
    }

    public boolean isHighScore(){
        if(highScore.isNewHighScore(this.score)){
            return true;
        }
        else {
            return false;
        }
    }

    public void setHighScore(){
        highScore.setHighScore(this.score);
    }

    public void increaseScore(){
        score++;
    }

    public void resetGame(){
        score = 0;
        paused = true;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String name){
        playerName = name;
    }
}
