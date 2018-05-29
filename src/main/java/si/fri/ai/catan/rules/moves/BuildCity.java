package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class BuildCity extends Move {

    private byte villageIndex;

    public BuildCity(int playerIndex, byte villageIndex) {
        super(playerIndex);
        this.villageIndex = villageIndex;
    }

    @Override
    public void make(Game game, State state) {

        state.subResource(playerIndex, ResourceType.IRON, (byte) 3);
        state.subResource(playerIndex, ResourceType.WHEAT, (byte) 2);

        byte landIndex = state.buildCity(playerIndex, villageIndex);

        Land l = game.getMap().gl(landIndex);

        for(Terrain t : l.getTerrains()) {
            addTerrain(t, state, playerIndex);
        }

    }


    @Override
    public String toString() {
        return "BuildCity{" +
                "villageIndex=" + villageIndex +
                ", playerIndex=" + playerIndex +
                '}';
    }
}
