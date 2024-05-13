package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class PowerUp extends GameObject implements IDrawable{
    private Bitmap bitmapPowerup;
    private Point location = new Point();
    private final Point spawnRange;
    private final int size;
    private static final int OFF_SCREEN_X = -10;
    private int powerUpChance = 5;

    PowerUp(Context context, Point spawnRange, int size){
        super(context, spawnRange, size);

        this.spawnRange = spawnRange;
        this.size = size;
        setLocation(new Point(OFF_SCREEN_X, 0));

        bitmapPowerup = BitmapFactory.decodeResource(context.getResources(), R.drawable.espresso);
        bitmapPowerup = Bitmap.createScaledBitmap(bitmapPowerup, size, size, false);
    }

    @Override
    public void spawn() {
        Random random = new Random();
        setLocation(new Point(random.nextInt(spawnRange.x) + 1, random.nextInt(spawnRange.y - 1) + 1));
    }

    public void spawn(int amount) {
        for (int i = 0; i < amount; i++) {
            spawn();
        }
    }

    Point getLocation() {
        return new Point(location.x, location.y);
    }

    private void setLocation(Point location) {
        this.location = location;
    }
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmapPowerup, location.x * size, location.y * size, paint);
    }

    public int getPowerUpChance() {
        return powerUpChance;
    }

    public void powerUpRemove(){
        setLocation(new Point(OFF_SCREEN_X, 0));
    }

    public void setPowerUpChance(int powerUpChance) {
        this.powerUpChance = powerUpChance;
    }
}
