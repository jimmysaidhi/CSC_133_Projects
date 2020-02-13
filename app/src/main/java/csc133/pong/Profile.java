package csc133.pong;

public class Profile {
    String userName;
    // The current score and lives remaining
    int mScore;
    int mLives;

    public Profile(String userName) {
        this.userName = userName;
        mScore = 0;
        mLives = 3;
    }

    public void incrementScore() {
        mScore++;
    }

    public void decrementLives() {
        mLives--;
    }

    public boolean hasZeroLives() {
        return mLives == 0;
    }
}
