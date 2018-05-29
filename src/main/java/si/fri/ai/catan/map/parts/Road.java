package si.fri.ai.catan.map.parts;

import si.fri.ai.catan.map.parts.positon.Point;

public class Road {

    private byte index;

    private Land from;
    private Land to;

    private Point point;

    public Road(int index, Land from, Land to) {
        this.index = (byte) index;
        this.from = from;
        this.to = to;
    }

    public byte getIndex() {
        return index;
    }

    public Land getFrom() {
        return from;
    }

    public Land getTo() {
        return to;
    }

    public Point getPoint() {
        return point;
    }

    public Land getOther(Land land) {
        if(from == land) {
            return to;
        } else {
            return from;
        }
    }

    public void calculatePoint() {
        point = new Point(
                (from.getPoint().getX() + to.getPoint().getX()) / 2,
                (from.getPoint().getY() + to.getPoint().getY()) / 2
        );
    }
}
