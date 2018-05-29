package si.fri.ai.catan.map.parts;

import si.fri.ai.catan.map.parts.positon.Point;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class Terrain {

    private byte index;
    private int dice;

    private ResourceType type;

    private int landIndex = 0;
    private Land[] lands;

    private Point point;

    public Terrain(int index, int dice, ResourceType type) {
        this.index = (byte) index;
        this.dice = dice;
        this.type = type;
        this.lands = new Land[6];
    }

    public void addLand(Land l) {
        this.lands[landIndex] = l;
        landIndex++;
    }

    public Land[] getLands() {
        return lands;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public byte getIndex() {
        return index;
    }

    public int getDice() {
        return dice;
    }

    public ResourceType getType() {
        return type;
    }

}
