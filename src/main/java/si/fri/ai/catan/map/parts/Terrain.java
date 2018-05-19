package si.fri.ai.catan.map.parts;

import si.fri.ai.catan.map.parts.enums.TerrainType;
import si.fri.ai.catan.map.parts.positon.Point;

public class Terrain {

    private int index;
    private int dice;

    private TerrainType type;

    private Point point;

    public Terrain(int index, int dice, TerrainType type) {
        this.index = index;
        this.dice = dice;
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getIndex() {
        return index;
    }

    public int getDice() {
        return dice;
    }

    public TerrainType getType() {
        return type;
    }

}
