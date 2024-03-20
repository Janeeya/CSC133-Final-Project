package com.gamecodeschool.snakeapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

class SnakeGame extends SurfaceView implements Runnable{


    private Thread mThread = null;
    private long mNextFrameTime;
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;
    private int mScore;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Snake mSnake;
    private Apple mApple;
    private Typeface plain = Typeface.createFromAsset(getContext().getAssets(), "lobstertwo_regular.ttf");

    public SnakeGame(Context context, Point size) {
        super(context);

        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

        } catch (IOException e) {
        }

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPaint.setTypeface(plain);

        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

    }


    public void newGame() {

        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        mApple.spawn();

        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }


    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            draw();
        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        final long TARGET_FPS = 10;
        final long MILLIS_PER_SECOND = 1000;

        if(mNextFrameTime <= System.currentTimeMillis()){

            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            return true;
        }

        return false;
    }


    public void update() {

        mSnake.move();

        // Check if the snake ate the apple
        if(mSnake.checkDinner(mApple.getLocation())){
            mApple.spawn(2);

            mScore = mScore + 1;

            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        // Check if snake died
        if (mSnake.detectDeath()) {
            // Pause the game ready to start again
            mSP.play(mCrashID, 1, 1, 0, 0, 1);

            mPaused =true;
        }

    }


    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            Drawable background = getResources().getDrawable(R.drawable.background, null);
            background.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
            background.draw(mCanvas);

            drawScore();

            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);

            drawName();
            drawTapToPlay();
            drawPauseButton(mCanvas, mPaint);
            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawScore() {
        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(120);
        mCanvas.drawText("" + mScore, 20, 120, mPaint);
    }

    private void drawName() {
        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(120);
        mCanvas.drawText(getResources().
                        getString(R.string.player_names),
                1350, 150, mPaint);
    }

    private void drawTapToPlay() {
        if (mPaused) {
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(250);
            mCanvas.drawText(getResources().
                            getString(R.string.tap_to_play),
                    200, 700, mPaint);
        }
    }
    public void drawPauseButton(Canvas canvas, Paint paint) {
        int pauseButtonWidth = 350;
        int pauseButtonHeight = 140;
        int pauseButtonX = 500;
        int pauseButtonY = 50;

        // Draw button background
        paint.setColor(Color.argb(200, 0, 0, 0)); // Black with transparency
        canvas.drawRect(pauseButtonX, pauseButtonY,
                pauseButtonX + pauseButtonWidth, pauseButtonY + pauseButtonHeight, paint);

        // Draw the word "Pause" on the button
        paint.setColor(Color.argb(255, 255, 255, 255)); // White
        paint.setTextSize(120);
        canvas.drawText("Pause", pauseButtonX + 30, pauseButtonY + 110, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }

                // Let the Snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }


    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
        }
    }


    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }
}
