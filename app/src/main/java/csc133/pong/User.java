package csc133.pong;

public class User {
    String userName = "Jimmy Le";
    // The current score and lives remaining
    int mScore;
    int mLives;


    public User() {
        mScore = 0;
        mLives = 3;
    }

    public void incrementScore() {
        this.mScore++;
    }

    public void decrementLives() {
        mLives--;
    }

    public boolean hasZeroLives() {
        return mLives == 0;
    }
}
