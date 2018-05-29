package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;

public class PlacingVillage extends Move {

    private byte landIndex;
    private byte roadIndex;

    public PlacingVillage(int playerIndex, byte landIndex, byte roadIndex) {
        super(playerIndex);
        this.landIndex = landIndex;
        this.roadIndex = roadIndex;
    }

    @Override
    public void make(Game game, State state) {

        state.buildVillages(playerIndex, landIndex);

        Land l = game.getMap().gl(landIndex);

        if(l.isAnyTrading()) {
            state.activateAnyResourceTrading(playerIndex);
        } else if(l.getTrading() != null) {
            state.activateResourceTrading(playerIndex, l.getTrading());
        }


        for(Terrain t : l.getTerrains()) {
            addTerrain(t, state, playerIndex);
        }

        state.buildRoad(playerIndex, roadIndex);

    }

    @Override
    public String toString() {
        return String.format("[%d] Placed village on [%d] and road on [%d]", playerIndex, landIndex, roadIndex);
    }

}
