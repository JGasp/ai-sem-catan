package si.fri.ai.catan.players.monteCarlo;

public class MCScore {

    private static float C = 1.4f;

    private int plays = 0;
    private int wins = 0;

    private MCNode node;

    public MCScore(MCNode node) {
        this.node = node;
    }

    public MCNode getNode() {
        return node;
    }

    public double getScore(int totalPlays) {
        return getWinRatio() + C * Math.sqrt((double) totalPlays / this.plays);
    }

    public double getWinRatio(){
        return (double) wins / plays;
    }

    public void incPlays(){
        plays++;
    }

    public void incWins(){
        wins++;
    }

    public int getPlays() {
        return plays;
    }

    public int getWins() {
        return wins;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MCScore) {
            MCScore that = (MCScore) obj;
            return node.equals(that.node);
        } else {
            return false;
        }
    }

}
