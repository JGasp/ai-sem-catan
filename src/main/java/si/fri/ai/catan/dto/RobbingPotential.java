package si.fri.ai.catan.dto;

public class RobbingPotential {

    private int playerIndex;
    private int score;

    public RobbingPotential(int playerIndex, int score) {
        this.playerIndex = playerIndex;
        this.score = score;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getScore() {
        return score;
    }
}
