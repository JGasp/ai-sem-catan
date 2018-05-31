package si.fri.ai.catan.players;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.*;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.*;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class HumanPlayer extends Player {

    public HumanPlayer(Game game, int playerIndex) {
        super(game, playerIndex);
    }

    private float getResourceIncomeScore(State state, PlayerResAvgInc prai) {

        float average = prai.getAverageValue();
        float derivation = prai.getDerivation(average);

        float score = average - derivation;

        float wood = prai.get(ResourceType.WOOD);
        score += Math.min(wood, 0.01f);

        float clay = prai.get(ResourceType.CLAY);
        score += Math.min(clay, 0.01f);

        if(
                prai.get(ResourceType.CLAY) > 0 &&
                prai.get(ResourceType.WOOD) > 0 &&
                prai.get(ResourceType.WHEAT) > 0 &&
                prai.get(ResourceType.SHEEP) > 0
        ) {
            score += 0.5f;
        }

        if(state.isAnyRoadAvailable(getPlayerIndex())) {
            score += average / 3;
        } else {
            score += average / 4;
        }

        for(ResourceType rt : ResourceType.values()) {
            if(state.isResourceTrading(getPlayerIndex(), rt)) {
                score += prai.get(rt) / 2;
            }
        }

        return score;
    }

    private Land getBestLand(Collection<Land> lands, State state, PlayerResAvgInc playerResAvgInc) {

        float bestScore = -Float.MAX_VALUE;
        Land bestLand = null;

        for(Land l : lands) {

            PlayerResAvgInc tempResourceIncome = playerResAvgInc.copy();

            if(state.getLand(l.getIndex()) == 0) {
                for(Terrain t : l.getTerrains()) {
                    tempResourceIncome.addAvgIncome(t.getType(), State.getDiceRatio(t.getDice()));
                }
            }

            float score = getResourceIncomeScore(state, tempResourceIncome);
            if(score > bestScore) {
                bestScore = score;
                bestLand = l;
            }
        }

        return bestLand;
    }

    @Override
    public List<Move> playPlacingTurn(State state) {

        PlayerResAvgInc ri = new PlayerResAvgInc(state, getPlayerIndex());

        Land bestGlobalLand = getBestLand(getMap().getLands(), state, ri);

        List<Land> neighbours = new ArrayList<>();
        for(Road r : bestGlobalLand.getRoads()) {
            neighbours.add(r.getNeighbour(bestGlobalLand));
        }

        for(Terrain t : bestGlobalLand.getTerrains()) {
            ri.addAvgIncome(t.getType(), State.getDiceRatio(t.getDice()));
        }

        Land bestLocalLand = getBestLand(neighbours, state, ri);

        Road bestLocalRoad = bestGlobalLand.getConnectingRoad(bestLocalLand);

        PlacingVillage pv = new PlacingVillage(getPlayerIndex(), bestGlobalLand.getIndex());
        PlacingRoad pr = new PlacingRoad(getPlayerIndex(), bestLocalRoad.getIndex());

        List<Move> placing = new ArrayList<>();
        placing.add(pv);
        placing.add(pr);

        return placing;
    }

    @Override
    public List<Move> playTurn(State state) {
        State tempState = state.copy();

        List<Move> moves = new ArrayList<>();
        List<Move> buildVillageLocations = new ArrayList<>();

        buildCity(tempState, moves);
        buildVillage(tempState, moves, buildVillageLocations);

        if(buildVillageLocations.isEmpty()) {
            buildRoad(tempState, moves);
        }

        tradeRedundant(tempState, moves);

        return moves;
    }

    @Override
    public DropResources dropResources(State state) {
        DropResources dropResources = new DropResources(getPlayerIndex());

        int totalAmount = 0;

        List<ResourceAmount> resourceAmounts = new ArrayList<>();
        for(ResourceType rt : ResourceType.values()) {
            int amount = state.getResource(getPlayerIndex(), rt);
            if(amount > 0) {
                totalAmount += amount;
                resourceAmounts.add(new ResourceAmount(
                        rt,
                        amount,
                        state.getResourceAvgIncome(getPlayerIndex(), rt))
                );
            }
        }

        List<DropResources> drops = new ArrayList<>();

        if(totalAmount > 7) {
            int drop = totalAmount / 2;
            for(int d=0; d<drop; d++) {
                resourceAmounts.sort(ResourceAmount.compScore());

                ResourceAmount ra = resourceAmounts.get(0);
                ra.decAmount(1);
                if(ra.getAmount() == 0) resourceAmounts.remove(ra);

                dropResources.add(ra.getType(), (byte) 1);
            }
        }

        return dropResources;
    }

    @Override
    public MoveRobber moveRobber(State state) {

        Terrain bestTerrain = getMap().gt(8);
        float bestDenialScore = -Float.MAX_VALUE;

        terrainLoop:
        for(Terrain t : getMap().getTerrains()) {
            if(t.getType() != null && t.getIndex() != state.getThiefTerrain()) {
                float denialScore = 0;

                for(Land l : t.getLands()) {
                    byte occupation = state.getLand(l.getIndex());

                    if(occupation != 0) {
                        int robingPlayerIndex = occupation / 10;
                        if(robingPlayerIndex == getPlayerIndex()) {
                            continue terrainLoop;
                        }

                        int figure = occupation % 10;
                        denialScore += figure * State.getDiceRatio(t.getDice());
                    }
                }

                if(denialScore > bestDenialScore) {
                    bestDenialScore = denialScore;
                    bestTerrain = t;
                }
            }
        }

        HashSet<Integer> playersToSteal = new HashSet<>();
        for(Land l : bestTerrain.getLands()) {
            byte occupied = state.getLand(l.getIndex());
            if(occupied != 0) {
                int robbingPlayerIndex = occupied / 10;
                playersToSteal.add(robbingPlayerIndex);
            }
        }

        List<RobbingPotential> stealPotential = new ArrayList<>();
        for(Integer pi : playersToSteal) {
            PlayerResAmount pra = new PlayerResAmount(state, pi);
            if(pra.getTotalAmount() > 0) {
                stealPotential.add(new RobbingPotential(pi, state.getScore(pi)));
            }
        }

        stealPotential.sort((o1, o2) -> o2.getScore() - o1.getScore());

        int robbingPlayerIndex = -1;
        if(!stealPotential.isEmpty()) {
            robbingPlayerIndex = stealPotential.get(0).getPlayerIndex();
        }

        return new MoveRobber(getPlayerIndex(), bestTerrain.getIndex(), robbingPlayerIndex);
    }

    private List<TradeResources> tryTradingResources(State state, PlayerResAmount temp, PlayerResAmount missing) {
        List<TradeResources> tradeMoves = new ArrayList<>();
        List<ResourceAmount> resources = new ArrayList<>();

        boolean anyTrading = state.isAnyResourceTrading(getPlayerIndex());

        for(ResourceType rt : ResourceType.values()) {
            int amount = temp.get(rt);
            if(amount > 0) {
                resources.add(new ResourceAmount(rt, amount, state, getPlayerIndex()));
            }
        }

        int missingAmount = missing.getTotalAmount();

        dropLoop:
        while (missingAmount > 0) {
            resources.sort(ResourceAmount.compIncAmount());

            for(ResourceAmount ra : resources) {

                int ratio = 4;
                if(anyTrading) ratio = 3;
                if(ra.isTrading()) ratio = 2;


                if(ra.getAmount() >= ratio) {
                    missingAmount--;
                    temp.sub(ra.getType(), ratio);
                    ra.decAmount(ratio);

                    ResourceType tradeFor = missing.getNonZero();
                    missing.sub(tradeFor, 1);
                    temp.add(tradeFor, 1);

                    tradeMoves.add(new TradeResources(getPlayerIndex(), ra.getType(), tradeFor, ratio));
                    continue dropLoop;
                }
            }

            return null;
        }

        return tradeMoves;
    }

    private void buildCity(State state, List<Move> moves) {
        List<Move> buildCityMove = new ArrayList<>();

        if(state.isAnyCityAvailable(getPlayerIndex()) && state.getNumberOfVillages(getPlayerIndex()) > 0) {

            PlayerResAmount pra = new PlayerResAmount(state, getPlayerIndex());;
            PlayerResAmount missing = new PlayerResAmount();

            if(pra.get(ResourceType.IRON) >= 3) {
                pra.sub(ResourceType.IRON, 3);
            } else {
                missing.set(ResourceType.IRON, 3 - pra.get(ResourceType.IRON));
                pra.set(ResourceType.IRON, 0);
            }

            if(pra.get(ResourceType.WHEAT) >= 2) {
                pra.sub(ResourceType.WHEAT, 2);
            } else {
                missing.set(ResourceType.WHEAT, 2 - pra.get(ResourceType.WHEAT));
                pra.set(ResourceType.WHEAT, 0);
            }


            if(missing.getTotalAmount() > 0) {
                List<TradeResources> tradeMoves = tryTradingResources(state, pra, missing);
                if(tradeMoves == null) {
                    return; // Can't tryTradingResources resources to build city
                }
                buildCityMove.addAll(tradeMoves);
            }

            HashMap<Land, Byte> lands = new HashMap<>();
            for(Byte v=0; v<state.getNumberOfVillages(getPlayerIndex()); v++) {
                lands.put(getMap().gl(state.getVillagesLocation(getPlayerIndex(), v)), v);
            }

            PlayerResAvgInc prai = new PlayerResAvgInc(state, getPlayerIndex());
            Land bestLand = getBestLand(lands.keySet(), state, prai);

            byte bestVillage = lands.get(bestLand);
            buildCityMove.add(new BuildCity(getPlayerIndex(), bestVillage));

            for(Move m : buildCityMove) {
                m.make(getGame(), state);
                moves.add(m);
            }
        }
    }

    private void buildVillage(State state, List<Move> moves, List<Move> buildVillageLocations) {
        List<Move> buildVillageMove = new ArrayList<>();

        if(state.isAnyVillageAvailable(getPlayerIndex())) {


            Rule.getBuildVillageMoves(getMap(), state, getPlayerIndex(), buildVillageLocations);

            if(!buildVillageLocations.isEmpty()) {

                PlayerResAmount pra = new PlayerResAmount(state, getPlayerIndex());
                PlayerResAmount missing = new PlayerResAmount();

                ResourceType[] res = { ResourceType.SHEEP, ResourceType.CLAY, ResourceType.WOOD, ResourceType.WHEAT };
                for(ResourceType rt : res) {
                    if(pra.get(rt) >= 1) {
                        pra.sub(rt, 1);
                    } else {
                        missing.add(rt, 1);
                        pra.set(rt, 0);
                    }
                }

                if(missing.getTotalAmount() > 0) {
                    List<TradeResources> tradeMoves = tryTradingResources(state, pra, missing);
                    if(tradeMoves == null) {
                        return; // Can't tryTradingResources resources to build village
                    }
                    buildVillageMove.addAll(tradeMoves);
                }

                HashSet<Land> lands = new HashSet<>();
                for(Move m : buildVillageLocations) {
                    lands.add(getMap().gl(((BuildVillage) m).getLandIndex()));
                }

                PlayerResAvgInc prai = new PlayerResAvgInc(state, getPlayerIndex());
                Land bestLand = getBestLand(lands, state, prai);

                buildVillageMove.add(new BuildVillage(getPlayerIndex(), bestLand.getIndex()));

                for(Move m : buildVillageMove) {
                    m.make(getGame(), state);
                    moves.add(m);
                }

            }
        }
    }

    private void buildRoad(State state, List<Move> moves) {
        List<Move> buildRoadMove = new ArrayList<>();

        if(state.isAnyRoadAvailable(getPlayerIndex())) {

            PlayerResAmount pra = new PlayerResAmount(state, getPlayerIndex());
            PlayerResAmount missing = new PlayerResAmount();

            ResourceType[] res = { ResourceType.CLAY, ResourceType.WOOD};
            for(ResourceType rt : res) {
                if(pra.get(rt) >= 1) {
                    pra.sub(rt, 1);
                } else {
                    missing.add(rt, 1);
                }
            }

            if(missing.getTotalAmount() > 0) {
                List<TradeResources> tradeMoves = tryTradingResources(state, pra, missing);
                if(tradeMoves == null) {
                    return; // Can't trade resources to build road
                }
                buildRoadMove.addAll(tradeMoves);
            }

            List<Move> buildRoadLocations = new ArrayList<>();
            Rule.getLocationsToBuildRoad(getMap(), state, getPlayerIndex(), buildRoadLocations);

            List<Move> buildRoadForVillage = buildRoadLocations.stream().filter(move -> {
                Road r = getMap().gr(((BuildRoad) move).getRoadIndex());
                return r.areLandsEmpty(state);
            }).collect(Collectors.toList());


            HashMap<Land, Road> candidates = new HashMap<>();
            for(Move m : buildRoadForVillage) {
                Road r = getMap().gr(((BuildRoad) m).getRoadIndex());

                if(r.getFrom().isPlayerConnected(state, getPlayerIndex())) {
                    byte landIndex = r.getTo().getIndex();
                    if(Rule.canBuildVillageOnLand(getMap(),state, landIndex)) {
                        candidates.put(getMap().gl(landIndex), r);
                    }
                } else {
                    byte landIndex = r.getFrom().getIndex();
                    if(Rule.canBuildVillageOnLand(getMap(),state, landIndex)) {
                        candidates.put(getMap().gl(landIndex), r);
                    }
                }
            }

            if(candidates.isEmpty()) {
                for(Move m : buildRoadLocations) {
                    Road r = getMap().gr(((BuildRoad) m).getRoadIndex());

                    Land notConnectedLand;
                    if(r.getFrom().isPlayerConnected(state, getPlayerIndex())) {
                        notConnectedLand = r.getTo();
                    } else {
                        notConnectedLand = r.getFrom();
                    }

                    byte occupied = state.getLand(notConnectedLand.getIndex());
                    if(occupied == 0) {
                        for(Road nr : notConnectedLand.getRoads()) {
                            if(nr != r) {
                                byte nrOccupied = state.getRoad(nr.getIndex());
                                if(nrOccupied == 0) {
                                    Land nnl = nr.getNeighbour(notConnectedLand);
                                    candidates.put(nnl, r);
                                }
                            }
                        }
                    }
                }
            }

            PlayerResAvgInc prai = new PlayerResAvgInc(state, getPlayerIndex());
            Land bestLand = getBestLand(candidates.keySet(), state, prai);
            Road bestRoad = candidates.get(bestLand);

            buildRoadMove.add(new BuildRoad(getPlayerIndex(), bestRoad.getIndex()));

            for(Move m : buildRoadMove) {
                m.make(getGame(), state);
                moves.add(m);
            }
        }
    }

    private void tradeRedundant(State state, List<Move> moves) {
        List<TradeResources> tradingMoves = new ArrayList<>();

        PlayerResAmount pra = new PlayerResAmount(state, getPlayerIndex());
        if(pra.getTotalAmount() > 7) {

            List<ResourceAmount> amounts = new ArrayList<>();

            for(ResourceType rt : ResourceType.values()) {
                int amount = pra.get(rt);
                if(amount > 0) {
                    amounts.add(new ResourceAmount(rt, amount, state, getPlayerIndex()));
                }
            }

            boolean anyTrading = state.isAnyResourceTrading(getPlayerIndex());

            if(pra.getTotalAmount() > 7) {
                amounts.sort(ResourceAmount.compScore());

                for(ResourceAmount ra : amounts) {
                    int ratio = 4;
                    if(anyTrading) ratio = 3;
                    if(ra.isTrading()) ratio = 2;


                    if(ra.getAmount() > ratio) {
                        pra.sub(ra.getType(), ratio);

                        ra.setAmount(ra.getAmount() - ratio);

                        ResourceAmount tradeFor = amounts.get(amounts.size() - 1);
                        tradeFor.setAmount(tradeFor.getAmount() + 1);

                        pra.add(tradeFor.getType(), 1);

                        tradingMoves.add(new TradeResources(getPlayerIndex(), ra.getType(), tradeFor.getType(), ratio));
                    }
                }
            }

        }

        for(TradeResources tr : tradingMoves) {
            tr.make(getGame(), state);
            moves.add(tr);
        }
    }

}
