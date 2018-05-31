package si.fri.ai.catan.players.monteCarlo;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.InfoMessage;
import si.fri.ai.catan.dto.PlayerResAmount;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.players.RandomPlayer;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingRoad;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MonteCarloSimulation {

    private static final int maxRounds = 1000;
    private static final int duration = 30 * 1000;


    private byte thisPlayerIndex;
    private Game game;

    private EntryPoint entryPoint;
    private State initialState;

    private Player[] players;

    private HashMap<MCNode, MCScore> memorized;
    private List<MCNode> initialMoves;

    public MonteCarloSimulation(Game game, int playerIndex) {
        this.game = game;
        this.thisPlayerIndex = (byte) playerIndex;

        initPlayers(game);
    }

    private void initPlayers(Game game) {
        players = new Player[State.NUMBER_OF_PLAYERS];
        for(int p=0; p<players.length; p++) {
            players[p] = new RandomPlayer(game, p);
        }
    }


    public List<Move> getTurnMoves(State state) {
        this.entryPoint = EntryPoint.TURN;
        this.initialState = state;

        memorized = new HashMap<>();

        List<Move> allMoves = Rule.getAllMoves(game.getMap(), initialState, thisPlayerIndex);
        if(allMoves.isEmpty()) {
            return allMoves; // Return 0 moves, end turn
        }

        long now = System.currentTimeMillis();
        long end = now + duration;

        while (now < end) {
            runSimulation();
            now = System.currentTimeMillis();
        }

        List<Move> doMoves = new ArrayList<>();

        State tempState = state.copy();
        List<MCNode> currentMoves = initialMoves;
        while (true) {

            double bestScore = -1;
            MCNode bestMove = currentMoves.get(0);
            for(MCNode m : currentMoves) {
                MCScore score = memorized.get(m);
                if(score != null) {
                    if(bestScore < score.getWinRatio()) {
                        bestMove = score.getNode();
                    }
                }
            }

            if(bestMove.getMove() == null) {
                break;
            } else {
                doMoves.add(bestMove.getMove());
                bestMove.getMove().make(game, tempState);

                currentMoves.clear();
                Rule.getAllMoves(game.getMap(), tempState, thisPlayerIndex).forEach(move -> {
                    currentMoves.add(new MCNode(move, tempState));
                });
            }
        }

        return doMoves;
    }

    public List<Move> getPlacingMoves(State state) {
        this.entryPoint = EntryPoint.PLACING_TURN;
        this.initialState = state;

        memorized = new HashMap<>();


        long now = System.currentTimeMillis();
        long end = now + duration;

        while (now < end) {
            runSimulation();
            now = System.currentTimeMillis();
        }


        State tempState = state.copy();
        List<MCNode> currentMoves = initialMoves;

        double bestScore = -1;
        MCNode bestVillagePlacingMove = currentMoves.get(0);
        for(MCNode m : currentMoves) {
            MCScore score = memorized.get(m);
            if(score != null) {
                if(bestScore < score.getWinRatio()) {
                    bestVillagePlacingMove = score.getNode();
                }
            }
        }
        bestVillagePlacingMove.getMove().make(game, tempState);

        PlacingVillage pv = (PlacingVillage) bestVillagePlacingMove.getMove();

        currentMoves.clear();
        for(Road r : game.getMap().gl(pv.getLandIndex()).getRoads()) {
            currentMoves.add(new MCNode(new PlacingRoad(thisPlayerIndex, r.getIndex()), tempState));
        }

        bestScore = -1;
        MCNode bestRoadPlacingMove = currentMoves.get(0);
        for(MCNode m : currentMoves) {
            MCScore score = memorized.get(m);
            if(score != null) {
                if(bestScore < score.getWinRatio()) {
                    bestRoadPlacingMove = score.getNode();
                }
            }
        }

        List<Move> doMoves = new ArrayList<>();
        doMoves.add(bestVillagePlacingMove.getMove());
        doMoves.add(bestRoadPlacingMove.getMove());
        return doMoves;
    }

    public DropResources getDropResourceMove(State state) {
        this.entryPoint = EntryPoint.DROP_RESOURCES;
        this.initialState = state;

        memorized = new HashMap<>();


        long now = System.currentTimeMillis();
        long end = now + duration;

        while (now < end) {
            runSimulation();
            now = System.currentTimeMillis();
        }


        List<MCNode> currentMoves = initialMoves;
        double bestScore = -1;
        MCNode bestDropResourceMove = currentMoves.get(0);
        for(MCNode m : currentMoves) {
            MCScore score = memorized.get(m);
            if(score != null) {
                if(bestScore < score.getWinRatio()) {
                    bestDropResourceMove = score.getNode();
                }
            }
        }


        return (DropResources) bestDropResourceMove.getMove();
    }

    public MoveRobber getMoveRobberMove(State state) {
        this.entryPoint = EntryPoint.MOVE_ROBBER;
        this.initialState = state;

        memorized = new HashMap<>();


        long now = System.currentTimeMillis();
        long end = now + duration;

        while (now < end) {
            runSimulation();
            now = System.currentTimeMillis();
        }

        List<MCNode> currentMoves = initialMoves;
        double bestScore = -1;
        MCNode bestDropResourceMove = currentMoves.get(0);
        for(MCNode m : currentMoves) {
            MCScore score = memorized.get(m);
            if(score != null) {
                if(bestScore < score.getWinRatio()) {
                    bestDropResourceMove = score.getNode();
                }
            }
        }


        return (MoveRobber) bestDropResourceMove.getMove();
    }



    private void runSimulation() {
        HashSet<MCNode> visitedStates = new HashSet<>();
        State state = initialState.copy();

        int remainingPlacingTurns = runSimulationEntry(state, visitedStates);
        byte winner = runSimulationToEnd(state, remainingPlacingTurns);

        for(MCNode node : visitedStates) {
            MCScore exist = memorized.get(node);
            if(exist != null) {
                exist.incPlays();
                if(winner == thisPlayerIndex) {
                    exist.incWins();
                }
            }
        }
    }

    private int runSimulationEntry(State state, HashSet<MCNode> visitedStates) {
        int remainingPlacingTurns = 0;

        if(entryPoint == EntryPoint.PLACING_TURN) {
            int num = state.getNumberOfVillages(thisPlayerIndex);
            if(num == 0) {
                remainingPlacingTurns += State.NUMBER_OF_PLAYERS;
            }
            remainingPlacingTurns += State.NUMBER_OF_PLAYERS - thisPlayerIndex - 1;

            runSimulateEntryPlacing(state, visitedStates);
        } else {
            if(entryPoint == EntryPoint.DROP_RESOURCES) {
                runSimulateEntryDropResources(state, visitedStates);
            } else if(entryPoint == EntryPoint.MOVE_ROBBER) {
                runSimulateRobber(state, visitedStates);
            } else if(entryPoint == EntryPoint.TURN) {
                runSimulateTurn(state, visitedStates);
            }
        }

        return remainingPlacingTurns;
    }


    private void runSimulateTurn(State state, HashSet<MCNode> visitedStates) {

        MCNode nextMove = playTurnMove(state);
        while (nextMove.getMove() != null) {
            nextMove.getMove().make(game, state);
            visitedStates.add(new MCNode(null, state));

            nextMove = playTurnMove(state);
        }

        visitedStates.add(new MCNode(null, state));
    }

    private MCNode playTurnMove(State state) {

        if(initialMoves != null && state.equals(initialState)) {
            List<MCScore> allMemorized = new ArrayList<>();

            int totalPlays = 0;
            for(MCNode node : initialMoves) {
                MCScore score = memorized.get(node);
                if (score != null) {
                    totalPlays += score.getPlays();
                    allMemorized.add(score);
                }
            }

            return pickMove(initialMoves, allMemorized, totalPlays);
        } else {
            List<Move> allRaw = Rule.getAllMoves(game.getMap(), state, thisPlayerIndex);

            int totalPlays = 0;
            if(allRaw.isEmpty()) {
                return new MCNode(null, state);
            } else {

                List<MCNode> allMoves = new ArrayList<>();
                List<MCScore> allMemorized = new ArrayList<>();

                for (Move m : allRaw) {
                    MCNode node = new MCNode(m, state);
                    allMoves.add(node);

                    MCScore score = memorized.get(node);
                    if (score != null) {
                        totalPlays += score.getPlays();
                        allMemorized.add(score);
                    }
                }

                if(state.equals(initialState)) {
                    initialMoves = allMoves;
                }

                return pickMove(allMoves, allMemorized, totalPlays);
            }
        }
    }


    private void runSimulateRobber(State state, HashSet<MCNode> visitedStates) {
        MCNode mr = playRobber(state);
        mr.getMove().make(game, state);
        visitedStates.add(mr);

        runSimulateTurn(state, visitedStates);
    }

    private MCNode playRobber(State state) {
        List<MCScore> allMemorized = new ArrayList<>();
        List<MCNode> allMoves = new ArrayList<>();

        int totalPlays = 0;
        byte robberTerrainIndex = state.getThiefTerrain();
        for(Terrain t : game.getMap().getTerrains()) {
            if(t.getIndex() != robberTerrainIndex) {

                HashSet<Integer> playersToRob = new HashSet<>();
                for(Land l : t.getLands()) {
                    byte occupied = state.getLand(l.getIndex());
                    if(occupied != 0) {
                        int playerIndex = occupied / 10;
                        if(playerIndex != thisPlayerIndex) {
                            playersToRob.add(playerIndex);
                        }
                    }
                }
                if(playersToRob.size() == 0) playersToRob.add(-1); // Add no one to rob

                for(Integer pi : playersToRob) {
                    MoveRobber mr = new MoveRobber(thisPlayerIndex, t.getIndex(), pi);

                    MCNode node = new MCNode(mr, state);
                    allMoves.add(node);

                    MCScore score = memorized.get(node);
                    if(score != null) {
                        totalPlays += score.getPlays();
                        allMemorized.add(score);
                    }
                }
            }
        }

        return pickMove(allMoves, allMemorized, totalPlays);
    }


    private void runSimulateEntryDropResources(State state, HashSet<MCNode> visitedStates) {

        MCNode drop = playDropResource(state);
        drop.getMove().make(game, state);
        visitedStates.add(drop);

        for (int pi = thisPlayerIndex + 1; pi < State.NUMBER_OF_PLAYERS; pi++) {
            DropResources dr = players[pi].dropResources(state);
            dr.make(game, state);
        }

        Player thisPlayer = players[thisPlayerIndex];
        Player currentPlayer = players[state.getCurrentPlayerIndex()];

        if(currentPlayer == thisPlayer) {
            runSimulateRobber(state, visitedStates);
        } else {
            MoveRobber mr = currentPlayer.moveRobber(state);
            mr.make(game, state);

            List<Move> pt = currentPlayer.playTurn(state);
            for(Move m : pt) {
                m.make(game, state);
            }
        }
    }

    private MCNode playDropResource(State state) {
        List<MCScore> allMemorized = new ArrayList<>();
        List<MCNode> allMoves = generateAllPossibleResourceDrops(state);

        int totalPlays = 0;
        for(MCNode node : allMoves) {
            MCScore nodeMem = memorized.get(node);
            if(nodeMem != null) {
                totalPlays += nodeMem.getPlays();
                allMemorized.add(nodeMem);
            }
        }

        return pickMove(allMoves, allMemorized, totalPlays);
    }

    private List<MCNode> generateAllPossibleResourceDrops(State state) {
        List<MCNode> allMoves = new ArrayList<>();

        PlayerResAmount pra = new PlayerResAmount(state, thisPlayerIndex);
        int drop = pra.getTotalAmount() / 2;
        DropResources dr = new DropResources(thisPlayerIndex);

        generateAllPossibleResourceDrops(state, pra, drop, dr, ResourceType.getRt(0), allMoves);

        return allMoves;
    }

    private void generateAllPossibleResourceDrops(State state, PlayerResAmount pra, int drop, DropResources dr, ResourceType type, List<MCNode> generatedNodes) {
        if(drop == dr.getTotalAmount()) {
            generatedNodes.add(new MCNode(dr, state));
        } else {
            ResourceType next = ResourceType.getRt(type.getIndex() + 1);
            if(next != null) {
                generateAllPossibleResourceDrops(state, pra, drop, dr.copy(), next, generatedNodes);
            }

            if(pra.get(type) > dr.get(type)) {
                dr.add(type, (byte) 1);
                generateAllPossibleResourceDrops(state, pra, drop, dr, type, generatedNodes);
            }
        }
    }


    private void runSimulateEntryPlacing(State state, HashSet<MCNode> visitedStates) {
        MCNode placeVillage = playPlacingVillageTurn(state);
        placeVillage.getMove().make(game, state);
        visitedStates.add(placeVillage);

        MCNode placeRoad = playPlacingRoadTurn(state, (PlacingVillage) placeVillage.getMove());
        placeRoad.getMove().make(game, state);
        visitedStates.add(placeRoad);
    }

    private MCNode playPlacingRoadTurn(State state, PlacingVillage pv) {
        List<MCNode> allMoves = new ArrayList<>();
        List<MCScore> allMemorized = new ArrayList<>();

        int totalPlays = 0;
        for(Road r : game.getMap().gl(pv.getLandIndex()).getRoads()) {
            byte occupied = state.getLand(r.getIndex());

            if(occupied == 0) {
                PlacingRoad pr = new PlacingRoad(thisPlayerIndex, r.getIndex());

                MCNode node = new MCNode(pr, state);
                allMoves.add(node);

                MCScore nodeMem = memorized.get(node);
                if(nodeMem != null) {
                    totalPlays += nodeMem.getPlays();
                    allMemorized.add(nodeMem);
                }
            }
        }

        return pickMove(allMoves, allMemorized, totalPlays);
    }

    private MCNode playPlacingVillageTurn(State state) {
        List<MCNode> allMoves = new ArrayList<>();
        List<MCScore> allMemorized = new ArrayList<>();

        int totalPlays = 0;
        for(Land l : game.getMap().getLands()) {
            byte occupied = state.getLand(l.getIndex());

            if(occupied == 0) {
                PlacingVillage pv = new PlacingVillage(thisPlayerIndex, l.getIndex());

                MCNode node = new MCNode(pv, state);
                allMoves.add(node);

                MCScore nodeMem = memorized.get(node);
                if(nodeMem != null) {
                    totalPlays += nodeMem.getPlays();
                    allMemorized.add(nodeMem);
                }
            }
        }

        return pickMove(allMoves, allMemorized, totalPlays);
    }


    private MCNode pickMove(List<MCNode> allMoves, List<MCScore> allMemorized, int totalPlays) {
        if(allMoves.size() == allMemorized.size()) {
            final int finalTotalPlays = totalPlays;

            allMemorized.sort((o1, o2) -> {
                double diff = o2.getScore(finalTotalPlays) - o1.getScore(finalTotalPlays);
                if(diff > 0) return 1;
                if(diff < 0) return -1;
                return 0;
            });

            return allMemorized.get(0).getNode();
        } else {
            int random = Rule.random.nextInt(allMoves.size());

            MCNode node = allMoves.get(random);
            MCScore score = memorized.get(node);

            if(score == null) {
                memorized.put(node, new MCScore(node));
            }

            return node;
        }
    }


    private byte runSimulationToEnd(State state, int remainingPlacingTurns) {
        // Rest of simulation
        byte winner = -1;
        int rounds = 0;

        while (rounds < maxRounds) {
            rounds++;

            int dice = Rule.throwDice();
            byte playerIndex = state.getCurrentPlayerIndex();

            Player p = players[playerIndex];

            if(remainingPlacingTurns > 0) {
                remainingPlacingTurns--;

                List<Move> moves = p.playPlacingTurn(state);
                for(Move m : moves) {
                    m.make(game, state);
                }
            } else {
                if(dice == 7) {
                    // Drop excessive resources and move thief
                    for(int pi=0; pi<players.length; pi++) {
                        DropResources dr = p.dropResources(state);
                        dr.make(game, state);
                    }

                    MoveRobber m = p.moveRobber(state);
                    m.make(game, state);
                } else {
                    // Add all resources
                    for(int pi=0; pi<players.length; pi++) {
                        for(ResourceType rt: ResourceType.values()) {
                            byte amount = state.getResourceIncome(pi, rt, dice);
                            if(amount > 0) {
                                state.addResource(pi, rt, amount);
                            }
                        }

                    }
                }

                List<Move> playerTurn = p.playTurn(state);
                for(Move m : playerTurn) {
                    m.make(game, state);
                }

                if(Rule.isWinner(state, playerIndex)) {
                    winner = playerIndex;
                    break;
                }
            }
        }

        return winner;
    }

}
