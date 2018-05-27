package si.fri.ai.catan.players;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.ArrayList;
import java.util.List;

public class RandomPlayer extends Player {


    public RandomPlayer(Game game, int playerIndex) {
        super(game, playerIndex);
    }

    @Override
    public PlacingVillage playPlacingTurn(State state) {
        while (true) {
            byte randomLand = (byte) ((int) (Math.random() * getGame().getMap().getLands().size()));

            if(state.getLand(randomLand) == 0) {
                Land l = getGame().getMap().gl(randomLand);

                while(true) {
                    byte randomLandRoad = (byte) ((int) (Math.random() * l.getRoads().length));

                    Road r = l.getRoads()[randomLandRoad];

                    if(state.getRoad(r.getIndex()) == 0) {
                        return new PlacingVillage(getGame(), getPlayerIndex(), randomLand, r.getIndex());
                    }
                }
            }
        }
    }

    @Override
    public List<Move> playTurn(State state) {

        List<Move> allMoves = getGame().getRule().getAllMoves(state, getPlayerIndex());

        List<Move> doMoves = new ArrayList<>();
        if(!allMoves.isEmpty()) {
            Move doneMove = allMoves.get((int) (Math.random() * (allMoves.size() - 1)));
            doMoves.add(doneMove);
        }

        return doMoves;
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
