package csc133.pong;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Screen {
    // Holds the resolution of the screen
    int x;
    int y;
    Canvas mCanvas;
    Paint mPaint;
    Paint userPaint;
    // How many frames per second did we get?
    long mFPS;
    // The number of milliseconds in a second
    final int MILLIS_IN_SECOND = 1000;


    // How big will the text be?
    int mFontSize;
    int mFontMargin;


    public Screen(Point resolution) {
        x = resolution.x;
        y = resolution.y;
        // Font is 5% (1/20th) of screen width
        mFontSize = x / 20;
        // Margin is 1.5% (1/75th) of screen width
        mFontMargin = x / 75;
        mPaint = new Paint();

    }

    public void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS ,
                10, debugStart + debugSize, mPaint);
    }

}
