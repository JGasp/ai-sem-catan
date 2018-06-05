package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Objects;

public class BuildRoad extends Move {

    private byte roadIndex;

    public BuildRoad(int playerIndex, byte roadIndex) {
        super(playerIndex);
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(Game game, State state) {

        state.subResource(playerIndex, ResourceType.CLAY, (byte) 1);
        state.subResource(playerIndex, ResourceType.WOOD, (byte) 1);

        state.buildRoad(playerIndex, roadIndex);
    }

    public byte getRoadIndex() {
        return roadIndex;
    }


    @Override
    public String toString() {
        return String.format("[%d] Build road on [%d]", playerIndex, roadIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BuildRoad.class.getSimpleName(), playerIndex, roadIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BuildRoad) {
            BuildRoad that = (BuildRoad) obj;
            return roadIndex == that.roadIndex && playerIndex == that.playerIndex;
        }
        return false;
    }
}
