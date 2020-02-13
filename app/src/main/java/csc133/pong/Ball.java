package csc133.pong;
import android.graphics.RectF;

class Ball {

    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;


    Ball(int screenX){

        mBallWidth = screenX / 100;
        mBallHeight = screenX / 100;

        mRect = new RectF();
    }


    // Return a reference to mRect to PongGame
    RectF getRect(){
        return mRect;
    }

    // Update the ball position.
    // Called each frame/loop
    void update(long fps){
        // Move the ball based upon the
        // horizontal (mXVelocity) and
        // vertical(mYVelocity) speed
        // and the current frame rate(mFPS)

        // Move the top left corner
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);

        // Match up the bottom right corner
        // based on the size of the ball
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;
    }

    // Reverse the vertical direction of travel
    void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }

    // Reverse the horizontal direction of travel
    void reverseXVelocity(){
        mXVelocity = -mXVelocity;
    }

    void reset(Screen screen){

        // Initialise the four points of
        // the rectangle which defines the ball
        mRect.left = screen.x / 2;
        mRect.top = 0;
        mRect.right = screen.x / 2 + mBallWidth;
        mRect.bottom = mBallHeight;

        // How fast will the ball travel
        // You could vary this to suit
        // You could even increase it as the game progresses
        // to make it harder
        mYVelocity = -(screen.y / 3);
        mXVelocity = (screen.y / 3);
    }

    void increaseVelocity(){
        // increase the speed by 10%
        mXVelocity = mXVelocity * 1.1f;
        mYVelocity = mYVelocity * 1.1f;
    }


    // Bounce the ball back based upon
    // whether it hits the left or right hand side
    void batBounce(RectF batPosition){

        // Detect center of bat
        float batCenter = batPosition.left +
                (batPosition.width() / 2);

        // detect the center of the ball
        float ballCenter = mRect.left +
                (mBallWidth / 2);

        // Where on the bat did the ball hit?
        float relativeIntersect = (batCenter - ballCenter);

        // Pick a bounce direction
        if(relativeIntersect < 0){
            // Go right
            mXVelocity = Math.abs(mXVelocity);
            // Math.abs is a static method that
            // strips any negative values from a value.
            // So -1 becomes 1 and 1 stays as 1
        }else{
            // Go left
            mXVelocity = -Math.abs(mXVelocity);
        }
        reverseYVelocity();
    }

    public boolean hitsBottom(Screen screen) {
        return this.getRect().bottom > screen.y;
    }

    public boolean hitsRight(Screen screen) {
        return this.getRect().right > screen.x;
    }

    public boolean hitsLeft() {
        return this.getRect().left < 0;
    }

    public boolean hitsTop() {
        return this.getRect().top < 0;
    }
}
