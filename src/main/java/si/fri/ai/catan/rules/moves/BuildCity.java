package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;

public class BuildCity extends Move {

    private byte villageIndex;

    public BuildCity(byte villageIndex) {
        this.villageIndex = villageIndex;
    }

    @Override
    public void make(State state, int playerIndex) {
        state.buildCity(playerIndex, villageIndex);
    }
}
