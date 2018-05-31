package si.fri.ai.catan;

import si.fri.ai.catan.dto.InfoMessage;
import si.fri.ai.catan.gui.MapPanel;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.players.HumanPlayer;
import si.fri.ai.catan.players.RandomPlayer;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.players.monteCarlo.MonteCarloPlayer;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Game {

    private boolean displayGui = true;

    private Map map;
    private MapPanel mapPanel;

    private Player[] playerList;
    private State state;

    private Byte winnerIndex = null;

    public Game() {
        this(true);
    }

    public Game(boolean displayGui) {
        map = new Map();
        this.displayGui = displayGui;

        if(displayGui) {
            mapPanel = new MapPanel(map);
        }

        state = new State();
        initPlayers();
    }

    public void initPlayers() {
        playerList = new Player[State.NUMBER_OF_PLAYERS];
        playerList[0] = new RandomPlayer(this, 0);
        playerList[1] = new MonteCarloPlayer(this,1);
        /*playerList[2] = new HumanPlayer(this,2);
        playerList[3] = new HumanPlayer(this,3);*/
    }


    public void start() {
        placementGameLoop();
        mainGameLoop();
    }


    private void placementGameLoop() {

        for(int t=0; t<2; t++) {
            for(Player p : playerList) {
                 List<Move> moves = p.playPlacingTurn(state);
                 for(Move m : moves) {
                     m.make(this, state);

                     updateGui(new InfoMessage(m.toString(), p.getPlayerIndex()));
                 }
            }
        }
    }

    private void mainGameLoop() {

        while(true) {

            int dice = Rule.throwDice();
            byte playerIndex = state.getCurrentPlayerIndex();

            String roundInfo = String.format("Round %d \t Dice: %d \t Player: %d \n", state.getRound(), dice, playerIndex);
            updateGui(new InfoMessage(roundInfo), false);

            Player p = playerList[playerIndex];

            if(dice == 7) {
                // Drop excessive resources and move thief
                for(int pi=0; pi<playerList.length; pi++) {
                    DropResources drop = p.dropResources(state);
                    drop.make(this, state);

                    updateGui(new InfoMessage(drop.toString(), pi));
                }

                MoveRobber m = p.moveRobber(state);
                m.make(this, state);

                updateGui(new InfoMessage(m.toString(), playerIndex) );
            } else {
                // Add all resources
                for(int pi=0; pi<playerList.length; pi++) {
                    for(ResourceType rt: ResourceType.values()) {
                        byte amount = state.getResourceIncome(pi, rt, dice);
                        if(amount > 0) {
                            state.addResource(pi, rt, amount);

                            String info = String.format("[%d] got [%d] x [%s]", pi, amount, rt.name());
                            updateGui(new InfoMessage(info, pi), false);
                        }
                    }

                }
            }

            List<Move> playerTurn = p.playTurn(state);
            for(Move m : playerTurn) {
                m.make(this, state);

                updateGui(new InfoMessage(m.toString(), playerIndex));
            }

            if(Rule.isWinner(state, playerIndex)) {
                winnerIndex = playerIndex;
                String info = "Winner was player: " + playerIndex;
                updateGui(new InfoMessage(info, playerIndex));
                break;
            }

            if(state.getRound() > 10000) {
                String info = "Game took to long";
                updateGui(new InfoMessage(info));
                return;
            }

            updateGui(null);

            state.nextPlayerIndex();
        }

    }




    public Map getMap() {
        return map;
    }

    public Byte getWinnerIndex() {
        return winnerIndex;
    }

    public void updateGui(InfoMessage roundInfo) {
        updateGui(roundInfo, true);
    }

    public void updateGui(InfoMessage roundInfo, boolean wait) {
        if(displayGui) {
            mapPanel.updateState(state, roundInfo);

            if(wait) {
                waitForSpace();
            }
        }
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }




    private static boolean pressed = false;
    public static void waitForSpace() {
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            KeyEventDispatcher dispatcher = new KeyEventDispatcher() {

                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (pressed) {
                            pressed = false;
                        } else {
                            latch.countDown();
                            pressed = true;
                        }
                    }
                    return false;
                }
            };
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
            latch.await();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
