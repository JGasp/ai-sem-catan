package si.fri.ai.catan.gui;

import si.fri.ai.catan.State;
import si.fri.ai.catan.dto.InfoMessage;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.map.parts.positon.Point;
import si.fri.ai.catan.map.parts.positon.Vector;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.TimerTask;
import java.util.Timer;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MapPanel extends JPanel {

    private static final Color[] playerColors = { Color.MAGENTA, Color.WHITE, Color.RED, Color.BLUE };

    private static final int Y_OFFSET = 500;
    private static final int X_OFFSET = 900;

    private static final Font statsFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);
    private static final Font mapFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    private ScheduledExecutorService executor;

    private JFrame frame;

    private Map map;
    private State state;

    private int maxInfoSize = 30;
    private List<InfoMessage> roundInfo;

    private int timerSeconds = 0;

    public MapPanel(Map map) {
        this.map = map;

        roundInfo = new CopyOnWriteArrayList<>();

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1920, 1080));
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    private void addInfo(InfoMessage info) {
        if(roundInfo.size() > maxInfoSize) {
            roundInfo.remove(0);
        }
        roundInfo.add(info);
    }

    public void updateState(State state, InfoMessage roundInfo) {
        this.state = state;

        if(roundInfo != null) addInfo(roundInfo);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(mapFont);

        g.setColor(Color.CYAN);
        g.fillRect(0,0, 1920, 1080);

        paintInfo(g);
        paintTimer(g);

        for(Terrain t : map.getTerrains()) {
            paintTerrain(g, t);
        }

        if(state != null) {
            g.setFont(statsFont);

            g.setColor(Color.BLACK);
            char[] textIndex = "RT \t Wood \t Iron \t Clay \t Wheat \t Sheep".toCharArray();
            g.drawChars(textIndex, 0, textIndex.length, 50, 30);

            int yOffsetBottom = Y_OFFSET * 2 - State.NUMBER_OF_PLAYERS * 15 - 60;
            int xOffsetRight = X_OFFSET * 2 - 400;
            textIndex = "Score \t Roads \t Villages \t City".toCharArray();
            g.drawChars(textIndex, 0, textIndex.length, xOffsetRight, yOffsetBottom);

            paintThief(g, map.gt(state.getThiefTerrain()).getPoint());

            for(int pi=0; pi<State.NUMBER_OF_PLAYERS; pi++) {

                paintStats(g, state, pi);

                int numberOfRoads = state.getNumberOfRoads(pi);
                for(int ri=0; ri<numberOfRoads; ri++) {
                    Road r = map.gr(state.getRoadLocation(pi, ri));
                    paintRoadFigure(g, r.getFrom().getPoint(), r.getTo().getPoint(), pi);
                }

                int numberOfVillages = state.getNumberOfVillages(pi);
                for(int vi=0; vi<numberOfVillages; vi++) {
                    Land l = map.gl(state.getVillagesLocation(pi, vi));
                    paintVillageFigure(g, l.getPoint(), pi);
                }

                int numberOfCities = state.getNumberOfCities(pi);
                for(int ci=0; ci<numberOfCities; ci++) {
                    Land l = map.gl(state.getCityLocation(pi, ci));
                    paintCityFigure(g, l.getPoint(), pi);
                }
            }

            g.setFont(mapFont);
        }

        for(Land l : map.getLands()) {
            paintLand(g, l);
        }

        for(Road r : map.getRoads()) {
            paintRoad(g, r);
        }

    }

    private void paintStats(Graphics g, State state, int playerIndex) {
        g.setColor(playerColors[playerIndex]);


        int roads = state.getNumberOfRoads(playerIndex);
        int villages = state.getNumberOfVillages(playerIndex);
        int cities = state.getNumberOfCities(playerIndex);
        int score = state.getScore(playerIndex);

        int yOffsetBottom = Y_OFFSET * 2 - (State.NUMBER_OF_PLAYERS - playerIndex) * 15 - 45;
        int xOffsetRight = X_OFFSET * 2 - 400;
        char[] textIndex = String.format("%5d \t %5d \t %7d \t %4d", score, roads, villages, cities).toCharArray();
        g.drawChars(textIndex, 0, textIndex.length, xOffsetRight, yOffsetBottom);


        int wood = state.getResource(playerIndex, ResourceType.WOOD);
        int iron = state.getResource(playerIndex, ResourceType.IRON);
        int clay = state.getResource(playerIndex, ResourceType.CLAY);
        int wheat = state.getResource(playerIndex, ResourceType.WHEAT);
        int sheep = state.getResource(playerIndex, ResourceType.SHEEP);

        textIndex = String.format("[$$] \t %3d \t %3d \t %3d \t %3d \t %3d", wood, iron, clay, wheat, sheep).toCharArray();
        int yOffset = playerIndex * 200 + 60;
        g.drawChars(textIndex, 0, textIndex.length, 50, yOffset);


        String anyTr = state.isAnyResourceTrading(playerIndex) ? "*" : "-";
        String woodTr = state.isResourceTrading(playerIndex, ResourceType.WOOD) ? "*" : "-";
        String ironTr = state.isResourceTrading(playerIndex, ResourceType.IRON) ? "*" : "-";
        String clayTr = state.isResourceTrading(playerIndex, ResourceType.CLAY) ? "*" : "-";
        String wheatTr = state.isResourceTrading(playerIndex, ResourceType.WHEAT) ? "*" : "-";
        String sheepTr = state.isResourceTrading(playerIndex, ResourceType.SHEEP) ? "*" : "-";

        textIndex = String.format("[ %s] \t %3s \t %3s \t %3s \t %3s \t %3s", anyTr, woodTr, ironTr, clayTr, wheatTr, sheepTr).toCharArray();
        yOffset += 15;
        g.drawChars(textIndex, 0, textIndex.length, 50, yOffset);


        for(int dice=2; dice<=12; dice++) {
            if(dice != 7) {
                int woodInc = state.getResourceIncome(playerIndex, ResourceType.WOOD, dice);
                int ironInc = state.getResourceIncome(playerIndex, ResourceType.IRON, dice);
                int clayInc = state.getResourceIncome(playerIndex, ResourceType.CLAY, dice);
                int wheatInc = state.getResourceIncome(playerIndex, ResourceType.WHEAT, dice);
                int sheepInc = state.getResourceIncome(playerIndex, ResourceType.SHEEP, dice);

                textIndex = String.format("[%2d] \t %3d \t %3d \t %3d \t %3d \t %3d", dice, woodInc, ironInc, clayInc, wheatInc, sheepInc).toCharArray();

                yOffset += 15;

                g.drawChars(textIndex, 0, textIndex.length, 50, yOffset);
            }
        }
    }

    private void paintTerrain(Graphics g, Terrain t) {

        Point p1 = t.getPoint().add(Vector.landUpRight);
        Point p2 = p1.add(Vector.landDownRight);
        Point p3 = p2.add(Vector.landDownLeft);
        Point p4 = p3.add(Vector.landLeft);
        Point p5 = p4.add(Vector.landUpLeft);
        Point p6 = p5.add(Vector.landUpRight);


        int[] xPoints = new int[6];
        xPoints[0] = (int) p1.getX() + X_OFFSET;
        xPoints[1] = (int) p2.getX() + X_OFFSET;
        xPoints[2] = (int) p3.getX() + X_OFFSET;
        xPoints[3] = (int) p4.getX() + X_OFFSET;
        xPoints[4] = (int) p5.getX() + X_OFFSET;
        xPoints[5] = (int) p6.getX() + X_OFFSET;

        int[] yPoints = new int[6];
        yPoints[0] = (int) p1.getY() + Y_OFFSET;
        yPoints[1] = (int) p2.getY() + Y_OFFSET;
        yPoints[2] = (int) p3.getY() + Y_OFFSET;
        yPoints[3] = (int) p4.getY() + Y_OFFSET;
        yPoints[4] = (int) p5.getY() + Y_OFFSET;
        yPoints[5] = (int) p6.getY() + Y_OFFSET;


        Polygon polygon = new Polygon(xPoints, yPoints, 6);

        Color c = Color.YELLOW;
        if(t.getType() != null) {
            switch (t.getType()) {
                case WHEAT: c = Color.ORANGE; break;
                case IRON: c = Color.GRAY; break;
                case WOOD: c = Color.GREEN.darker(); break;
                case SHEEP: c = Color.GREEN.brighter(); break;
                case CLAY: c = Color.RED.darker(); break;
            }
        }
        g.setColor(c);

        g.fillPolygon(polygon);

        g.setColor(Color.BLACK);
        String type = "Desert";
        if(t.getType() != null) {
            type = t.getType().name();
        }

        char[] textIndex = String.format("Index: %d", t.getIndex()).toCharArray();
        char[] textType = String.format("Type: %s", type).toCharArray();
        char[] textDice = String.format("Dice: %d", t.getDice()).toCharArray();

        g.drawChars(textIndex, 0, textIndex.length,
                (int) t.getPoint().getX() + X_OFFSET - 30, (int) t.getPoint().getY() + 15 + Y_OFFSET);

        g.drawChars(textType, 0, textType.length,
                (int) t.getPoint().getX() + X_OFFSET - 30, (int) t.getPoint().getY() + Y_OFFSET);

        g.drawChars(textDice, 0, textDice.length,
                (int) t.getPoint().getX() + X_OFFSET - 30, (int) t.getPoint().getY() - 15 + Y_OFFSET);


    }

    private void paintLand(Graphics g, Land l) {
        g.setColor(Color.BLACK);

        String trading = "";
        if(l.isAnyTrading()) {
            trading = "|*";
        } else if(l.getTrading() != null) {
            trading = "|" + l.getTrading().name().substring(0, 1);
        }

        char[] textIndex = String.format("%d%s", l.getIndex(), trading).toCharArray();

        g.drawChars(textIndex, 0, textIndex.length,
                (int) l.getPoint().getX() + X_OFFSET, (int) l.getPoint().getY() + Y_OFFSET);
    }

    private void paintRoad(Graphics g, Road r) {
        g.setColor(Color.BLACK);
        char[] textIndex = String.format("%d", r.getIndex()).toCharArray();

        g.drawChars(textIndex, 0, textIndex.length,
                (int) r.getPoint().getX() + X_OFFSET,  (int) r.getPoint().getY() + Y_OFFSET);
    }

    private void paintVillageFigure(Graphics g, Point p, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        int[] xPoints = new int[5];
        xPoints[0] = (int) (p.getX() - 8) + X_OFFSET;
        xPoints[1] = (int) (p.getX() - 8) + X_OFFSET;
        xPoints[2] = (int) (p.getX()) + X_OFFSET;
        xPoints[3] = (int) (p.getX() + 8) + X_OFFSET;
        xPoints[4] = (int) (p.getX() + 8) + X_OFFSET;

        int[] yPoints = new int[5];
        yPoints[0] = (int) (p.getY() + 8) + Y_OFFSET;
        yPoints[1] = (int) (p.getY() - 8) + Y_OFFSET;
        yPoints[2] = (int) (p.getY() - 16) + Y_OFFSET;
        yPoints[3] = (int) (p.getY() - 8) + Y_OFFSET;
        yPoints[4] = (int) (p.getY() + 8) + Y_OFFSET;

        Polygon polygon = new Polygon(xPoints, yPoints, 5);

        g.fillPolygon(polygon);
    }

    private void paintCityFigure(Graphics g, Point p, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        int[] xPoints = new int[6];
        xPoints[0] = (int) (p.getX() - 8) + X_OFFSET;
        xPoints[1] = (int) (p.getX() - 8) + X_OFFSET;
        xPoints[2] = (int) (p.getX()) + X_OFFSET;
        xPoints[3] = (int) (p.getX() + 4) + X_OFFSET;
        xPoints[4] = (int) (p.getX() + 8) + X_OFFSET;
        xPoints[5] = (int) (p.getX() + 8) + X_OFFSET;

        int[] yPoints = new int[6];
        yPoints[0] = (int) (p.getY() + 8) + Y_OFFSET;
        yPoints[1] = (int) (p.getY() - 8) + Y_OFFSET;
        yPoints[2] = (int) (p.getY() - 8) + Y_OFFSET;
        yPoints[3] = (int) (p.getY() - 16) + Y_OFFSET;
        yPoints[4] = (int) (p.getY() - 8) + Y_OFFSET;
        yPoints[5] = (int) (p.getY() + 8) + Y_OFFSET;

        Polygon polygon = new Polygon(xPoints, yPoints, 6);

        g.fillPolygon(polygon);
    }

    private void paintRoadFigure(Graphics g, Point from, Point to, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));

        Vector v = Vector.vec(from, to).scale(0.2);

        Point p1 = from.add(v);
        Point p2 = to.add(v.neg());

        g.drawLine((int) p1.getX() + X_OFFSET, (int) p1.getY() + Y_OFFSET,
                (int) p2.getX() + X_OFFSET, (int) p2.getY() + Y_OFFSET);

    }

    private void paintThief(Graphics g, Point p) {
        g.setColor(Color.BLACK);

        int[] xPoints = new int[4];
        xPoints[0] = (int) (p.getX() - 8) + X_OFFSET;
        xPoints[1] = (int) (p.getX()) + X_OFFSET;
        xPoints[2] = (int) (p.getX() + 8) + X_OFFSET;
        xPoints[3] = (int) (p.getX()) + X_OFFSET;

        int[] yPoints = new int[4];
        yPoints[0] = (int) (p.getY()) + Y_OFFSET + 60;
        yPoints[1] = (int) (p.getY() - 8) + Y_OFFSET + 60;
        yPoints[2] = (int) (p.getY()) + Y_OFFSET + 60;
        yPoints[3] = (int) (p.getY() + 8) + Y_OFFSET + 60;

        Polygon polygon = new Polygon(xPoints, yPoints, 4);

        g.fillPolygon(polygon);
    }

    private void paintInfo(Graphics g) {
        g.setColor(Color.BLACK);

        int x = X_OFFSET * 2 - 400;
        int y = 100;

        for(InfoMessage info : roundInfo) {
            y += 15;

            if(info.getPlayerIndex() == -1) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(playerColors[info.getPlayerIndex()]);
            }

            char[] textIndex = info.getMessage().toCharArray();
            g.drawChars(textIndex, 0, textIndex.length, x, y);
        }

    }

    private void paintTimer(Graphics g) {
        if(timerSeconds > 0) {
            g.setColor(Color.BLACK);
            char[] textIndex = String.format("Remaining time: %5d", timerSeconds).toCharArray();
            g.drawChars(textIndex, 0, textIndex.length, X_OFFSET - 100, 25);
        }
    }

    public void startTime(int seconds) {
        timerSeconds = seconds;
        if(executor == null) {
             executor = Executors.newSingleThreadScheduledExecutor();
             executor.scheduleAtFixedRate(() -> timerTick(), 0, 1, TimeUnit.SECONDS);
        }
    }

    private void timerTick() {
        if(timerSeconds > 0) {
            timerSeconds--;
            repaint();
        }
    }

}
