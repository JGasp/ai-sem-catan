package si.fri.ai.catan.players.base;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.List;

public abstract class Player {

    private int playerIndex;
    private Game game;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract List<Move> playPlacingTurn(State state);
    public abstract List<Move> playTurn(State state);
    public abstract List<DropResources> dropResources(State state);
    public abstract MoveRobber moveRobber(State state);

}
