package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Objects;

public class BuildVillage extends Move {

    private byte landIndex;

    public BuildVillage(int playerIndex, byte landIndex) {
        super(playerIndex);
        this.landIndex = landIndex;
    }

    @Override
    public void make(Game game, State state) {

        state.subResource(playerIndex, ResourceType.WOOD, (byte) 1);
        state.subResource(playerIndex, ResourceType.CLAY, (byte) 1);
        state.subResource(playerIndex, ResourceType.SHEEP, (byte) 1);
        state.subResource(playerIndex, ResourceType.WHEAT, (byte) 1);

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
    }

    public byte getLandIndex() {
        return landIndex;
    }

    @Override
    public String toString() {
        return String.format("[%d] Build village on [%d]", playerIndex, landIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BuildVillage.class.getSimpleName(),playerIndex, landIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BuildVillage) {
            BuildVillage that = (BuildVillage) obj;
            return landIndex == that.landIndex && playerIndex == that.playerIndex;
        }
        return false;
    }
}
