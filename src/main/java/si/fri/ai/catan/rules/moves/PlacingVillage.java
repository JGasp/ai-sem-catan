package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;

public class PlacingVillage extends Move {

    private byte landIndex;
    private byte roadIndex;

    public PlacingVillage(Game game, int playerIndex, byte landIndex, byte roadIndex) {
        super(game, playerIndex);
        this.landIndex = landIndex;
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(State state) {

    }
}
