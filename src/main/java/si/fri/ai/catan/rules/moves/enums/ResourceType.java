package si.fri.ai.catan.rules.moves.enums;

public enum ResourceType {
    WHEAT(0),
    IRON(1),
    WOOD(2),
    SHEEP(3),
    CLAY(4);

    private int index;

    ResourceType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static ResourceType getRt(int value) {
        switch (value) {
            case 0: return WHEAT;
            case 1: return IRON;
            case 2: return WOOD;
            case 3: return SHEEP;
            case 4: return CLAY;
            default: return null;
        }
    }
}
