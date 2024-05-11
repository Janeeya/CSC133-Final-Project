package com.gamecodeschool.snakeapplication;

public interface IAudioStrategy {
    void apply(GameEventType eventType);
    void release();
}
