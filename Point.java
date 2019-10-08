import java.awt.*;

public class Point {
    private double x, y;


    public Point() {
        x = 0.;
        y = 0.;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(String point){
        int temp = point.indexOf(',');
        int temp2 = point.indexOf('}');

        this.x = Double.parseDouble(point.substring(8,temp));
        this.y = Double.parseDouble(point.substring(temp + 4, temp2));

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
    
    public Point sub(Point pt) {
    	return new Point(x - pt.getX(), y - pt.getY());
    }
    public Point add(Point pt) {
    	return new Point(x + pt.getX(), y + pt.getY());
    }

}
