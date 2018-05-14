package si.fri.ai.catan.map.parts;

public class Road {

    private int index;

    private Land from;
    private Land to;

    public Road(int index, Land from, Land to) {
        this.index = index;
        this.from = from;
        this.to = to;
    }

    public int getIndex() {
        return index;
    }

    public Land getFrom() {
        return from;
    }

    public Land getTo() {
        return to;
    }
}
