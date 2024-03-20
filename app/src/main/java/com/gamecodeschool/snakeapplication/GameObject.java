package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.graphics.Point;

public class GameObject {
    protected Context context;
    protected Point blockBoundary;
    protected int size;

    public GameObject(Context context, Point blockBoundary, int size) {
        this.context = context;
        this.blockBoundary = blockBoundary;
        this.size = size;
    }

    public void spawn() {

    }
}
