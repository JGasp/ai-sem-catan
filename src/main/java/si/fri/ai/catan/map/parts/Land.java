package si.fri.ai.catan.map.parts;


import si.fri.ai.catan.map.parts.positon.Point;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class Land {

    private byte index;
    private Terrain[] terrains;
    private Road[] roads;

    private Point point;

    private int roadsCount = 0;

    private ResourceType trading = null;
    private boolean anyTrading = false;

    public Land(int index, int roads, Terrain... terrains) {
        this.index = (byte) index;
        this.roads = new Road[roads];
        this.terrains = terrains;

        for(Terrain t : terrains) {
            t.addLand(this);
        }
    }

    public byte getIndex() {
        return index;
    }

    public Terrain[] getTerrains() {
        return terrains;
    }

    public Road[] getRoads() {
        return roads;
    }

    public Road initRoad(int index, Land that) {
        Road road = new Road(index, this, that);

        this.roads[this.roadsCount++] = road;
        that.roads[that.roadsCount++] = road;

        return road;
    }

    public int getRoadsCount() {
        return roadsCount;
    }


    public void setAnyTrading(boolean anyTrading) {
        this.anyTrading = anyTrading;
    }

    public boolean isAnyTrading() {
        return anyTrading;
    }

    public ResourceType getTrading() {
        return trading;
    }

    public void setTrading(ResourceType trading) {
        this.trading = trading;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
