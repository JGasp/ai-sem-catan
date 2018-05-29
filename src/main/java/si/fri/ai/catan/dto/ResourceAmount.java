package si.fri.ai.catan.dto;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Comparator;

public class ResourceAmount {

    private ResourceType type;
    private int amount;
    private float avgInc;

    private boolean isTrading = false;


    public ResourceAmount(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ResourceAmount(ResourceType type, int amount, float avgInc) {
        this.type = type;
        this.amount = amount;
        this.avgInc = avgInc;
    }

    public ResourceAmount(ResourceType type, int amount, State state, int playerIndex) {
        this.type = type;
        this.amount = amount;
        this.avgInc = state.getResourceAvgIncome(playerIndex, type);
        this.isTrading = state.isResourceTrading(playerIndex, type);
    }

    /**
     * Highest inc / amount first
     * @return
     */
    public static Comparator<ResourceAmount> compIncAmount() {
        return (o1, o2) -> {
            if(o1.getAvgInc() == o2.getAvgInc()) {
                return o2.getAmount() - o1.getAmount();
            } else {
                float diff = o2.getAvgInc() - o1.getAmount();
                return compFloat(diff);
            }
        };
    }

    /**
     * Highest score first
     * @return
     */
    public static Comparator<ResourceAmount> compScore() {
        return (o1, o2) -> {
            float diff = o2.getScore() - o1.getScore();
            return compFloat(diff);
        };
    }

    private static int compFloat(float diff) {
        if(diff > 0) {
            return 1;
        } else if(diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public float getScore() {
        return amount * avgInc;
    }


    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void decAmount(int amount) {
        this.amount -= amount;
    }

    public void decAmount() {
        decAmount(1);
    }

    public float getAvgInc() {
        return avgInc;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAvgInc(float avgInc) {
        this.avgInc = avgInc;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public void setTrading(boolean trading) {
        isTrading = trading;
    }


}
