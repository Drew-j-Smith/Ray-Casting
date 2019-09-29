import java.awt.*;

public class lineseg {
    private point sp, ep;

    public lineseg() {
        sp = new point();
        ep = new point();
    }

    public lineseg(point sp, point ep) {
        this.sp = sp;
        this.ep = ep;
    }

    public lineseg(point p, double degreeAngle, double length){
        sp = p;
        if (degreeAngle <= 90)
            ep = new point(p.getX() + length * Math.cos(Math.toRadians(degreeAngle)), p.getY() + length * Math.sin(Math.toRadians(degreeAngle)));
        else{
            degreeAngle -= 180;
            ep = new point(p.getX() + -1.0 * length * Math.cos(Math.toRadians(degreeAngle)), p.getY() + -1.0 * length * Math.sin(Math.toRadians(degreeAngle)));
        }

    }

    public point getSp() {
        return sp;
    }

    public void setSp(point sp) {
        this.sp = sp;
    }

    public point getEp() {
        return ep;
    }

    public void setEp(point ep) {
        this.ep = ep;
    }

    public String toString() {
        return "lineseg{" +
                "sp=" + sp +
                ", ep=" + ep +
                '}';
    }

    public void draw(Graphics g){
        //sp.draw(g);
        //ep.draw(g);
        g.drawLine((int)sp.getX(),(int)sp.getY(),(int)ep.getX(),(int)ep.getY());
    }

    public double getSlope(){
        return (ep.getY()-sp.getY())/(ep.getX()-sp.getX());
    }

    public double getSlope(double angle){
        return Math.tan(Math.toRadians(angle));
    }

    public double getDistance(){
        return Math.sqrt(Math.pow(sp.getX()-ep.getX(),2)+Math.pow(sp.getY()-ep.getY(),2));
    }

    public double getYIntercept(){
        return sp.getY() - getSlope() * sp.getX();
    }

    public point getIntersection(lineseg l){
        double x1 = sp.getX(); double x2 = ep.getX();
        double y1 = sp.getY(); double y2 = ep.getY();
        double x3 = l.getSp().getX(); double x4 = l.getEp().getX();
        double y3 = l.getSp().getY(); double y4 = l.getEp().getY();

        final double den = (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4); // IF den == 0 ray and wall are parallel

        if (den == 0) return null; // they don't intersect

        final double t = (((x1 - x3)*(y3 - y4)) - ((y1 - y3)*(x3 - x4))) / den;
        final double u = -((((x1 - x2)*(y1 - y3)) - ((y1 - y2)*(x1 - x3))) / den);

        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) { // IF they do intersect
            point pt = new point();
            pt.setX(x1 + t * (x2 - x1));
            pt.setY(y1 + t * (y2 - y1));
            return pt;
        }

        return null; // they don't intersect

    }

    public double getAngle(){
        if (getEp().getX() < getSp().getX())
            return Math.toDegrees(Math.atan((getEp().getY()-getSp().getY())/(getEp().getX()-getSp().getX()))) + 180;
        else
            return Math.toDegrees(Math.atan((getEp().getY()-getSp().getY())/(getEp().getX()-getSp().getX())));
    }



}
