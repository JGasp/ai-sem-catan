package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;

public class BuildRoad extends Move {

    private byte roadIndex;

    public BuildRoad(Game game, int playerIndex, byte roadIndex) {
        super(game, playerIndex);
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(State state) {

        state.subClay(playerIndex, 2);
        state.subWood(playerIndex, 2);

        state.buildRoad(playerIndex, roadIndex);
    }
}
