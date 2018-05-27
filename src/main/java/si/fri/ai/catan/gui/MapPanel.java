package si.fri.ai.catan.gui;

import si.fri.ai.catan.State;
import si.fri.ai.catan.map.Map;
import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.map.parts.positon.Point;
import si.fri.ai.catan.map.parts.positon.Vector;

import javax.swing.*;
import java.awt.*;


public class MapPanel extends JPanel {

    private static final Color[] playerColors = { Color.RED, Color.BLUE, Color.ORANGE, Color.WHITE };

    private static final int Y_OFFSET = 500;
    private static final int X_OFFSET = 900;

    private JFrame frame;

    private Map map;
    private State state;


    public MapPanel(Map map) {
        this.map = map;


        frame = new JFrame();
        frame.setPreferredSize(new Dimension(1920, 1080));
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);

    }


    public void updateState(State state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        g.fillRect(0,0, 1920, 1080);

        for(Terrain t : map.getTerrains()) {
            paintTerrain(g, t);
        }

        if(state != null) {
            for(int pi=0; pi<State.NUMBER_OF_PLAYERS; pi++) {

                int numberOfRoads = state.getNumberOfRoads(pi);
                for(int ri=0; ri<numberOfRoads; ri++) {
                    Road r = map.gr(state.getRoadLocation(pi, ri));
                    paintRoad(g, r.getFrom().getPoint(), r.getTo().getPoint(), pi);
                }

                int numberOfVillages = state.getNumberOfVillages(pi);
                for(int vi=0; vi<numberOfVillages; vi++) {
                    Land l = map.gl(state.getVillagesLocation(pi, vi));
                    paintVillage(g, l.getPoint(), pi);
                }

                int numberOfCities = state.getNumberOfCities(pi);
                for(int ci=0; ci<numberOfCities; ci++) {
                    Land l = map.gl(state.getCityLocation(pi, ci));
                    paintCity(g, l.getPoint(), pi);
                }
            }
        }

        for(Land l : map.getLands()) {
            paintLand(g, l);
        }

        for(Road r : map.getRoads()) {
            paintRoad(g, r);
        }

    }

    protected void paintTerrain(Graphics g, Terrain t) {

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
        switch (t.getType()) {
            case WHEAT: c = Color.ORANGE; break;
            case IRON: c = Color.GRAY; break;
            case WOOD: c = Color.GREEN.darker(); break;
            case SHEEP: c = Color.GREEN.brighter(); break;
            case CLAY: c = Color.RED.darker(); break;
        }
        g.setColor(c);

        g.fillPolygon(polygon);

        g.setColor(Color.BLACK);
        char[] textIndex = String.format("Index: %d", t.getIndex()).toCharArray();
        char[] textType = String.format("Type: %s", t.getType().name()).toCharArray();
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
        char[] textIndex = String.format("%d", l.getIndex()).toCharArray();

        g.drawChars(textIndex, 0, textIndex.length,
                (int) l.getPoint().getX() + X_OFFSET, (int) l.getPoint().getY() + Y_OFFSET);
    }

    private void paintRoad(Graphics g, Road r) {
        g.setColor(Color.BLACK);
        char[] textIndex = String.format("%d", r.getIndex()).toCharArray();

        g.drawChars(textIndex, 0, textIndex.length,
                (int) r.getPoint().getX() + X_OFFSET,  (int) r.getPoint().getY() + Y_OFFSET);
    }

    private void paintVillage(Graphics g, Point p, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        int[] xPoints = new int[5];
        xPoints[0] = (int) (p.getX() - 6);
        xPoints[1] = (int) (p.getX() - 6);
        xPoints[2] = (int) (p.getX());
        xPoints[3] = (int) (p.getX() + 6);
        xPoints[4] = (int) (p.getX() + 6);

        int[] yPoints = new int[5];
        yPoints[0] = (int) (p.getY() + 6);
        yPoints[1] = (int) (p.getY() - 6);
        yPoints[2] = (int) (p.getY() - 12);
        yPoints[3] = (int) (p.getY() - 6);
        yPoints[4] = (int) (p.getY() + 6);

        Polygon polygon = new Polygon(xPoints, yPoints, 5);

        g.fillPolygon(polygon);
    }

    private void paintCity(Graphics g, Point p, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        int[] xPoints = new int[6];
        xPoints[0] = (int) (p.getX() - 12);
        xPoints[1] = (int) (p.getX() - 12);
        xPoints[2] = (int) (p.getX());
        xPoints[2] = (int) (p.getX() + 6);
        xPoints[3] = (int) (p.getX() + 12);
        xPoints[4] = (int) (p.getX() + 12);

        int[] yPoints = new int[6];
        yPoints[0] = (int) (p.getY() + 6);
        yPoints[1] = (int) (p.getY() - 6);
        yPoints[1] = (int) (p.getY() - 6);
        yPoints[2] = (int) (p.getY() - 12);
        yPoints[3] = (int) (p.getY() - 6);
        yPoints[4] = (int) (p.getY() + 6);

        Polygon polygon = new Polygon(xPoints, yPoints, 6);

        g.fillPolygon(polygon);
    }

    private void paintRoad(Graphics g, Point from, Point to, int playerIndex) {
        g.setColor(playerColors[playerIndex]);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));

        Vector v = Vector.vec(from, to).scale(0.2);

        Point p1 = from.add(v);
        Point p2 = to.add(v.neg());

        g.drawLine((int) p1.getX(), (int) p1.getY(),
                (int) p2.getX(), (int) p2.getY());

    }

}
