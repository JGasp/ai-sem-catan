package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class BuildRoad extends Move {

    private byte roadIndex;

    public BuildRoad(Game game, int playerIndex, byte roadIndex) {
        super(game, playerIndex);
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(State state) {

        state.subResource(playerIndex, ResourceType.CLAY, (byte) 1);
        state.subResource(playerIndex, ResourceType.WOOD, (byte) 1);

        state.buildRoad(playerIndex, roadIndex);
    }
}
