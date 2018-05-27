package si.fri.ai.catan;

import si.fri.ai.catan.gui.MapPanel;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;
import si.fri.ai.catan.rules.moves.DropResources;
import si.fri.ai.catan.rules.moves.MoveRobber;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.List;

public class Game {

    private Map map;
    private Rule rule;
    private MapPanel mapPanel;

    private List<Player> playerList;

    public Game() {
        map = new Map();
        rule = new Rule(this);
        mapPanel = new MapPanel(map);


    }

    public Map getMap() {
        return map;
    }

    public Rule getRule() {
        return rule;
    }

    public int numberOfPLayers() {
        return playerList.size();
    }

    public void game() {


        mainGameLoop();
    }

    public void mainGameLoop() {

        State state = new State();

        int playerIndex = 0;
        int winnerIndex = getRule().getWinner(state);

        while(winnerIndex != -1) {
            int dice = rule.throwDice();

            Player p = playerList.get(playerIndex);

            if(dice == 7) {
                for(int i=0; i<playerList.size(); i++) {
                    List<DropResources> drop = p.dropResources(state);
                    for(DropResources m : drop) {
                        m.make(state);
                    }
                }

                MoveRobber m = p.moveRobber(state);
                m.make(state);
            } else {
                for(int i=0; i<playerList.size(); i++) {
                    state.updateResourceAmount(dice, i);
                }
            }

            List<Move> playerTurn = p.playTurn(state);
            for(Move m : playerTurn) {
                m.make(state);
            }

            playerIndex++;
            if(playerIndex >= playerList.size()) {
                playerIndex = 0;
            }
        }

    }
}
