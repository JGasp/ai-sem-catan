package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.rules.moves.base.Move;

public class MoveRobber extends Move {

    private byte terrainIndex;
    private int robbingPlayerIndex;

    public MoveRobber(Game game, int playerIndex, byte terrainIndex, int robbingPlayerIndex) {
        super(game, playerIndex);
        this.terrainIndex = terrainIndex;
        this.robbingPlayerIndex = robbingPlayerIndex;
    }

    @Override
    public void make(State state) {

        Terrain from = game.getMap().gt(state.getThiefLand());
        for(Land l : from.getLands()) {
            byte landOccupation = state.getLand(l.getIndex());

            if(landOccupation != 0) {
                int loPlayerIndex = landOccupation / State.FIGURE_TAG_OFFSET;
                int figure = landOccupation % State.FIGURE_TAG_OFFSET;

                if(figure > 0) {
                    state.addResourceIncome(loPlayerIndex, from.getType(), from.getDice(), (byte) figure);
                }
            }
        }

        Terrain to = game.getMap().gt(terrainIndex);
        for(Land l : to.getLands()) {
            byte landOccupation = state.getLand(l.getIndex());

            if(landOccupation != 0) {
                int loPlayerIndex = landOccupation / State.FIGURE_TAG_OFFSET;
                int figure = landOccupation % State.FIGURE_TAG_OFFSET;

                if(figure > 0) {
                    state.subResourceIncome(loPlayerIndex, to.getType(), to.getDice(), (byte) figure);
                }
            }
        }

        state.setThiefLand(terrainIndex);
    }

}
