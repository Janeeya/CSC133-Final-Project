package com.gamecodeschool.snakeapplication;

import android.app.Activity;
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
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



class SnakeGame extends SurfaceView implements Runnable{

    private Thread mThread = null;
    //private int mFPS = 10;
    private long mNextFrameTime;
    private IAudioStrategy soundEffectStrategy;
    private IAudioStrategy backgroundMusicStrategy;
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Snake mSnake;
    private Apple mApple;
    private PowerUp mPowerUp;
    private Typeface plain = Typeface.createFromAsset(getContext().getAssets(), "lobstertwo_regular.ttf");
    private Typeface mTypeface;
    private GameState gameState;
    private static final String TAG = "SnakeGame";

    public SnakeGame(Context context, Point size) {
        super(context);
        gameState = new GameState(context);

        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mTypeface = Typeface.createFromAsset(context.getAssets(), "lobstertwo_regular.ttf");
        //gameState.demoFix(); //uncomment this for demo
        showNameInput(context);
        // Initialize the SoundPool


        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPaint.setTypeface(plain);

        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE-1,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mPowerUp = new PowerUp(context,
                new Point(NUM_BLOCKS_WIDE-1,
                        mNumBlocksHigh),
                blockSize);

        soundEffectStrategy = new SoundEffectStrategy(context);
        backgroundMusicStrategy = new BackgroundMusicStrategy(context);

        backgroundMusicStrategy.apply(GameEventType.START);
    }

    public void newGame() {
        gameState.newGame();
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mApple.spawn();
        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
        resume();
    }

    private void showNameInput(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Your Name");

        final EditText input = new EditText(context);

        input.setTextSize(18);
        input.setBackgroundColor(Color.LTGRAY);
        input.setTypeface(mTypeface);

        builder.setView(input);

        // Set up the "OK" button to handle input
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playerName = input.getText().toString().trim();
                if (!playerName.isEmpty()) {
                    gameState.setPlayerName(playerName);
                    drawTapToPlay(); // Start the game logic
                } else {
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Prevent dismiss by tapping outside of the dialog
        builder.setCancelable(false);

        // Create and show the dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Override the "OK" button click listener to prevent dismissal without valid input
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture the input and handle it manually
                String playerName = input.getText().toString().trim();
                if (!playerName.isEmpty()) {
                    gameState.setPlayerName(playerName);
                    dialog.dismiss();
                    drawTapToPlay();
                } else {
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void run() {
        //Log.d(TAG, "Game loop started");
        while (gameState.isPlaying()) {
            if(!gameState.isPaused()) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }
            draw();

        }
        //Log.d(TAG, "Game loop ended");
    }

    // Check to see if it is time for an update
    public boolean updateRequired() {

        //final long TARGET_FPS = 10;
        final long MILLIS_PER_SECOND = 1000;

        if(mNextFrameTime <= System.currentTimeMillis()){

            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / gameState.getmFPS();

            return true;
        }
        return false;
    }

    // Inside the update() method
    public void update() {
        //Log.d(TAG, "Updating game state");
        mSnake.move();

        // Check if the snake ate the apple
        if (mSnake.checkDinner(mApple.getLocation())) {
            mApple.spawn(2);
            gameState.increaseScore();
            if (new Random().nextInt(mPowerUp.getPowerUpChance()) == 0) {
                mPowerUp.spawn(); //spawns power up randomly about 20% of the time
                System.out.println("Powerup is spawning at coordinates (" + mPowerUp.getLocation().x + ", " + mPowerUp.getLocation().y + ")");
            }
            soundEffectStrategy.apply(GameEventType.EAT);
        }

        if (mSnake.checkDinner(mPowerUp.getLocation())) {
            //snake ate powerUp, so slow time for X frames
            gameState.slowmo();
            mPowerUp.powerUpRemove();
            soundEffectStrategy.apply(GameEventType.POWERUP);
        }

        // Check if snake died
        if (mSnake.detectDeath()) {
            soundEffectStrategy.apply(GameEventType.CRASH);
            gameState.pause();

            drawGameOver();
        }
    }
    public void draw() {
        //Log.d(TAG, "Drawing game state");
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK);

            Drawable background = getResources().getDrawable(R.drawable.cafe, null);
            background.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
            background.draw(mCanvas);

            drawScore();
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            mPowerUp.draw(mCanvas, mPaint);

            // Draw player's name at the top right corner
            mPaint.setColor(Color.BLUE);
            mPaint.setTextSize(80);
            mPaint.setTypeface(mTypeface);
            mCanvas.drawText(gameState.getPlayerName(), mCanvas.getWidth() - 300, 100, mPaint);

            drawTapToPlay();
            drawPauseButton(mCanvas, mPaint);

            drawHighScore();
            drawFPS();
            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
    private void drawGameOver() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        TextView title = new TextView(getContext());
        title.setText("Game Over");
        title.setTextColor(Color.RED); // Set color to red
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50); // Set larger font size
        title.setTypeface(null, Typeface.BOLD); // Set bold style

        builder.setCustomTitle(title);


        boolean isNewHighScore = gameState.isHighScore();
        int score = gameState.getScore();
        String scoreMessage;
        if (isNewHighScore) {

            gameState.setHighScore();
            scoreMessage = "New High Score: " + score;
        } else {
            scoreMessage = "Score: " + score;
        }

        SpannableString spannableMessage = new SpannableString(scoreMessage);

        int scoreIndex = scoreMessage.indexOf("Score");
        int newHighScoreIndex = scoreMessage.indexOf("New High Score");

        if (scoreIndex != -1) {
            int fontSize = 75;
            spannableMessage.setSpan(new AbsoluteSizeSpan(fontSize), scoreIndex, scoreIndex + "Score".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMessage.setSpan(new ForegroundColorSpan(Color.WHITE), scoreIndex, scoreIndex + "Score".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (newHighScoreIndex != -1) {
            int fontSize = 75;
            spannableMessage.setSpan(new AbsoluteSizeSpan(fontSize), newHighScoreIndex, newHighScoreIndex + "New High Score".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMessage.setSpan(new ForegroundColorSpan(Color.WHITE), newHighScoreIndex, newHighScoreIndex + "New High Score".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        // Style score value
        int scoreValueIndex = scoreMessage.indexOf(String.valueOf(score));
        if (scoreValueIndex != -1) {
            int fontSize = 75;
            spannableMessage.setSpan(new AbsoluteSizeSpan(fontSize), scoreValueIndex, scoreValueIndex + String.valueOf(score).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        builder.setMessage(spannableMessage);


        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetGame();
                newGame(); // Continue the game
            }
        });

        builder.setNegativeButton("Exit Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) getContext()).finish();
            }
        });
        builder.setCancelable(false);

  
        backgroundMusicStrategy.apply(GameEventType.STOP);

 

        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });
    }

    private void drawScore() {
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(120);
        mCanvas.drawText("" + gameState.getScore(), 20, 120, mPaint);
    }
    private void drawTapToPlay() {
        if (gameState.isPaused()) {
            mPaint.setColor(Color.BLUE);
            mPaint.setTextSize(220);
            mCanvas.drawText(getResources().
                            getString(R.string.tap_to_play),
                    600, 900, mPaint);
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
    private void drawHighScore() {
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(80);

        // Set a thicker stroke width for the paint
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);

        mCanvas.drawText("High Score - " + gameState.getHighScoreName() +": "+gameState.getHighScore(), 20, 260, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
    }


    public void resetGame() {
        // Reset the snake to its initial position
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mApple.spawn();
        gameState.resetGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        int left = 500;
        int right = 850;
        int top = 75;
        int bottom = 200;

        double xTouchPosition = motionEvent.getAxisValue(0);
        double yTouchPosition = motionEvent.getAxisValue(1);

        if (xTouchPosition >= left && xTouchPosition <= right &&
                yTouchPosition >= top && yTouchPosition <= bottom &&
                motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (gameState.isPlaying()) {
                pause();
            } else {
                resume();
            }
            return true;
        }

        if (!gameState.isPlaying()) {
            return true;
        }

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (gameState.isPaused()) {
                    gameState.resume();
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
        gameState.setPlaying(false);
        try {
            mThread.join();
        } catch (InterruptedException e) {
        }
        backgroundMusicStrategy.apply(GameEventType.PAUSE);

    }


    public void resume() {
        gameState.setPlaying(true);
        mThread = new Thread(this);
        mThread.start();
        backgroundMusicStrategy.apply(GameEventType.RESUME);
    }

    public void release() {
        // Release audio resources
        soundEffectStrategy.release();
        backgroundMusicStrategy.release();
    }

    private void drawFPS() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        String fpsText = "FPS: " + gameState.getmFPS();
        mCanvas.drawText(fpsText, mCanvas.getWidth() - 150, mCanvas.getHeight() - 20, paint);
    }
}