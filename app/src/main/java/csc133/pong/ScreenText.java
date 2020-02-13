package csc133.pong;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ScreenText {
    public Paint mPaint;
    private Paint userPaint;
    private Display display;
    private User user;
    int mFontSize;
    int mFontMargin;
    Canvas mCanvas;

    public ScreenText(Display display, User user) {
        this.display = display;
        this.user = user;
        mFontSize = display.mScreenX / 20;
        // Margin is 1.5% (1/75th) of screen width
        mFontMargin = display.mScreenX / 75;
        mPaint = new Paint();

       // this.printScreen();
    }

    public void printScreen(SurfaceHolder mOurHolder) {

        mCanvas = mOurHolder.lockCanvas();
        mCanvas.drawColor(Color.argb(255,26,128,182));

        // Choose the font size
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(Color.argb(255,255,255,255));


        // Draw the HUD
        mCanvas.drawText("Score: " + user.mScore +
                        "   Lives: " + user.mLives,
                this.mFontMargin, this.mFontSize, mPaint);


        /* Creates a copy of the paint object that's used to create text (to steal the properties)
           then prints name to screen */
        userPaint = new Paint(mPaint);
        userPaint.setTextAlign(Paint.Align.RIGHT);
        mCanvas.drawText("Name: " + user.userName,
                this.display.mScreenX - 50, mFontSize, userPaint);

    }
}
