package csc133.pong;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;


public class Screen {
    final int MILLIS_IN_SECOND = 1000;
    long mFPS;
    // Holds the resolution of the screen
    int x, y, mFontSize, mFontMargin;
    Paint mPaint, userPaint;
    Canvas mCanvas;


    public Screen(Point resolution) {
        x = resolution.x;
        y = resolution.y;
        // Font is 5% (1/20th) of screen width, and Margin is 1.5% (1/75th) of screen width
        mFontSize = x / 20;
        mFontMargin = x / 75;
        mPaint = new Paint();
    }

    public void printDetails(Profile profile, SurfaceHolder mOurHolder, Bat mBat, Ball mBall) {
        // Lock the canvas (graphics memory) ready to draw
        this.mCanvas = mOurHolder.lockCanvas();

        // Fill the screen with a solid color
        this.mCanvas.drawColor(Color.argb
                (255, 26, 128, 182));

        // Choose a color to paint with
        this.mPaint.setColor(Color.argb
                (255, 255, 255, 255));

        // Draw the bat and ball
        this.mCanvas.drawRect(mBall.getRect(), this.mPaint);
        this.mCanvas.drawRect(mBat.getRect(), this.mPaint);

        // Choose the font size
        this.mPaint.setTextSize(this.mFontSize);

        // Draw the HUD
        this.mCanvas.drawText("Score: " + profile.mScore +
                        "   Lives: " + profile.mLives,
                this.mFontMargin , this.mFontSize, this.mPaint);


            /* Creates a copy of the paint object that's used to create text (to steal the properties)
               then prints name to screen */
        this.userPaint = new Paint(this.mPaint);
        this.userPaint.setTextAlign(Paint.Align.RIGHT);
        this.mCanvas.drawText("Name: " + profile.userName,
                this.x-50 , this.mFontSize, this.userPaint);
    }

    public void printDebuggingText(){
        int debugSize = this.mFontSize / 2;
        int debugStart = 150;
        this.mPaint.setTextSize(debugSize);
        this.mCanvas.drawText("FPS: " + this.mFPS ,
                10, debugStart + debugSize, this.mPaint);
    }

    public void updateFPS(long frameStartTime) {
        // How long did this frame/loop take?
        // Store the answer in timeThisFrame
        long timeThisFrame = System.currentTimeMillis() - frameStartTime;

        // Make sure timeThisFrame is at least 1 millisecond
        // because accidentally dividing by zero crashes the game
        if (timeThisFrame > 0) {
            // Store the current frame rate in mFPS
            // ready to pass to the update methods of
            // mBat and mBall next frame/loop
            this.mFPS = this.MILLIS_IN_SECOND / timeThisFrame;
        }
    }

}
