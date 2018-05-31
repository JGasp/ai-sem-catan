package si.fri.ai.catan.rules;

import si.fri.ai.catan.State;
import si.fri.ai.catan.map.Map;
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


    public static boolean isWinner(State state, int playerIndex) {
        if(state.getScore(playerIndex) >= WINNING_POINTS) {
            return true;
        }
        return false;
    }

    public static int throwDice() {
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        return dice1 + dice2;
    }



    public static boolean hasResourceToBuildRoad(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.CLAY) >= 1 &&
                state.getResource(playerIndex, ResourceType.WOOD) >= 1
        ) {
            return true;
        }
        return false;
    }

    public static void getLocationsToBuildRoadForLand(Map map, State state, int playerIndex, int sourceRoadIndex, int landIndex, List<Move> moves) {
        Land land = map.gl(landIndex);

        for(Road r : land.getRoads()) {
            if(r.getIndex() != sourceRoadIndex) {
                byte roadOccupation = state.getRoad(r.getIndex());
                if(roadOccupation == 0) {
                    moves.add(new BuildRoad(playerIndex, r.getIndex()));
                }
            }
        }
    }

    public static void getLocationsToBuildRoad(Map map, State state, int playerIndex, List<Move> moves) {

        byte roads = state.getNumberOfRoads(playerIndex);
        for(int i=0; i<roads; i++) {

            byte roadIndex = state.getRoadLocation(playerIndex, i);
            Road road = map.gr(roadIndex);

            getLocationsToBuildRoadForLand(map, state, playerIndex, road.getIndex(), road.getTo().getIndex(), moves);
            getLocationsToBuildRoadForLand(map, state, playerIndex, road.getIndex(), road.getFrom().getIndex(), moves);
        }

    }

    public static void canBuildRoadMoves(Map map, State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildRoad(state, playerIndex) && state.isAnyRoadAvailable(playerIndex)) {
            getLocationsToBuildRoad(map, state, playerIndex, moves);
        }
    }




    public static boolean hasResourceToBuildVillage(State state, int playerIndex) {
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

    public static boolean canBuildVillageOnLand(Map map, State state, byte landIndex) {
        byte landOccupation = state.getLand(landIndex);
        if(landOccupation == 0) {
            Land l = map.gl(landIndex);

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

    public static void getBuildVillageMoves(Map map, State state, int playerIndex, List<Move> moves) {
        if(state.isAnyVillageAvailable(playerIndex)) {
            int roads = state.getNumberOfRoads(playerIndex);
            for(int i=0; i<roads; i++) {

                Road r = map.gr(state.getRoadLocation(playerIndex, i));

                if(canBuildVillageOnLand(map, state, r.getTo().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getTo().getIndex()));
                }

                if(canBuildVillageOnLand(map, state, r.getFrom().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getFrom().getIndex()));
                }

            }
        }
    }

    public static void getValidBuildVillageMoves(Map map, State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildVillage(state, playerIndex)) {
            getBuildVillageMoves(map, state, playerIndex, moves);
        }
    }



    public static boolean hasResourceToBuildCity(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.IRON) >= 3 &&
                state.getResource(playerIndex, ResourceType.WHEAT) >= 2
        ) {
            return true;
        }
        return false;
    }

    public static void getBuildCityMoves(State state, int playerIndex, List<Move> moves) {
        if(state.isAnyCityAvailable(playerIndex)) {
            int villages = state.getNumberOfVillages(playerIndex);
            for(byte i=0; i<villages; i++) {
                moves.add(new BuildCity(playerIndex, i));
            }
        }
    }

    public static void getValidBuildCityMoves(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildCity(state, playerIndex)) {
            getBuildCityMoves(state, playerIndex, moves);
        }
    }




    public static void canTradeResource(ResourceType in, int playerIndex, int ratio, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            if(rt != in) {
                moves.add(new TradeResources(playerIndex, in, rt, ratio));
            }
        }
    }

    public static void canTradeResources(State state, int playerIndex, ResourceType resource, List<Move> moves) {

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

    public static void canTrade(State state, int playerIndex, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            canTradeResources(state, playerIndex, rt, moves);
        }
    }



    public static List<Move> getAllMoves(Map map, State state, int playerIndex) {
        List<Move> moves = new ArrayList<>();

        canBuildRoadMoves(map, state, playerIndex, moves);
        getValidBuildVillageMoves(map, state, playerIndex, moves);
        getValidBuildCityMoves(state, playerIndex, moves);

        canTrade(state, playerIndex, moves);

        return moves;
    }

}

