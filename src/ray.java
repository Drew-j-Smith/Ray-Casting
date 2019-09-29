import java.awt.*;
import java.util.List;

public class ray extends lineseg{

    private double range;
    private boolean render;

    public ray(double range, boolean render){
        super();
        this.range = range;
        this.render = render;
    }

    public void cast(point center, double angle, List<lineseg> walls, Graphics g){
        point intersectionPoint;

        //collision tests, returns EP of ray
        {
            setSp(center);
            setEp(new lineseg(center, angle, range).getEp());
            for (lineseg l : walls) {
                intersectionPoint = l.getIntersection(this);
                if (intersectionPoint != null && getDistance() > new lineseg(getSp(), intersectionPoint).getDistance()) {
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
