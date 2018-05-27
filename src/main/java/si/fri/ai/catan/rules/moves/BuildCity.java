package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;

public class BuildCity extends Move {

    private byte villageIndex;

    public BuildCity(Game game, int playerIndex, byte villageIndex) {
        super(game, playerIndex);
        this.villageIndex = villageIndex;
    }

    @Override
    public void make(State state) {

        state.subIron(playerIndex, 3);
        state.subWheat(playerIndex, 2);

        byte landIndex = state.buildCity(playerIndex, villageIndex);

        Land l = game.getMap().gl(landIndex);

        for(Terrain t : l.getTerrains()) {
            addTerrain(t, state, playerIndex);
        }

    }
}
