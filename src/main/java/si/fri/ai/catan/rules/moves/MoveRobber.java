package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.ResourceAmount;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class MoveRobber extends Move {

    private byte terrainIndex;
    private int robbingPlayerIndex;
    private ResourceType took;

    public MoveRobber(int playerIndex, byte terrainIndex, int robbingPlayerIndex) {
        super(playerIndex);
        this.terrainIndex = terrainIndex;
        this.robbingPlayerIndex = robbingPlayerIndex;
    }

    @Override
    public void make(Game game, State state) {

        Terrain from = game.getMap().gt(state.getThiefTerrain());
        if(from.getType() != null) {
            for(Land l : from.getLands()) {
                byte landOccupation = state.getLand(l.getIndex());

                if(landOccupation != 0) {
                    int loPlayerIndex = landOccupation / State.FIGURE_TAG_OFFSET;
                    int figure = landOccupation % State.FIGURE_TAG_OFFSET;

                    state.addResourceIncome(loPlayerIndex, from.getType(), from.getDice(), (byte) figure);
                }
            }
        }

        Terrain to = game.getMap().gt(terrainIndex);
        if(to.getType() != null) {
            for(Land l : to.getLands()) {
                byte landOccupation = state.getLand(l.getIndex());

                if(landOccupation != 0) {
                    int loPlayerIndex = landOccupation / State.FIGURE_TAG_OFFSET;
                    int figure = landOccupation % State.FIGURE_TAG_OFFSET;

                    state.subResourceIncome(loPlayerIndex, to.getType(), to.getDice(), (byte) figure);
                }
            }
        }

        state.setThiefLand(terrainIndex);


        if(robbingPlayerIndex != -1) {
            int index = 0;
            int totalAmount = 0;
            ResourceAmount[] resourceAmounts = new ResourceAmount[5];

            for(ResourceType rt : ResourceType.values()) {
                int amount = state.getResource(robbingPlayerIndex, rt);
                totalAmount += amount;
                resourceAmounts[index++] = new ResourceAmount(rt, amount);
            }

            if(totalAmount > 0) {
                int randomResource = Rule.random.nextInt(totalAmount);

                int checked = 0;
                for(ResourceAmount ra : resourceAmounts) {
                    if(ra.getAmount() > 0) {
                        checked += ra.getAmount();

                        if(checked >= randomResource) {
                            state.addResource(playerIndex, ra.getType(), (byte) 1);
                            state.subResource(robbingPlayerIndex, ra.getType(), (byte) 1);

                            took = ra.getType();
                            break;
                        }
                    }
                }
            }
        }
    }


    @Override
    public String toString() {
        return String.format("[%d] Moved robber to [%d] and took [%s] from player [%d]",
                playerIndex, terrainIndex, took.name(), robbingPlayerIndex);
    }
}
