package si.fri.ai.catan.dto;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Arrays;

public class PlayerResAmount {

    private int resource[] = new int[State.DIFFERENT_RESOURCES];

    public PlayerResAmount() {
    }

    public PlayerResAmount(State state, int playerIndex) {
        for(ResourceType rt : ResourceType.values()) {
            resource[rt.getIndex()] = state.getResource(playerIndex, rt);
        }
    }

    public PlayerResAmount(PlayerResAmount pra) {
        this.resource = Arrays.copyOf(pra.resource, pra.resource.length);
    }


    public int getTotalAmount() {
        int totalAmount = 0;
        for(int i=0; i<resource.length; i++) {
            totalAmount += resource[i];
        }
        return totalAmount;
    }

    public PlayerResAmount copy() {
        return new PlayerResAmount(this);
    }

    public int get(ResourceType rt) {
        return resource[rt.getIndex()];
    }

    public void set(ResourceType rt, int amount) {
        resource[rt.getIndex()] = amount;
    }

    public void add(ResourceType rt, int amount) {
        resource[rt.getIndex()] += amount;
    }

    public void sub(ResourceType rt, int amount) {
        resource[rt.getIndex()] -= amount;
    }

    public ResourceType getNonZero() {
        for(ResourceType rt : ResourceType.values()) {
            if(get(rt) > 0) {
                return rt;
            }
        }

        return null;
    }

}
