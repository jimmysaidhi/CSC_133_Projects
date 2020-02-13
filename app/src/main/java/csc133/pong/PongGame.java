package csc133.pong;

import android.graphics.Point;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PongGame extends SurfaceView implements Runnable{

    // Are we debugging?
    private final boolean DEBUGGING = true;
    private static String USERNAME = "Jimmy Le";
    // These objects are needed to do the drawing
    private SurfaceHolder mOurHolder;

    // The game objects
    private Bat mBat;
    private Ball mBall;

    // SoundPool to control Sound
    private Sound gameSound;
    private Profile profile;
    private Screen screen;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;
    // This volatile variable can be accessed
    // from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    public PongGame(Context context, Point resolution) {
        super(context);

        // initializes sound based, connecting it to this context and starts the sound
        this.gameSound = new Sound(context);
        this.profile = new Profile(USERNAME);
        this.screen = new Screen(resolution);

        // Initialize the objects ready for drawing with getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        screen.mPaint = new Paint();

        // Initialize the bat and ball
        mBall = new Ball(screen.x);
        mBat = new Bat(screen.x, screen.y);

        // Everything is ready so start the game
        startNewGame();
    }

    // The player has just lost or is starting their first game
    private void startNewGame(){
        // Put the ball back to the starting position
        mBall.reset(screen);
        profile = new Profile(USERNAME);
    }

    @Override
    public void run() {
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
                screen.mFPS = screen.MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    private void update() {
        // Update the bat and the ball
        mBall.update(screen.mFPS);
        mBat.update(screen.mFPS);
    }

    private void detectCollisions(){
        // Has the bat hit the ball?
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            // Realistic-ish bounce
            mBall.batBounce(mBat.getRect());
            mBall.increaseVelocity();
            profile.incrementScore();
            gameSound.collisionSounds("BAT");
        }
        // Has the ball hit the edge of the screen
        // Bottom
        if(mBall.getRect().bottom > screen.y) {
            mBall.reverseYVelocity();
            profile.decrementLives();
            gameSound.collisionSounds("BOTTOM");

            if(profile.hasZeroLives()){
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
        if(mBall.getRect().right > screen.x){
            mBall.reverseXVelocity();
            gameSound.collisionSounds("RIGHT");
        }
    }

    // Draw the game objects and the HUD
    void draw() {
        if (mOurHolder.getSurface().isValid()) {
            screen.printDetails(this.profile, mOurHolder, this.mBat, this.mBall);

            if(DEBUGGING){
                screen.printDebuggingText();
            }
            // Display the drawing on screen
            // unlockCanvasAndPost is a method of SurfaceView
            mOurHolder.unlockCanvasAndPost(screen.mCanvas);
        }
    }

    // Handle all the screen touches
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // This switch block replaces the
        // if statement from the Sub Hunter game
        switch (motionEvent.getAction() &
                MotionEvent.ACTION_MASK) {

            // The player has put their finger on the screen
            case MotionEvent.ACTION_DOWN:

                // If the game was paused un-pause
                mPaused = false;

                // Where did the touch happen
                if(motionEvent.getX() > screen.x / 2){
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


    public void pause() {
        mPlaying = false;
        try {
            // Stop the thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);

        // Start the thread
        mGameThread.start();
    }
}