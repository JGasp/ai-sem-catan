package si.fri.ai.catan.rules.moves.enums;

public enum ResourceType {
    WHEAT(0),
    IRON(1),
    WOOD(2),
    SHEEP(3),
    CLAY(4),
    NOTHING(5);

    int index;
    ResourceType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
