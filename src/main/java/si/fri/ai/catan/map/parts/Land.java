package si.fri.ai.catan.map.parts;


import si.fri.ai.catan.map.parts.positon.Point;

public class Land {

    private byte index;
    private Terrain[] terrains;
    private Road[] roads;

    private Point point;

    private int roadsCount = 0;

    private boolean clayTrading = false;
    private boolean woodTrading = false;
    private boolean wheatTrading = false;
    private boolean ironTrading = false;
    private boolean sheepTrading = false;
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

    public void setClayTrading(boolean clayTrading) {
        this.clayTrading = clayTrading;
    }

    public void setWoodTrading(boolean woodTrading) {
        this.woodTrading = woodTrading;
    }

    public void setWheatTrading(boolean wheatTrading) {
        this.wheatTrading = wheatTrading;
    }

    public void setIronTrading(boolean ironTrading) {
        this.ironTrading = ironTrading;
    }

    public void setSheepTrading(boolean sheepTrading) {
        this.sheepTrading = sheepTrading;
    }

    public void setAnyTrading(boolean anyTrading) {
        this.anyTrading = anyTrading;
    }

    public int getRoadsCount() {
        return roadsCount;
    }

    public boolean isClayTrading() {
        return clayTrading;
    }

    public boolean isWoodTrading() {
        return woodTrading;
    }

    public boolean isWheatTrading() {
        return wheatTrading;
    }

    public boolean isIronTrading() {
        return ironTrading;
    }

    public boolean isSheepTrading() {
        return sheepTrading;
    }

    public boolean isAnyTrading() {
        return anyTrading;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
