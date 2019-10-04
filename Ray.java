import java.awt.*;
import java.util.List;

public class Ray extends Lineseg {

    private double range;
    private boolean render;

    public Ray(double range, boolean render){
        super();
        this.range = range;
        this.render = render;
    }

    public int cast(Point center, double angle, List<Wall> walls, Graphics g){
        Point intersectionPoint;
        int intersectingWall = -1;
        //collision tests, returns EP of ray
        {
            setSp(center);
            setEp(new Lineseg(center, angle, range).getEp());
            for (int i = 0; i < walls.size(); i++) {
                intersectionPoint = walls.get(i).getIntersection(this);
                if (intersectionPoint != null && getDistance() > new Lineseg(getSp(), intersectionPoint).getDistance()) {
                	setEp(intersectionPoint);
                    intersectingWall = i;
                }
            }
        }


        if (render)
            draw(g);

        return intersectingWall;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}
