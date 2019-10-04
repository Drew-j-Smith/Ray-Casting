import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Lineseg {

	protected boolean raysPass = true;
	protected BufferedImage texture = null;
	// TODO have darker texture for distances further away
	protected double height = 20;
	protected int constraintY1 = 0; // image point upper left y
	protected int constraintY2 = 0; // image point bottom right y
	protected int constraintX1 = 0; // image point upper left x
	protected int constraintX2 = 0; // image point bottom right x
	
	public Wall() {
		super();
	}
	
	public Wall(BufferedImage img) {
		super();
		this.texture = img;
		constraintY2 = img.getHeight();
		constraintX2 = img.getWidth();
	}
	
	public Wall(Lineseg lineseg) {
		super(lineseg);
	}
	
	public Wall(Wall wall) {
		super(wall.getSp(), wall.getEp());
		this.texture = wall.getTexture();
		this.constraintX1 = wall.getConstraintX1();
		this.constraintX2 = wall.getConstraintX2();
		this.constraintY1 = wall.getConstraintY1();
		this.constraintY2 = wall.getConstraintY2();
		this.height = wall.getHeight();
		this.raysPass = wall.isRaysPass();
	}
	
	public Wall(Point sp, Point ep) {
		super(ep,sp);
	}
	
	public Wall(Point sp, Point ep, double height, boolean raysPass) {
		super(ep,sp);
		this.height = height;
		this.raysPass = raysPass;
	}
	
	public Wall(Point sp, double degreeAngle, double length, double height, boolean raysPass) {
		super(sp, degreeAngle,length);
		this.raysPass = raysPass;
		this.height = height;
	}
	
	public Wall(BufferedImage img, Point sp, Point ep, double height, boolean raysPass) {
		super(ep,sp);
		this.texture = img;
		constraintY2 = img.getHeight();
		constraintX2 = img.getWidth();
		this.height = height;
		this.raysPass = raysPass;
	}
	
	public Wall(BufferedImage img, Point sp, double degreeAngle, double length, double height, boolean raysPass) {
		super(sp, degreeAngle,length);
		this.texture = img;
		constraintY2 = img.getHeight();
		constraintX2 = img.getWidth();
		this.raysPass = raysPass;
		this.height = height;
	}
	
	public void draw(int dx, int dy1, int dy2, Graphics g) {
		// y1 = (int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter
		// y2 = (int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter

		g.drawLine(dx,dy1,dx+1,dy2);
		
	}
	
	public void draw(int dx1, int dy1, int dx2, int dy2, int sx1, int sx2, Graphics g) {
		// y1 = (int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter
		// y2 = (int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter
		if (texture != null) {
	    	g.drawImage(texture,
	    		 dx1,dy1,
                 dx2,dy2,
                 constraintX1 + sx1, constraintY1,
                 constraintX1 + sx2, constraintY2,
                 null
            );
		}
	}
	
	public void setConstraints(int x1, int y1, int x2, int y2) {
		this.constraintX1 = x1;
		this.constraintX2 = x2;
		this.constraintY1 = y1;
		this.constraintY2 = y2;
	}

	public boolean isRaysPass() {
		return raysPass;
	}

	public void setRaysPass(boolean raysPass) {
		this.raysPass = raysPass;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getConstraintY1() {
		return constraintY1;
	}

	public void setConstraintY1(int constraintY1) {
		this.constraintY1 = constraintY1;
	}

	public int getConstraintY2() {
		return constraintY2;
	}

	public void setConstraintY2(int constraintY2) {
		this.constraintY2 = constraintY2;
	}

	public int getConstraintX1() {
		return constraintX1;
	}

	public void setConstraintX1(int constraintX1) {
		this.constraintX1 = constraintX1;
	}

	public int getConstraintX2() {
		return constraintX2;
	}

	public void setConstraintX2(int constraintX2) {
		this.constraintX2 = constraintX2;
	}
	
	public int getTextureWidth() {
		return constraintX2 - constraintX1;	
	}
}
