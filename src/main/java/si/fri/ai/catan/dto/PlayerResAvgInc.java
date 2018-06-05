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


    public float get(ResourceType rt) {
        return resource[rt.getIndex()];
    }

    public void set(ResourceType rt, float amount) {
        resource[rt.getIndex()] = amount;
    }


    public float getAverageValue() {
        float avg = 0;

        for(int i=0; i<resource.length; i++) {
            avg += resource[i];
        }

        avg /= resource.length;

        return avg;
    }

    public float getDerivation(float avg) {
        float derivation = 0;

        for(int i=0; i<resource.length; i++) {
            derivation += Math.abs(resource[i] - avg);
        }

        derivation /= resource.length;

        return derivation;
    }

    public float getTotalAverageIncome() {
        float totalAvgInc = 0;

        for(ResourceType rt : ResourceType.values()) {
            totalAvgInc += get(rt);
        }

        return totalAvgInc;
    }
}
