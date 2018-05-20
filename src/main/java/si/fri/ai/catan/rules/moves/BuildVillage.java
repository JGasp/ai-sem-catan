package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;

public class BuildVillage extends Move {

    private byte landIndex;

    public BuildVillage(byte landIndex) {
        this.landIndex = landIndex;
    }

    @Override
    public void make(State state, int playerIndex) {
        state.buildVillages(playerIndex, landIndex);
    }
}
