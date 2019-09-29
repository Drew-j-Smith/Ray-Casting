import java.awt.*;

public class point {
    private double x, y;


    public point() {
        x = 0.;
        y = 0.;
    }

    public point(double x, double y) {
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

    @Override
    public String toString() {
        return "point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void draw(Graphics g) {
        g.drawOval((int) x-5, (int) y-5, 10,10);
    }

}
