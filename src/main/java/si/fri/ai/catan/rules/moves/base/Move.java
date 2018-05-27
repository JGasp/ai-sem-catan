package si.fri.ai.catan.rules.moves.base;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public abstract class Move {

    protected Game game;
    protected int playerIndex;

    public Move(Game game, int playerIndex) {
        this.game = game;
        this.playerIndex = playerIndex;
    }

    public abstract void make(State state);

    protected void addTerrain(Terrain t, State state, int playerIndex) {
        state.addResourceIncome(playerIndex, t.getType(), t.getDice(), (byte) 1);
    }

    protected void subResource(State state, ResourceType type, int amount) {
       state.subResource(playerIndex, type, (byte) amount);
    }


}
