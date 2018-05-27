package si.fri.ai.catan;

import si.fri.ai.catan.gui.MapPanel;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.players.RandomPlayer;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.PlacingVillage;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.List;

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
        playerList[0] = new RandomPlayer(this, 0);
        playerList[1] = new RandomPlayer(this,1);
    }


    public void start() {
        placementGameLoop();
        mainGameLoop();
    }

    private void placementGameLoop() {

        for(int t=0; t<2; t++) {
            for(Player p : playerList) {
                 PlacingVillage m = p.playPlacingTurn(state);
                 m.make(state);

                 updateGui();
            }
        }
    }

    private void mainGameLoop() {

        State state = new State();

        int playerIndex = 0;
        int winnerIndex = rule.getWinner(state);

        while(winnerIndex != -1) {
            int dice = rule.throwDice();

            Player p = playerList[playerIndex];

            if(dice == 7) {
                for(int i=0; i<playerList.length; i++) {
                    List<DropResources> drop = p.dropResources(state);
                    for(DropResources m : drop) {
                        m.make(state);
                    }
                }

                MoveRobber m = p.moveRobber(state);
                m.make(state);
            } else {
                for(int i=0; i<playerList.length; i++) {
                    state.updateResourceAmount(dice, i);
                }
            }

            List<Move> playerTurn = p.playTurn(state);
            for(Move m : playerTurn) {
                m.make(state);
            }

            playerIndex++;
            if(playerIndex >= playerList.length) {
                playerIndex = 0;
            }

            updateGui();
        }

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

    private void updateGui(){
        mapPanel.updateState(state);


    }
}
