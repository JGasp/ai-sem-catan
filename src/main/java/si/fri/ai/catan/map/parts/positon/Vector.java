package si.fri.ai.catan.map.parts.positon;

public class Vector {

    private static final int BASE_SIZE = 50;

    public static final Vector X_HALF = new Vector(BASE_SIZE, 0);
    public static final Vector X = X_HALF.scale(2);

    public static final Vector Y = new Vector(0, Math.sin(Math.toRadians(60)) * BASE_SIZE * 2).neg();


    public static final Vector terrainUp = Y.scale(2);
    public static final Vector terrainDown = terrainUp.neg();

    public static final Vector terrainUpLeft = Y.add(X.add(X_HALF).neg());
    public static final Vector terrainDownRight = terrainUpLeft.neg();

    public static final Vector terrainUpRight = Y.add(X.add(X_HALF));
    public static final Vector terrainDownLeft = terrainUpRight.neg();

    public static final Vector landRight = X;
    public static final Vector landLeft = X.neg();

    public static final Vector landUpRight = Y.add(X_HALF);
    public static final Vector landDownLeft = landUpRight.neg();

    public static final Vector landUpLeft = Y.add(X_HALF.neg());
    public static final Vector landDownRight = landUpLeft.neg();



    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector scale(double scale) {
        return new Vector(x * scale, y * scale);
    }

    public Vector neg() {
        return new Vector(-x, -y);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public Vector add(Vector vec) {
        return new Vector(x + vec.x, y + vec.y);
    }

    public static Vector vec(Point from, Point to) {
        return new Vector(to.getX() - from.getX(), to.getY() - from.getY());
    }
}
