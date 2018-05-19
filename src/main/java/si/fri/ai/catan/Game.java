package si.fri.ai.catan;

import si.fri.ai.catan.gui.MapPanel;
import si.fri.ai.catan.map.Map;

public class Game {

    private Map map;
    private MapPanel mapPanel;



    public Game() {
        map = new Map();
        mapPanel = new MapPanel(map);
    }

    public Map getMap() {
        return map;
    }
}
