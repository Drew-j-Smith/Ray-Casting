import java.util.ArrayList;
import java.util.List;

public class LevelEditor {
	
	private String map;
	private int width, height;
	private List<Lineseg> walls = new ArrayList<Lineseg>();
	private double wallLength = 25;
	
	private final char wall = '#';
	
	public LevelEditor(String map, int width, int height) {
		// width and height starting at one
		this.map = map;
		this.width = width;
		this.height = height;
		
	}
	
	public List<Lineseg> mapToLineseg() throws Exception {
		
		if (map.length() != width * height) throw new MapExceptions("\n-Expected map length: " + width*height + "\n-Map length: " + map.length() + "\n-Returned: null"); // TODO return if number of chars in string is less then area of map
		
		boolean top, bottom, left, right;
		Lineseg topLine = new Lineseg(null,null);
		Lineseg bottomLine = new Lineseg(null,null);
		Lineseg leftLine = new Lineseg(null,null);
		Lineseg rightLine = new Lineseg(null,null);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int i = x + (y * width);
				if (map.charAt(i) == wall) {
					
					top = true;
					bottom = true;
					
					if (y == 0) {
						if (map.charAt(i + width) == wall) top = false;
					} else if (y == height - 1) {
						if (map.charAt(i - width) == wall) bottom = false;
					} else {
						if (map.charAt(i + width) == wall) top = false;
						if (map.charAt(i - width) == wall) bottom = false;
					}
					
					if (top) {
					if (topLine.getSp() == null) { // create new line
						topLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
					} else { // add to line
						topLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
					} 
					} else {
						if (topLine.getSp() != null) { // add to wall list and prepair bottomLine
							topLine.setEp(new Point((x)*wallLength, y*wallLength + wallLength));
							walls.add(new Lineseg(topLine));
							topLine.setSp(null);
							topLine.setEp(null);
						}
					}
					if (bottom) {
					if (bottomLine.getSp() == null) { // create new line
						bottomLine.setSp(new Point(x*wallLength,y*wallLength));
					} else { // add to line
						bottomLine.setEp(new Point(x*wallLength,y*wallLength));
					}
					} else {
						if (bottomLine.getSp() != null) { // add to wall list and prepair bottomLine
							bottomLine.setEp(new Point((x)*wallLength, y*wallLength));
							walls.add(new Lineseg(bottomLine));
							bottomLine.setSp(null);
							bottomLine.setEp(null);
						}
					}
	
					if (x == width - 1) { // x row has ended
						if (bottomLine.getSp() != null) { // add to wall list and prepair bottomLine
							bottomLine.setEp(new Point((x+1)*wallLength, y*wallLength));
							walls.add(new Lineseg(bottomLine));
							bottomLine.setSp(null);
							bottomLine.setEp(null);
						}
						if (topLine.getSp() != null) { // add to wall list and prepair bottomLine
							topLine.setEp(new Point((x+1)*wallLength, y*wallLength + wallLength));
							walls.add(new Lineseg(topLine));
							topLine.setSp(null);
							topLine.setEp(null);
						}
					}
					
				} else { // not a wall
					if (bottomLine.getSp() != null) { // add to wall list and prepair bottomLine
						bottomLine.setEp(new Point(x*wallLength, y*wallLength));
						walls.add(new Lineseg(bottomLine));
						bottomLine.setSp(null);
						bottomLine.setEp(null);
					}
					if (topLine.getSp() != null) { // add to wall list and prepair bottomLine
						topLine.setEp(new Point(x*wallLength, y*wallLength + wallLength));
						walls.add(new Lineseg(topLine));
						topLine.setSp(null);
						topLine.setEp(null);
					}
				}
			}
		}
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int i = x + (y * width);
				if (map.charAt(i) == wall) {
					
					right = true;
					left = true;
					
					if (x == 0) {
						if (map.charAt(i+1) == wall) right = false;
					} else if (x == width - 1) {
						if (map.charAt(i - 1) == wall) left = false;
					} else {
						if (map.charAt(i - 1) == wall) left = false;
						if (map.charAt(i + 1) == wall) right = false;
					}
					
					if (right) {
					if (rightLine.getSp() == null) { // create new line
						rightLine.setSp(new Point(x*wallLength + wallLength,y*wallLength));
					} else { // add to line
						rightLine.setEp(new Point(x*wallLength + wallLength,y*wallLength));
					}
					} else {
						if (rightLine.getSp() != null) {
							rightLine.setEp(new Point((x)*wallLength + wallLength,y*wallLength));
							walls.add(new Lineseg(rightLine));
							rightLine.setSp(null);
							rightLine.setEp(null);
						}
					}
					
					if (left) {
					if (leftLine.getSp() == null) { // create new line
						leftLine.setSp(new Point(x*wallLength,y*wallLength));
					} else { // add to line
						leftLine.setEp(new Point(x*wallLength,y*wallLength));
					}
					} else {
						if (leftLine.getSp() != null) { // add to wall list and prepair bottomLine
							leftLine.setEp(new Point(x*wallLength, y*wallLength));
							walls.add(new Lineseg(leftLine));
							leftLine.setSp(null);
							leftLine.setEp(null);
						}
					}
	
					if (y == height - 1) { // y row has ended
						if (rightLine.getSp() != null) {
							rightLine.setEp(new Point((x)*wallLength + wallLength,(y+1)*wallLength));
							walls.add(new Lineseg(rightLine));
							rightLine.setSp(null);
							rightLine.setEp(null);
						}
						if (leftLine.getSp() != null) {
							leftLine.setEp(new Point((x)*wallLength,(y+1)*wallLength));
							walls.add(new Lineseg(leftLine));
							leftLine.setSp(null);
							leftLine.setEp(null);
						}
					}
					
				} else { // not a wall
					if (leftLine.getSp() != null) { // add to wall list and prepair bottomLine
						leftLine.setEp(new Point(x*wallLength, y*wallLength));
						walls.add(new Lineseg(leftLine));
						leftLine.setSp(null);
						leftLine.setEp(null);
					}
					if (rightLine.getSp() != null) { // add to wall list and prepair bottomLine
						rightLine.setEp(new Point(x*wallLength + wallLength, y*wallLength));
						walls.add(new Lineseg(rightLine));
						rightLine.setSp(null);
						rightLine.setEp(null);
					}
				}
			}
		}
		return walls;
	}
	
}
