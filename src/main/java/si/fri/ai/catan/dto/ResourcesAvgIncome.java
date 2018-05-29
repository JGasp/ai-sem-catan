package si.fri.ai.catan.dto;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class ResourcesAvgIncome {

    private float wood;
    private float clay;
    private float iron;
    private float sheep;
    private float wheat;

    public ResourcesAvgIncome(State state, int playerIndex) {
        wood = state.getResourceAvgIncome(playerIndex, ResourceType.WOOD);
        clay = state.getResourceAvgIncome(playerIndex, ResourceType.CLAY);
        iron = state.getResourceAvgIncome(playerIndex, ResourceType.IRON);
        sheep = state.getResourceAvgIncome(playerIndex, ResourceType.SHEEP);
        wheat = state.getResourceAvgIncome(playerIndex, ResourceType.WHEAT);
    }

    public ResourcesAvgIncome(ResourcesAvgIncome resourcesAvgIncome) {
        this.wood = resourcesAvgIncome.wood;
        this.clay = resourcesAvgIncome.clay;
        this.iron = resourcesAvgIncome.iron;
        this.sheep = resourcesAvgIncome.sheep;
        this.wheat = resourcesAvgIncome.wheat;
    }

    public ResourcesAvgIncome copy() {
        return new ResourcesAvgIncome(this);
    }

    public void addAvgIncome(ResourceType resourceType, float amount) {
        switch (resourceType) {
            case CLAY: clay += amount; break;
            case IRON: iron += amount; break;
            case WOOD: wood += amount; break;
            case WHEAT: wheat += amount; break;
            case SHEEP: sheep += amount; break;
        }
    }

    public float getWood() {
        return wood;
    }

    public float getClay() {
        return clay;
    }

    public float getIron() {
        return iron;
    }

    public float getSheep() {
        return sheep;
    }

    public float getWheat() {
        return wheat;
    }
}
