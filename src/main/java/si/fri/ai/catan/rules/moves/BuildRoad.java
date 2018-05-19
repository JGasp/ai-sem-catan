package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.rules.moves.base.Move;

public class BuildRoad extends Move {

    private byte roadIndex;

    public BuildRoad(byte roadIndex) {
        this.roadIndex = roadIndex;
    }

    public byte getRoadIndex() {
        return roadIndex;
    }
}
