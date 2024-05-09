package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Apple extends GameObject implements IDrawable {

    private Point location = new Point();

    private final Point spawnRange;
    private final int size;

    // Apple image
    private Bitmap bitmapApple;

    private static final int OFF_SCREEN_X = -10;


    Apple(Context context, Point spawnRange, int size) {
        super(context, spawnRange, size);

        this.spawnRange = spawnRange;
        this.size = size;
        setLocation(new Point(OFF_SCREEN_X, 0));

        bitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.croissant);
        bitmapApple = Bitmap.createScaledBitmap(bitmapApple, size, size, false);
    }

    //new apple at random location

    @Override
    public void spawn() {
        Random random = new Random();
        setLocation(new Point(random.nextInt(spawnRange.x) + 1, random.nextInt(spawnRange.y - 1) + 1));
    }

    //Spawns multiple apples at random locations.
    public void spawn(int amount) {
        for (int i = 0; i < amount; i++) {
            spawn();
        }
    }

    //Returns location of the apple
    Point getLocation() {
        return new Point(location.x, location.y);
    }

    //Setting location of the apple

    private void setLocation(Point location) {
        this.location = location;
    }

    // Draws the apple on the canvas.

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmapApple, location.x * size, location.y * size, paint);
    }
}