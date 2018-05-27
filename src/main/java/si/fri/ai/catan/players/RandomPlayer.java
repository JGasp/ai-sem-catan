package si.fri.ai.catan.players;

import si.fri.ai.catan.State;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.ArrayList;
import java.util.List;

public class RandomPlayer extends Player {


    @Override
    public List<Move> playPlacingTurn(State state) {
        return null;
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
