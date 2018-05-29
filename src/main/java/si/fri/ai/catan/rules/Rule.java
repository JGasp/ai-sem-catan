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

import java.util.*;

public class Rule {

    public static final Random random = new Random();
    private static final int WINNING_POINTS = 10;

    private Game game;

    public Rule(Game game) {
        this.game = game;
    }



    public boolean isWinner(State state, int playerIndex) {
        if(state.getScore(playerIndex) >= WINNING_POINTS) {
            return true;
        }
        return false;
    }

    public int throwDice() {
        int dice1 = random.nextInt(5) + 1;
        int dice2 = random.nextInt(5) + 1;

        return dice1 + dice2;
    }



    public boolean hasResourceToBuildRoad(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.CLAY) >= 1 &&
                state.getResource(playerIndex, ResourceType.WOOD) >= 1
        ) {
            return true;
        }
        return false;
    }

    public void getLocationsToBuildRoadForLand(State state, int playerIndex, int sourceRoadIndex, int landIndex, List<Move> moves) {
        Land land = game.getMap().gl(landIndex);

        for(Road r : land.getRoads()) {
            if(r.getIndex() != sourceRoadIndex) {
                byte roadOccupation = state.getRoad(r.getIndex());
                if(roadOccupation == 0) {
                    moves.add(new BuildRoad(playerIndex, r.getIndex()));
                }
            }
        }
    }

    public void getLocationsToBuildRoad(State state, int playerIndex, List<Move> moves) {

        byte roads = state.getNumberOfRoads(playerIndex);
        for(int i=0; i<roads; i++) {

            byte roadIndex = state.getRoadLocation(playerIndex, i);
            Road road = game.getMap().gr(roadIndex);

            getLocationsToBuildRoadForLand(state, playerIndex, road.getIndex(), road.getTo().getIndex(), moves);
            getLocationsToBuildRoadForLand(state, playerIndex, road.getIndex(), road.getFrom().getIndex(), moves);
        }

    }

    public void canBuildRoad(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildRoad(state, playerIndex) && state.isAnyRoadAvailable(playerIndex)) {
            getLocationsToBuildRoad(state, playerIndex, moves);
        }
    }




    public boolean hasResourceToBuildVillage(State state, int playerIndex) {
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

    public boolean canBuildVillageOnLand(State state, byte landIndex) {
        byte landOccupation = state.getLand(landIndex);
        if(landOccupation == 0) {
            Land l = game.getMap().gl(landIndex);

            for(Road r : l.getRoads()) {
                Land neighbour = r.getNeighbour(l);

                byte neighbourLandOccupation = state.getLand(neighbour.getIndex());
                if(neighbourLandOccupation != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void getBuildVillageMoves(State state, int playerIndex, List<Move> moves) {
        if(state.isAnyVillageAvailable(playerIndex)) {
            int roads = state.getNumberOfRoads(playerIndex);
            for(int i=0; i<roads; i++) {

                Road r = game.getMap().gr(state.getRoadLocation(playerIndex, i));

                if(canBuildVillageOnLand(state, r.getTo().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getTo().getIndex()));
                }

                if(canBuildVillageOnLand(state, r.getFrom().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getFrom().getIndex()));
                }

            }
        }
    }

    public void getValidBuildVillageMoves(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildVillage(state, playerIndex)) {
            getBuildVillageMoves(state, playerIndex, moves);
        }
    }



    public boolean hasResourceToBuildCity(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.IRON) >= 3 &&
                state.getResource(playerIndex, ResourceType.WHEAT) >= 2
        ) {
            return true;
        }
        return false;
    }

    public void getBuildCityMoves(State state, int playerIndex, List<Move> moves) {
        if(state.isAnyCityAvailable(playerIndex)) {
            int villages = state.getNumberOfVillages(playerIndex);
            for(byte i=0; i<villages; i++) {
                moves.add(new BuildCity(playerIndex, i));
            }
        }
    }

    public void getValidBuildCityMoves(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildCity(state, playerIndex)) {
            getBuildCityMoves(state, playerIndex, moves);
        }
    }




    public void canTradeResource(ResourceType in, int playerIndex, int ratio, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            if(rt != in) {
                moves.add(new TradeResources(playerIndex, in, rt, ratio));
            }
        }
    }

    public void canTradeResources(State state, int playerIndex, ResourceType resource, List<Move> moves) {

        byte resourceAmount = state.getResource(playerIndex, resource);
        boolean trading = state.isResourceTrading(playerIndex, resource);

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

    public void canTrade(State state, int playerIndex, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            canTradeResources(state, playerIndex, rt, moves);
        }
    }



    public List<Move> getAllMoves(State state, int playerIndex) {
        List<Move> moves = new ArrayList<>();

        canBuildRoad(state, playerIndex, moves);
        getValidBuildVillageMoves(state, playerIndex, moves);
        getValidBuildCityMoves(state, playerIndex, moves);

        canTrade(state, playerIndex, moves);

        return moves;
    }

}

