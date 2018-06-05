package si.fri.ai.catan.players;

import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.ResourceAmount;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingRoad;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.*;

public class RandomPlayer extends Player {

    public RandomPlayer(int playerIndex) {
        super(playerIndex);
    }

    @Override
    public List<Move> playPlacingTurn(State state) {
        while (true) {
            byte randomLand = (byte) Rule.random.nextInt(getMap().getLands().size() - 1);

            if(Rule.canBuildVillageOnLand(getMap(), state, randomLand)) {
                Land l = getGame().getMap().gl(randomLand);

                byte randomLandRoad = (byte) Rule.random.nextInt(l.getRoads().length - 1);
                Road r = l.getRoads()[randomLandRoad];

                if(state.getRoad(r.getIndex()) == 0) {
                    PlacingVillage pv = new PlacingVillage(getPlayerIndex(), randomLand);
                    PlacingRoad pr = new PlacingRoad(getPlayerIndex(), r.getIndex());

                    List<Move> placing = new ArrayList<>();
                    placing.add(pv);
                    placing.add(pr);

                    return placing;
                }
            }
        }
    }

    @Override
    public List<Move> playTurn(State state) {
        State tempState = state.copy();

        List<Move> doMoves = new ArrayList<>();
        List<Move> allMoves = Rule.getAllMoves(getMap(), tempState, getPlayerIndex());
        while (!allMoves.isEmpty()) {
            int randMove = Rule.random.nextInt(allMoves.size() + 1);

            if(allMoves.size() > randMove) {
                Move doMove = allMoves.get(randMove);
                doMoves.add(doMove);

                doMove.make(getGame(), tempState);

                allMoves = Rule.getAllMoves(getMap(), tempState, getPlayerIndex());
            } else {
                break;
            }

        }

        return doMoves;
    }

    @Override
    public DropResources dropResources(State state) {
        DropResources dropResources = new DropResources(getPlayerIndex());

        int index = 0;
        int totalAmount = 0;
        ResourceAmount[] resourceAmounts = new ResourceAmount[5];

        for(ResourceType rt : ResourceType.values()) {
            int amount = state.getResource(getPlayerIndex(), rt);
            totalAmount += amount;
            resourceAmounts[index++] = new ResourceAmount(rt, amount);
        }

        if(totalAmount > 7) {
            int drop = totalAmount / 2;

            for(int i=0; i<drop; i++) {
                int randResource = Rule.random.nextInt(totalAmount);

                int checked = 0;
                for(ResourceAmount ra : resourceAmounts) {
                    if(ra.getAmount() > 0) {
                        checked += ra.getAmount();

                        if(checked >= randResource) {
                            dropResources.add(ra.getType(), (byte) 1);
                            ra.decAmount();
                            totalAmount--;
                            break;
                        }
                    }
                }

            }

        }

        return dropResources;
    }

    @Override
    public MoveRobber moveRobber(State state) {

        while(true) {
            byte terrainIndex = (byte) Rule.random.nextInt(getGame().getMap().getTerrains().size() - 1);
            if(state.getThiefTerrain() != terrainIndex) {
                Terrain t = getGame().getMap().gt(terrainIndex);

                HashSet<Integer> robbingPlayerIndex = new HashSet<>();
                for(Land l : t.getLands()) {
                    byte landOccupation = state.getLand(l.getIndex());
                    if(landOccupation != 0) {
                        int pi = landOccupation / 10;
                        if(pi != getPlayerIndex()) {
                            robbingPlayerIndex.add(pi);
                        }
                    }
                }

                int rpi = -1;
                if(robbingPlayerIndex.size() != 0) {
                    byte randomPlayer = (byte) Rule.random.nextInt(robbingPlayerIndex.size());
                    for(Integer pi : robbingPlayerIndex) {
                        if(randomPlayer == 0) {
                            rpi = pi;
                            break;
                        }
                        randomPlayer--;
                    }
                }

                return new MoveRobber(getPlayerIndex(), terrainIndex, rpi);
            }
        }
    }

}
