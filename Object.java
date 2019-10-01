import java.awt.image.BufferedImage;

public class Object {

	Point position;
	double x; // width
	double z; // depth
	double y; // height
	BufferedImage side;
	BufferedImage top;
	
	Object(Point position, double width, double depth, double height) {
		this.position = position;
		this.x = width;
		this.z = depth;
		this.y = height;
	}
	
}
