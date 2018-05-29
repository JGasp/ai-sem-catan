package si.fri.ai.catan.dto;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Arrays;

public class PlayerResAvgInc {

    private float[] resource;

    public PlayerResAvgInc(State state, int playerIndex) {
        resource = new float[State.DIFFERENT_RESOURCES];
        for(ResourceType rt : ResourceType.values()) {
            resource[rt.getIndex()] = state.getResourceAvgIncome(playerIndex, rt);
        }
    }

    public PlayerResAvgInc(PlayerResAvgInc playerResAvgInc) {
        resource = Arrays.copyOf(playerResAvgInc.resource, playerResAvgInc.resource.length);
    }

    public PlayerResAvgInc copy() {
        return new PlayerResAvgInc(this);
    }

    public void addAvgIncome(ResourceType resourceType, float amount) {
        if(resourceType != null) {
            resource[resourceType.getIndex()] += amount;
        }
    }

}
