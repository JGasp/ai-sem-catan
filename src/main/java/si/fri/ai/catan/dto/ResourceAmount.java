package si.fri.ai.catan.dto;

import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class ResourceAmount {

    private ResourceType type;
    private int amount;

    public ResourceAmount(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void decAmount() {
        this.amount--;
    }
}
