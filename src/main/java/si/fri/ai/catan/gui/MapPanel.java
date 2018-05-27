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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        g.fillRect(0,0, 1920, 1080);

        for(Terrain t : map.getTerrains()) {
            paintTerrain(g, t);
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
        yPoints[0] = Y_OFFSET - (int) p1.getY();
        yPoints[1] = Y_OFFSET - (int) p2.getY();
        yPoints[2] = Y_OFFSET - (int) p3.getY();
        yPoints[3] = Y_OFFSET - (int) p4.getY();
        yPoints[4] = Y_OFFSET - (int) p5.getY();
        yPoints[5] = Y_OFFSET - (int) p6.getY();


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

        g.drawChars(textIndex, 0, textIndex.length, (int) t.getPoint().getX() + X_OFFSET - 30, Y_OFFSET - 15 - (int) t.getPoint().getY());
        g.drawChars(textType, 0, textType.length, (int) t.getPoint().getX() + X_OFFSET - 30, Y_OFFSET - (int) t.getPoint().getY());
        g.drawChars(textDice, 0, textDice.length, (int) t.getPoint().getX() + X_OFFSET - 30, Y_OFFSET + 15 - (int) t.getPoint().getY());


    }

    private void paintLand(Graphics g, Land l) {
        g.setColor(Color.BLACK);
        char[] textIndex = String.format("%d", l.getIndex()).toCharArray();
        g.drawChars(textIndex, 0, textIndex.length, (int) l.getPoint().getX() + X_OFFSET, Y_OFFSET - (int) l.getPoint().getY());
    }

    private void paintRoad(Graphics g, Road r) {
        g.setColor(Color.BLACK);
        char[] textIndex = String.format("%d", r.getIndex()).toCharArray();
        g.drawChars(textIndex, 0, textIndex.length, (int) r.getPoint().getX() + X_OFFSET, Y_OFFSET - (int) r.getPoint().getY());
    }

}
