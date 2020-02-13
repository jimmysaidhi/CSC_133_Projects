package csc133.pong;

import android.graphics.Color;
import android.graphics.Point;
import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PongGame extends SurfaceView implements Runnable{

    private Display display;
    private DebugText debugText;
    private SurfaceHolder mOurHolder;
    // SoundPool to control Sound
    private Sound gameSound;
    private User user;

    // The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;
    protected ScreenText screenText;

    // The game objects
    private Bat mBat;
    private Ball mBall;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;
    // This volatile variable can be accessed
    // from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    public PongGame(Context context, Point screenCoordinates) {
        super(context);

        // initializes sound based, connecting it to this context and starts the sound
        this.gameSound = new Sound(context);
        this.display = new Display(screenCoordinates);
        this.user = new User();
        this.screenText = new ScreenText(this.display, this.user);
        this.debugText = new DebugText(this);

        // Initialize the objects ready for drawing with getHolder is a method of SurfaceView
        mOurHolder = getHolder();

        // Bat and Ball for the game
        mBall = new Ball(this.display.mScreenX);
        mBat = new Bat(this.display.mScreenX, this.display.mScreenY);

        // Everything is ready so start the game
        startNewGame();
    }

   // New Game or Restarted after losing all lives
    private void startNewGame(){
        // Put the ball back to the starting position
        mBall.reset(this.display.mScreenX, this.display.mScreenY);

        // Rest the score and the player's chances
        this.user = new User();
    }

    @Override
    public void run() {
        // mPlaying gives us finer control
        // rather than just relying on the calls to run
        // mPlaying must be true AND
        // the thread running for the main loop to execute
        while (mPlaying) {

            // What time is it now at the start of the loop?
            long frameStartTime = System.currentTimeMillis();

            // Provided the game isn't paused call the update method
            if(!mPaused){
                update();
                // Now the bat and ball are in their new positions
                // we can see if there have been any collisions
                detectCollisions();
            }

            // The movement has been handled and collisions
            // detected now we can draw the scene.
            draw();

            // How long did this frame/loop take?
            // Store the answer in timeThisFrame
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;

            // Make sure timeThisFrame is at least 1 millisecond
            // because accidentally dividing by zero crashes the game
            if (timeThisFrame > 0) {
                // Store the current frame rate in mFPS
                // ready to pass to the update methods of
                // mBat and mBall next frame/loop
                debugText.setmFPS(MILLIS_IN_SECOND / timeThisFrame);
            }

        }
    }

    private void update() {
        // Update the bat and the ball
        mBall.update(debugText.getmFPS());
        mBat.update(debugText.getmFPS());
    }

    // Complete
    private void detectCollisions(){
        // Has the bat hit the ball?
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            // Realistic-ish bounce
            mBall.batBounce(mBat.getRect());
            mBall.increaseVelocity();
            user.incrementScore();
            gameSound.collisionSounds("BAT");
        }

        // Bottom
        if(mBall.getRect().bottom > this.display.mScreenY){
            mBall.reverseYVelocity();

            user.decrementLives();
            gameSound.collisionSounds("BOTTOM");

            if(user.hasZeroLives()){
                mPaused = true;
                startNewGame();
            }
        }

        // Top
        if(mBall.getRect().top < 0){
            mBall.reverseYVelocity();
            gameSound.collisionSounds("TOP");

        }

        // Left
        if(mBall.getRect().left < 0){
            mBall.reverseXVelocity();
            gameSound.collisionSounds("LEFT");
        }

        // Right
        if(mBall.getRect().right > this.display.mScreenX){
            mBall.reverseXVelocity();
            gameSound.collisionSounds("RIGHT");
        }
    }

    // Draw the game objects and the HUD
    void draw() {
        if (mOurHolder.getSurface().isValid()) {
        /*    // Lock the canvas (graphics memory) ready to draw
            this.screenText.mCanvas = mOurHolder.lockCanvas();

            // Fill the screen with a solid color
            this.screenText.mCanvas.drawColor(Color.argb
                    (255, 26, 128, 182));

            // Choose a color to paint with
            this.screenText.mPaint.setColor(Color.argb
                    (255, 255, 255, 255));*/

            screenText.printScreen(mOurHolder);

            // Draw the bat and ball
            this.screenText.mCanvas.drawRect(mBall.getRect(), this.screenText.mPaint);
            this.screenText.mCanvas.drawRect(mBat.getRect(), this.screenText.mPaint);

            debugText.printDebuggingText();

            // Display the drawing on screen
            // unlockCanvasAndPost is a method of SurfaceView
            mOurHolder.unlockCanvasAndPost(this.screenText.mCanvas);
        }

    }

    // Handle all the screen touches
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() &
                MotionEvent.ACTION_MASK) {

            // The player has put their finger on the screen
            case MotionEvent.ACTION_DOWN:

                // If the game was paused un-pause
                mPaused = false;

                // Where did the touch happen
                if(motionEvent.getX() > this.display.mScreenX / 2){
                    // On the right hand side
                    mBat.setMovementState(mBat.RIGHT);
                }
                else{
                    // On the left hand side
                    mBat.setMovementState(mBat.LEFT);
                }

                break;

            case MotionEvent.ACTION_UP:

                // Stop the bat moving
                mBat.setMovementState(mBat.STOPPED);
                break;
        }
        return true;
    }



// Do not touch
    // This method is called by PongActivity when player quits game
    public void pause() {

        // Set mPlaying to false
        // Stopping the thread isn't
        // always instant
        mPlaying = false;
        try {
            // Stop the thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // This method is called by PongActivity when the player starts the game
    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);

        // Start the thread
        mGameThread.start();
    }
}