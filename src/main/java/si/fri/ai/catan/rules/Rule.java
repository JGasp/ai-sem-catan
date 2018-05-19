package si.fri.ai.catan.rules;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.rules.moves.BuildCity;
import si.fri.ai.catan.rules.moves.BuildRoad;
import si.fri.ai.catan.rules.moves.BuildVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private Game game;

    public Rule(Game game) {
        this.game = game;
    }

    private boolean hasResourceToBuildRoad(State state, int playerIndex) {
        if(
                state.getClay(playerIndex) >= 1 &&
                state.getWood(playerIndex) >= 1
        ) {
            return true;
        }
        return false;
    }

    private void canBuildRoadOnPosition(State state, byte fromRoadIndex, byte landIndex, List<Move> moves) {
        Land land = game.getMap().gl(landIndex);

        for(Road r : land.getRoads()) {
            if(r.getIndex() != fromRoadIndex) {
                byte roadOccupation = state.getRoad(r.getIndex());
                if(roadOccupation == 0) {
                    moves.add(new BuildRoad(r.getIndex()));
                }
            }
        }

    }

    private void canBuildRoad(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildRoad(state, playerIndex)) {
            byte roads = state.getNumberOfRoads(playerIndex);
            for(int i=0; i<roads; i++) {

                byte roadIndex = state.getRoadLocation(playerIndex, i);
                Road r = game.getMap().gr(roadIndex);


                canBuildRoadOnPosition(state, roadIndex, r.getTo().getIndex(), moves);
                canBuildRoadOnPosition(state, roadIndex, r.getFrom().getIndex(), moves);
            }
        }
    }

    private boolean hasResourceToBuildVillage(State state, int playerIndex) {
        if(
                state.getClay(playerIndex) >= 1 &&
                state.getWood(playerIndex) >= 1 &&
                state.getSheep(playerIndex) >= 1 &&
                state.getWheat(playerIndex) >= 1
        ) {
            return true;
        }
        return false;
    }

    private boolean canBuildVillageOnLand(State state, byte landIndex) {

        byte landOccupation = state.getLand(landIndex);
        if(landOccupation == 0) {
            Land l = game.getMap().gl(landIndex);

            for(Road r : l.getRoads()) {

                Land neighbour;
                if(r.getTo() == l) {
                    neighbour = r.getFrom();
                } else {
                    neighbour = r.getTo();
                }

                byte neighbourLandOccupation = state.getLand(neighbour.getIndex());
                if(neighbourLandOccupation != 0) {
                    return false;
                }

            }

            return true;
        }

        return false;
    }

    private void canBuildVillage(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildVillage(state, playerIndex)) {

            int roads = state.getNumberOfRoads(playerIndex);
            for(int i=0; i<roads; i++) {

                byte roadIndex = state.getRoadLocation(playerIndex, i);
                Road r = game.getMap().gr(roadIndex);

                if(canBuildVillageOnLand(state, r.getTo().getIndex())) {
                    moves.add(new BuildVillage(r.getTo().getIndex()));
                }

                if(canBuildVillageOnLand(state, r.getFrom().getIndex())) {
                    moves.add(new BuildVillage(r.getFrom().getIndex()));
                }

            }
        }
    }

    private boolean hasResourceToBuildCity(State state, int playerIndex) {
        if(
                state.getIron(playerIndex) >= 3 &&
                state.getWheat(playerIndex) >= 2
        ) {
            return true;
        }
        return false;
    }


    private void canBuildCity(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildCity(state, playerIndex)) {

            int villages = state.getNumberOfVillages(playerIndex);
            for(int i=0; i<villages; i++) {
                moves.add(new BuildCity(i));
            }
        }
    }

    public List<Move> getAllMoves(State state, int playerIndex) {
        List<Move> moves = new ArrayList<>();

        canBuildRoad(state, playerIndex, moves);
        canBuildVillage(state, playerIndex, moves);
        canBuildCity(state, playerIndex, moves);

        return moves;
    }

    public State makeMove(State state, Move move) {
        State copy = state.copy();




        return copy;
    }

}