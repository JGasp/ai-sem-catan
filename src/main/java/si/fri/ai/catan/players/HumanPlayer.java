package si.fri.ai.catan.players;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.ResourcesAvgIncome;
import si.fri.ai.catan.dto.RobbingPotential;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HumanPlayer extends Player {

    public HumanPlayer(Game game, int playerIndex) {
        super(game, playerIndex);
    }

    private float getResourceIncomeScore(ResourcesAvgIncome resourcesAvgIncome) {
        return 0;
    }

    private Land getBestLand(List<Land> lands, State state, ResourcesAvgIncome resourcesAvgIncome) {

        float bestScore = 0;
        Land bestLand = null;

        for(Land l : lands) {
            if(state.getLand(l.getIndex()) == 0) {

                ResourcesAvgIncome tempResourceIncome = resourcesAvgIncome.copy();

                for(Terrain t : l.getTerrains()) {
                    tempResourceIncome.addAvgIncome(t.getType(), State.getDiceRatio(t.getDice()));
                }

                float score = getResourceIncomeScore(tempResourceIncome);
                if(score > bestScore) {
                    bestLand = l;
                }
            }
        }

        return bestLand;
    }

    @Override
    public PlacingVillage playPlacingTurn(State state) {

        ResourcesAvgIncome ri = new ResourcesAvgIncome(state, getPlayerIndex());

        Land bestGlobalLand = getBestLand(getMap().getLands(), state, ri);

        List<Land> neighbours = new ArrayList<>();
        for(Road r : bestGlobalLand.getRoads()) {
            neighbours.add(r.getOther(bestGlobalLand));
        }

        for(Terrain t : bestGlobalLand.getTerrains()) {
            ri.addAvgIncome(t.getType(), State.getDiceRatio(t.getDice()));
        }

        Land bestLocalLand = getBestLand(neighbours, state, ri);

        Road bestLocalRoad = bestGlobalLand.getConnectingRoad(bestLocalLand);

        return new PlacingVillage(getPlayerIndex(), bestGlobalLand.getIndex(), bestLocalRoad.getIndex());
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

        Terrain bestTerrain = getMap().gt(8);
        float bestDenialScore = 0;

        terrainLoop:
        for(Terrain t : getMap().getTerrains()) {
            if(t.getIndex() != state.getThiefTerrain()) {

                float denialScore = 0;

                for(Land l : t.getLands()) {
                    byte occupation = state.getLand(l.getIndex());

                    int robingPlayerIndex = occupation / 10;
                    if(robingPlayerIndex == getPlayerIndex()) {
                        continue terrainLoop;
                    }

                    int figure = occupation % 10;
                    denialScore += figure * State.getDiceRatio(t.getDice());
                }

                if(denialScore > bestDenialScore) {
                    bestDenialScore = denialScore;
                    bestTerrain = t;
                }

            }
        }

        HashSet<Integer> potentialRobberies = new HashSet<>();
        for(Land l : bestTerrain.getLands()) {
            byte occupied = state.getLand(l.getIndex());
            if(occupied != 0) {
                int robbingPlayerIndex = occupied % 10;
                potentialRobberies.add(robbingPlayerIndex);
            }
        }

        List<RobbingPotential> robbingPotentials = new ArrayList<>();
        for(Integer pi : potentialRobberies) {
            int amount = 0;
            for(ResourceType rt : ResourceType.values()) {
                amount += state.getResource(pi, rt);
            }

            if(amount > 0) {
                robbingPotentials.add(new RobbingPotential(pi, state.getScore(pi)));
            }
        }

        robbingPotentials.sort((o1, o2) -> o2.getPlayerIndex() - o1.getPlayerIndex());

        int robbingPlayerIndex = -1;
        if(!robbingPotentials.isEmpty()) {
            robbingPlayerIndex = robbingPotentials.get(0).getPlayerIndex();
        }

        return new MoveRobber(getPlayerIndex(), bestTerrain.getIndex(), robbingPlayerIndex);
    }
}
