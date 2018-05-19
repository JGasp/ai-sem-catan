package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.rules.moves.base.Move;

public class BuildCity extends Move {

    private int villageIndex;

    public BuildCity(int villageIndex) {
        this.villageIndex = villageIndex;
    }

    public int getVillageIndex() {
        return villageIndex;
    }
}
