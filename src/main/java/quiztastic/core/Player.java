package quiztastic.core;

public class Player {

    private final String playerName;
    private int playerScore;
    private int counter = 1;

    public Player(String playerName, int playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
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
        return  "Player " + counter++ + ": " + playerName +
                "\nScore:" + playerScore + "\n-------------------------------";
    }
}
