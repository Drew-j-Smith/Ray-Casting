import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Lineseg {

	protected boolean raysPass = true;
	protected BufferedImage texture = null;
	// TODO have darker texture for distances further away
	protected double height = 20;
	protected int imgMinHeight = 0; // TODO rename to something like constraintX1,constraintX2
	protected int imgMaxHeight = 0;
	protected int imgMinWidth = 0;
	protected int imgMaxWidth = 0;
	
	public Wall() {
		super();
	}
	
	public Wall(BufferedImage img) {
		super();
		this.texture = img;
		imgMaxHeight = img.getHeight();
		imgMaxWidth = img.getWidth();
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
		imgMaxHeight = img.getHeight();
		imgMaxWidth = img.getWidth();
		this.height = height;
		this.raysPass = raysPass;
	}
	
	public Wall(BufferedImage img, Point sp, double degreeAngle, double length, double height, boolean raysPass) {
		super(sp, degreeAngle,length);
		this.texture = img;
		imgMaxHeight = img.getHeight();
		imgMaxWidth = img.getWidth();
		this.raysPass = raysPass;
		this.height = height;
	}
	
	public void draw(int dx, int dy1, int dy2, Graphics g) {
		// y1 = (int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter
		// y2 = (int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter

		g.drawLine(dx,dy1,dx+1,dy2);
		
	}
	
	public void draw(int dx, int dy1, int dy2, int sx, Graphics g) {
		// y1 = (int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter
		// y2 = (int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter
		if (sx > imgMaxWidth) sx = imgMinWidth;
		if (texture != null) {
	    	g.drawImage(texture, dx,dy1,
                 dx+1,dy2,
                 sx,imgMinHeight,sx+1,imgMaxHeight, null
            );
		}
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

	public int getImgMinHeight() {
		return imgMinHeight;
	}

	public void setImgMinHeight(int imgMinHeight) {
		this.imgMinHeight = imgMinHeight;
	}

	public int getImgMaxHeight() {
		return imgMaxHeight;
	}

	public void setImgMaxHeight(int imgMaxHeight) {
		this.imgMaxHeight = imgMaxHeight;
	}

	public int getImgMinWidth() {
		return imgMinWidth;
	}

	public void setImgMinWidth(int imgMinWidth) {
		this.imgMinWidth = imgMinWidth;
	}

	public int getImgMaxWidth() {
		return imgMaxWidth;
	}

	public void setImgMaxWidth(int imgMaxWidth) {
		this.imgMaxWidth = imgMaxWidth;
	}

	
}
