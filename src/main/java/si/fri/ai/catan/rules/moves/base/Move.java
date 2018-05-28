package si.fri.ai.catan.rules.moves.base;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Terrain;

public abstract class Move {

    protected Game game;
    protected int playerIndex;

    public Move(Game game, int playerIndex) {
        this.game = game;
        this.playerIndex = playerIndex;
    }

    public abstract void make(State state);

    protected void addTerrain(Terrain t, State state, int playerIndex) {
        if(t.getType() != null && t.getIndex() != state.getThiefTerrain()) {
            state.addResourceIncome(playerIndex, t.getType(), t.getDice(), (byte) 1);
        }
    }

}
