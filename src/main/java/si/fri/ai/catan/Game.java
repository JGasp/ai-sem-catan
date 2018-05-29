package si.fri.ai.catan;

import si.fri.ai.catan.gui.MapPanel;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.players.HumanPlayer;
import si.fri.ai.catan.players.RandomPlayer;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Game {

    private Map map;
    private Rule rule;
    private MapPanel mapPanel;

    private Player[] playerList;

    private State state;

    public Game() {
        map = new Map();
        rule = new Rule(this);
        mapPanel = new MapPanel(map);

        state = new State();
        initPlayers();
    }

    public void initPlayers() {
        playerList = new Player[State.NUMBER_OF_PLAYERS];
        playerList[0] = new HumanPlayer(this, 0);
        playerList[1] = new HumanPlayer(this,1);
    }


    public void start() {
        placementGameLoop();
        mainGameLoop();
    }

    private void placementGameLoop() {

        for(int t=0; t<2; t++) {
            for(Player p : playerList) {
                 PlacingVillage m = p.playPlacingTurn(state);
                 m.make(this, state);

                 updateGui(m.toString());
            }
        }
    }

    private void mainGameLoop() {
        int round = 0;
        int playerIndex = 0;

        while(true) {
            round++;
            int dice = rule.throwDice();

            String roundInfo = String.format("Round %d \t Dice: %d \t Player: %d \n", round, dice, playerIndex);
            updateGui(roundInfo);

            Player p = playerList[playerIndex];

            if(dice == 7) {
                for(int i=0; i<playerList.length; i++) {
                    List<DropResources> drop = p.dropResources(state);
                    for(DropResources m : drop) {
                        m.make(this, state);

                        updateGui(m.toString());
                    }
                }

                MoveRobber m = p.moveRobber(state);
                m.make(this, state);
            } else {
                for(int i=0; i<playerList.length; i++) {
                    state.updateResourceAmount(dice, i);
                }
            }

            List<Move> playerTurn = p.playTurn(state);
            for(Move m : playerTurn) {
                m.make(this, state);

                updateGui(m.toString());
            }


            if(!rule.isWinner(state, playerIndex)) {
                break;
            }

            if(round > 10000) {
                System.out.println("Game took to long");
                return;
            }

            playerIndex++;
            if(playerIndex >= playerList.length) {
                playerIndex = 0;
            }
        }

        System.out.println("Winner was player: " + playerIndex);
        updateGui(null);
    }


    public Map getMap() {
        return map;
    }

    public Rule getRule() {
        return rule;
    }

    public int numberOfPLayers() {
        return playerList.length;
    }

    private void updateGui(String roundInfo){
        mapPanel.updateState(state, roundInfo);
        waitForSpace();
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
