package si.fri.ai.catan.players;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.List;

public class MonteCarloPlayer extends Player {

    public MonteCarloPlayer(Game game, int playerIndex) {
        super(game, playerIndex);
    }

    @Override
    public PlacingVillage playPlacingTurn(State state) {
        return null;
    }

    @Override
    public List<Move> playTurn(State state) {
        return null;
    }

    @Override
    public List<DropResources> dropResources(State state) {
        return null;
    }

    @Override
    public MoveRobber moveRobber(State state) {
        return null;
    }



}
