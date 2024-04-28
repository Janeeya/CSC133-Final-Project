package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Apple extends GameObject implements IDrawable {

    private Point location;
    private Point spawnRange;
    private int size;
    private Bitmap bitmapApple;

    Apple(Context context, Point sr, int s){
        super(context, sr, s);

        setSpawnRange(sr);
        setSize(s);
        hideApple();

        loadBitmapApple(context);
        resizeBitmapApple(s);
    }

    private void hideApple() {
        location = new Point(-10, 0);
    }

    private void loadBitmapApple(Context context) {
        bitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.croissant);
    }

    private void resizeBitmapApple(int s) {
        bitmapApple = Bitmap.createScaledBitmap(bitmapApple, s, s, false);
    }

    public void spawn(){
        Random random = new Random();
        setLocation(random.nextInt(getSpawnRange().x) + 1, random.nextInt(getSpawnRange().y - 1) + 1);
    }

    public void spawn(int amount) {
        for (int i = 0; i < amount; i++) {
            spawn();
        }
    }

    public Point getLocation(){
        return getLocationPoint();
    }

    private Point getLocationPoint() {
        return location;
    }

    private void setLocation(int x, int y) {
        location = new Point(x, y);
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bitmapApple, getLocationPoint().x * getSize(), getLocationPoint().y * getSize(), paint);
    }

    private Point getSpawnRange() {
        return spawnRange;
    }

    private void setSpawnRange(Point spawnRange) {
        this.spawnRange = spawnRange;
    }

    private int getSize() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
    }
}