import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CircleWall extends Wall {
	
	double radius;
	
	public CircleWall(Point pt, double radius) {
		this.sp = pt;
		this.radius = radius;
	}
	
	public CircleWall(BufferedImage img, Point pt, double radius) {
		setTexture(img);
		this.sp = pt;
		this.radius = radius;
	}
	
	public Point getIntersection(Lineseg l) {
    	
		Point d;
		Point intersect;
		double A, B, C, det, t;
		
		d = l.getEp().sub(l.getSp());
		
		A = d.getX() * d.getX() + d.getY() * d.getY();
		B = 2 * (d.getX() * (l.getSp().getX() - sp.getX()) + d.getY() * (l.getSp().getY() - sp.getY()));
		C = (l.getSp().getX() - sp.getX()) * (l.getSp().getX() - sp.getX()) +
				(l.getSp().getY() - sp.getY()) * (l.getSp().getY() - sp.getY()) - radius * radius;
		
		det = B * B - 4 * A * C;
		if ((A <= 0.0000001 || det < 0)) {
			// no real solution
			return null;
		} else if (det == 0) {
			// one answer
			t = -B / (2. * A);
			System.out.println("HUH");
			intersect = new Point(l.sp.getX() + t * d.getX(), l.sp.getY() + t * d.getY());
		} else {
			//t = (double)((-B + Math.sqrt(det)) / (2 * A));
			//intersect = new Point(l.sp.getX() + t *d.getX(), l.sp.getY() + t * d.getY());
			t = (double)((-B - Math.sqrt(det)) / (2 * A));
			intersect = new Point(l.sp.getX() + t *d.getX(), l.sp.getY() + t * d.getY());
			if (Math.round(new Lineseg(l.getSp(), intersect).getAngle()) == Math.round(l.getAngle())) {
				return intersect;
			}
		}

		return null;
	}
	
	public void draw(Graphics g) {
		g.drawOval((int)(sp.getX() - radius), (int)(sp.getY() - radius), (int)radius * 2, (int)radius * 2);
	}
        

    

}
