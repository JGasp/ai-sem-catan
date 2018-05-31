package si.fri.ai.catan.players;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.PlayerResAmount;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.players.monteCarlo.MonteCarloSimulation;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloPlayer extends Player {

    private MonteCarloSimulation simulation;

    public MonteCarloPlayer(Game game, int playerIndex) {
        super(game, playerIndex);
        simulation = new MonteCarloSimulation(game, playerIndex);
    }

    @Override
    public List<Move> playPlacingTurn(State state) {
        return simulation.getPlacingMoves(state);
    }

    @Override
    public List<Move> playTurn(State state) {
        return simulation.getTurnMoves(state);
    }

    @Override
    public DropResources dropResources(State state) {
        PlayerResAmount pra = new PlayerResAmount(state, getPlayerIndex());
        if(pra.getTotalAmount() > 7) {
            return simulation.getDropResourceMove(state);
        } else {
            return new DropResources(getPlayerIndex());
        }
    }

    @Override
    public MoveRobber moveRobber(State state) {
        return simulation.getMoveRobberMove(state);
    }



}
