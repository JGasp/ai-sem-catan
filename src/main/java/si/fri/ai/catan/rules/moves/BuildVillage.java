package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class BuildVillage extends Move {

    private byte landIndex;

    public BuildVillage(Game game, int playerIndex, byte landIndex) {
        super(game, playerIndex);
        this.landIndex = landIndex;
    }

    @Override
    public void make(State state) {

        state.subResource(playerIndex, ResourceType.WOOD, (byte) 1);
        state.subResource(playerIndex, ResourceType.CLAY, (byte) 1);
        state.subResource(playerIndex, ResourceType.SHEEP, (byte) 1);
        state.subResource(playerIndex, ResourceType.WHEAT, (byte) 1);

        state.buildVillages(playerIndex, landIndex);

        Land l = game.getMap().gl(landIndex);

        for(Terrain t : l.getTerrains()) {
            addTerrain(t, state, playerIndex);
        }
    }
}
