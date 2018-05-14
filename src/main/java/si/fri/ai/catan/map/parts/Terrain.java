package si.fri.ai.catan.map.parts;

import si.fri.ai.catan.map.parts.enums.TerrainType;

public class Terrain {

    private int index;
    private int dice;

    private TerrainType type;

    public Terrain(int index, int dice, TerrainType type) {
        this.index = index;
        this.dice = dice;
        this.type = type;
    }
}
