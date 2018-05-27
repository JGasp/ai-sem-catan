package si.fri.ai.catan.rules;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.rules.moves.BuildCity;
import si.fri.ai.catan.rules.moves.BuildRoad;
import si.fri.ai.catan.rules.moves.BuildVillage;
import si.fri.ai.catan.rules.moves.TradeResources;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private static final int WINNING_POINTS = 8;

    private Game game;

    public Rule(Game game) {
        this.game = game;
    }

    public int getWinner(State state) {

        for(int i=0; i<game.numberOfPLayers(); i++) {
            int points = state.getNumberOfCities(i) * 2 + state.getNumberOfVillages(i);
            if(points >= WINNING_POINTS) {
                return i;
            }
        }

        return -1;
    }

    public int throwDice() {
        int dice1 = (int) (Math.random() * 5 + 1);
        int dice2 = (int) (Math.random() * 5 + 1);

        return dice1 + dice2;
    }

    private boolean hasResourceToBuildRoad(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.CLAY) >= 1 &&
                state.getResource(playerIndex, ResourceType.WOOD) >= 1
        ) {
            return true;
        }
        return false;
    }

    private void canBuildRoadOnPosition(State state, int playerIndex, byte fromRoadIndex, byte landIndex, List<Move> moves) {
        Land land = game.getMap().gl(landIndex);

        for(Road r : land.getRoads()) {
            if(r.getIndex() != fromRoadIndex) {
                byte roadOccupation = state.getRoad(r.getIndex());
                if(roadOccupation == 0) {
                    moves.add(new BuildRoad(game, playerIndex, r.getIndex()));
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


                canBuildRoadOnPosition(state, playerIndex, roadIndex, r.getTo().getIndex(), moves);
                canBuildRoadOnPosition(state, playerIndex, roadIndex, r.getFrom().getIndex(), moves);
            }
        }
    }

    private boolean hasResourceToBuildVillage(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.CLAY) >= 1 &&
                state.getResource(playerIndex, ResourceType.WOOD) >= 1 &&
                state.getResource(playerIndex, ResourceType.SHEEP) >= 1 &&
                state.getResource(playerIndex, ResourceType.WHEAT) >= 1
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
                    moves.add(new BuildVillage(game, playerIndex, r.getTo().getIndex()));
                }

                if(canBuildVillageOnLand(state, r.getFrom().getIndex())) {
                    moves.add(new BuildVillage(game, playerIndex, r.getFrom().getIndex()));
                }

            }
        }
    }

    private boolean hasResourceToBuildCity(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.IRON) >= 3 &&
                state.getResource(playerIndex, ResourceType.WHEAT) >= 2
        ) {
            return true;
        }
        return false;
    }


    private void canBuildCity(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildCity(state, playerIndex)) {

            int villages = state.getNumberOfVillages(playerIndex);
            for(byte i=0; i<villages; i++) {
                moves.add(new BuildCity(game, playerIndex, i));
            }
        }
    }

    private void canTradeResource(ResourceType in, int playerIndex, int ratio, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            if(rt != in) {
                moves.add(new TradeResources(game, playerIndex, in, rt, ratio));
            }
        }
    }


    private void canTradeResources(State state, int playerIndex, ResourceType resource, List<Move> moves) {

        byte resourceAmount = 0;
        boolean trading = false;

        resourceAmount = state.getResource(playerIndex, resource);
        trading = state.isResourceTrading(playerIndex, resource);

        int ratio = 4;
        if(trading) {
            ratio = 2;
        } else if(state.isAnyResourceTrading(playerIndex)) {
            ratio = 3;
        }

        if(resourceAmount >= ratio) {
            canTradeResource(resource, playerIndex, ratio, moves);
        }
    }

    private void canTrade(State state, int playerIndex, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            canTradeResources(state, playerIndex, rt, moves);
        }
    }

    public List<Move> getAllMoves(State state, int playerIndex) {
        List<Move> moves = new ArrayList<>();

        canBuildRoad(state, playerIndex, moves);
        canBuildVillage(state, playerIndex, moves);
        canBuildCity(state, playerIndex, moves);

        canTrade(state, playerIndex, moves);

        return moves;
    }

}

