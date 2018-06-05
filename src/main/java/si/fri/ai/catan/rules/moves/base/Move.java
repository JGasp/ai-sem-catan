package si.fri.ai.catan.rules.moves.base;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Terrain;

public abstract class Move {

    protected int playerIndex;

    public Move(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public abstract void make(Game game, State state);

    protected void addTerrain(Terrain t, State state, int playerIndex) {
        if(t.getType() != null && t.getIndex() != state.getThiefTerrain()) {
            state.addResourceIncome(playerIndex, t.getType(), t.getDice(), (byte) 1);
        }
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
