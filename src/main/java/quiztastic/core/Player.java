package quiztastic.core;

import quiztastic.ui.Protocol;

import java.io.IOException;

public class Player {

    private final String playerName;
    private int playerScore;
    private int counter;

    //Her har vi vores player element, som vil blive tilføget når en person kommer ind på serveren og skriver deres navn.
    public Player(int counter, String playerName, int playerScore) {
        this.counter = counter;
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public String toString() {
        return  "Player " + counter + ": " + playerName +
                " | Score: " + playerScore;
    }
}
