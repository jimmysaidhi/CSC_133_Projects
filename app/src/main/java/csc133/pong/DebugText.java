package csc133.pong;

public class DebugText {
    PongGame pongGame;
    private long mFPS;

    public DebugText(PongGame pongGame) {
        this.pongGame = pongGame;
        printDebuggingText();
    }

    public void printDebuggingText(){
        int debugSize = pongGame.screenText.mFontSize / 2;
        int debugStart = 150;
        pongGame.screenText.mPaint.setTextSize(debugSize);
        pongGame.screenText.mCanvas.drawText("FPS: " + mFPS ,
                10, debugStart + debugSize, pongGame.screenText.mPaint);

    }

    public void setmFPS(long mFPS) {
        this.mFPS = mFPS;
    }

    public long getmFPS() {
        return this.mFPS;
    }
}
