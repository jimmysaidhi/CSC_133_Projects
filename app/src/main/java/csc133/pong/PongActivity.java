package csc133.pong;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class PongActivity extends Activity {
    private PongGame mPongGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        // Gets properties of the device and then initializes the pong game
        Point screen = new Point();
        display.getSize(screen);

        mPongGame = new PongGame(this, screen);
        setContentView(mPongGame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPongGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPongGame.pause();
    }
}