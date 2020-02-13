package csc133.pong;
import android.graphics.Point;

public class Screen {
    // Holds the resolution of the screen
    int x;
    int y;

    public Screen(Point resolution) {
        x = resolution.x;
        y = resolution.y;
    }

}
