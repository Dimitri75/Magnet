package kei.magnet;

/**
 * Created by carlo_000 on 24/10/2015.
 */
public class Position {
    private double x = 0.0d;
    private double y = 0.0d;

    public Position() {
    }
    public Position(double x, double y) {
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
}
