package csc133.pong;
import android.graphics.Point;


public class Display {
    // Holds the resolution of the screen
    int mScreenX;
    int mScreenY;


    public Display(Point p) {
        this.mScreenX = p.x;
        this.mScreenY = p.y;

    }

}
