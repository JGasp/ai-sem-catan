package si.fri.ai.catan.players.base;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.List;

public abstract class Player {

    private Game game;
    private int playerIndex;


    public Player(Game game, int playerIndex) {
        this.game = game;
        this.playerIndex = playerIndex;
    }


    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public Game getGame() {
        return game;
    }

    public Map getMap() {
        return game.getMap();
    }

    public Rule getRule() {
        return game.getRule();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract PlacingVillage playPlacingTurn(State state);
    public abstract List<Move> playTurn(State state);
    public abstract List<DropResources> dropResources(State state);
    public abstract MoveRobber moveRobber(State state);

}
