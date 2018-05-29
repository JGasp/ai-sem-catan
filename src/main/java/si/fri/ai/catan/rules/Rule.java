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
import java.util.Random;

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

    private boolean hasResourceToBuildRoad(State state, int playerIndex) {
        if(
                state.getResource(playerIndex, ResourceType.CLAY) >= 1 &&
                state.getResource(playerIndex, ResourceType.WOOD) >= 1
        ) {
            return true;
        }
        return false;
    }

    private void getLocationsToBuildRoad(State state, int playerIndex, List<BuildRoad> moves) {

        byte roads = state.getNumberOfRoads(playerIndex);
        for(int i=0; i<roads; i++) {

            byte roadIndex = state.getRoadLocation(playerIndex, i);
            Road startRoad = game.getMap().gr(roadIndex);



            for(Byte landIndex : lands) {
                Land land = game.getMap().gl(landIndex);

                for(Road r : land.getRoads()) {
                    if(r.getIndex() != startRoad.getIndex()) {
                        byte roadOccupation = state.getRoad(r.getIndex());
                        if(roadOccupation == 0) {
                            moves.add(new BuildRoad(playerIndex, r.getIndex()));
                        }
                    }
                }

            }
        }

    }

    private void canBuildRoad(State state, int playerIndex, List<Move> moves) {
        if(hasResourceToBuildRoad(state, playerIndex) && state.isAnyRoadAvailable(playerIndex)) {

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
        if(hasResourceToBuildVillage(state, playerIndex) && state.isAnyVillageAvailable(playerIndex)) {

            int roads = state.getNumberOfRoads(playerIndex);
            for(int i=0; i<roads; i++) {

                byte roadIndex = state.getRoadLocation(playerIndex, i);
                Road r = game.getMap().gr(roadIndex);

                if(canBuildVillageOnLand(state, r.getTo().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getTo().getIndex()));
                }

                if(canBuildVillageOnLand(state, r.getFrom().getIndex())) {
                    moves.add(new BuildVillage(playerIndex, r.getFrom().getIndex()));
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
        if(hasResourceToBuildCity(state, playerIndex) && state.isAnyCityAvailable(playerIndex)) {

            int villages = state.getNumberOfVillages(playerIndex);
            for(byte i=0; i<villages; i++) {
                moves.add(new BuildCity(playerIndex, i));
            }
        }
    }

    private void canTradeResource(ResourceType in, int playerIndex, int ratio, List<Move> moves) {
        for(ResourceType rt : ResourceType.values()) {
            if(rt != in) {
                moves.add(new TradeResources(playerIndex, in, rt, ratio));
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

