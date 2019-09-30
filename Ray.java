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

    public void cast(Point center, double angle, List<Lineseg> walls, Graphics g){
    	Point intersectionPoint;

        //collision tests, returns EP of ray
        {
            setSp(center);
            setEp(new Lineseg(center, angle, range).getEp());
            for (Lineseg l : walls) {
                intersectionPoint = l.getIntersection(this);
                if (intersectionPoint != null && getDistance() > new Lineseg(getSp(), intersectionPoint).getDistance()) {
                    setEp(intersectionPoint);
                }
            }
        }

        //rounds points
        if (angle < 0){
            //setEp(new point(getEp().getX(), Math.ceil(getEp().getY())));
        }

        if (render)
            draw(g);
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
