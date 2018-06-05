package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.Objects;

public class PlacingRoad extends Move {

    private byte roadIndex;

    public PlacingRoad(int playerIndex, byte roadIndex) {
        super(playerIndex);
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(Game game, State state) {

        state.buildRoad(playerIndex, roadIndex);

    }

    @Override
    public String toString() {
        return String.format("[%d] Placed road on [%d]", playerIndex, roadIndex);
    }

    public byte getRoadIndex() {
        return roadIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PlacingRoad.class.getSimpleName(), playerIndex, roadIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlacingRoad) {
            PlacingRoad that = (PlacingRoad) obj;
            return roadIndex == that.roadIndex && playerIndex == that.playerIndex;
        }
        return false;
    }

}
