import java.util.ArrayList;
import java.util.List;

public class LevelEditor {
	
	private String map;
	private int width, height;
	private List<Lineseg> finalWalls = new ArrayList<Lineseg>();
	private double wallLength = 25;
	
	private final char wall = '#';
	
	public LevelEditor(String map, int width, int height, double wallLength) {
		// width and height starting at one
		this.map = map;
		this.width = width;
		this.height = height;
		this.wallLength = wallLength;
	}
	
	public List<Lineseg> mapToLineseg() throws Exception {
		
		if (map.length() != width * height) throw new MapExceptions("\n-Expected map length: " + width*height + "\n-Map length: " + map.length() + "\n-Returned: null"); // TODO return if number of chars in string is less then area of map
		
		boolean top, bottom, left, right;
		Lineseg topLine = new Lineseg(null,null);
		Lineseg bottomLine = new Lineseg(null,null);
		
		List<Lineseg> verticalLeftWalls = new ArrayList<Lineseg>();
		List<Lineseg> verticalRightWalls = new ArrayList<Lineseg>();
		
		/*	Bottom and top of walls are switched because the top left corner is (0,0)
		 * 
		 *           .-> bottom
		 *           __
		 *	left <- |  | -> right
		 *          '--'    
		 *           '-> top
		 *
		 */
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if (y == 0) { // initialize lists with null values
					verticalLeftWalls.add(new Lineseg(null,null));
					verticalRightWalls.add(new Lineseg(null,null));
				}
				
				final int i = x + (y * width);
				
				if (map.charAt(i) == wall) {
					
					top = true;
					bottom = true;
					right = true;
					left = true;
					// Check if right and left walls are to be drawn
					if (y == 0) { // bottom is true because it's in the top row
						if (map.charAt(i + width) == wall) top = false;
					} else if (y == height - 1) {
						if (map.charAt(i - width) == wall) bottom = false;
					} else {
						if (map.charAt(i + width) == wall) top = false;
						if (map.charAt(i - width) == wall) bottom = false;
					}
					// Check if top and bottom walls are to be drawn
					if (x == 0) {
						if (map.charAt(i+1) == wall) right = false;
					} else if (x == width - 1) {
						if (map.charAt(i - 1) == wall) left = false;
					} else {
						if (map.charAt(i - 1) == wall) left = false;
						if (map.charAt(i + 1) == wall) right = false;
					}
					// IF there should be a top wall
					if (top) {
						if (topLine.getSp() == null) { // create new wall
							topLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
						} else { // update ending point of existing wall
							topLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
						} 
					} else {
							if (topLine.getSp() != null) { // add to final wall list and prepair for new wall
								topLine.setEp(new Point((x)*wallLength, y*wallLength + wallLength));
								finalWalls.add(new Lineseg(topLine));
								topLine.setSp(null);
								topLine.setEp(null);
						}
					}
					// IF there should be a bottom wall
					if (bottom) {
						if (bottomLine.getSp() == null) { // create new line
							bottomLine.setSp(new Point(x*wallLength,y*wallLength));
						} else { // update ending point of existing wall
							bottomLine.setEp(new Point(x*wallLength,y*wallLength));
						}
					} else {
						if (bottomLine.getSp() != null) { // add to final wall list and prepair for new wall
							bottomLine.setEp(new Point((x)*wallLength, y*wallLength));
							finalWalls.add(new Lineseg(bottomLine));
							bottomLine.setSp(null);
							bottomLine.setEp(null);
						}
					}
					// IF there should be a right wall
					if (right) {
						if (verticalRightWalls.get(x).getSp() == null) { // create new line
							verticalRightWalls.get(x).setSp(new Point(x*wallLength + wallLength,y*wallLength));
						} else { // add to line
							verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength,y*wallLength));
						}
					} else {
						if (verticalRightWalls.get(x).getSp() != null) {
							verticalRightWalls.get(x).setEp(new Point((x)*wallLength + wallLength,y*wallLength));
							finalWalls.add(new Lineseg(verticalRightWalls.get(x)));
							verticalRightWalls.get(x).setSp(null);
							verticalRightWalls.get(x).setEp(null);
						}
					}
					// IF there should be a left wall
					if (left) {
						if (verticalLeftWalls.get(x).getSp() == null) { // create new line
							verticalLeftWalls.get(x).setSp(new Point(x*wallLength,y*wallLength));
						} else { // add to line
							verticalLeftWalls.get(x).setEp(new Point(x*wallLength,y*wallLength));
						}
					} else {
						if (verticalLeftWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
							verticalLeftWalls.get(x).setEp(new Point(x*wallLength, y*wallLength));
							finalWalls.add(new Lineseg(verticalLeftWalls.get(x)));
							verticalLeftWalls.get(x).setSp(null);
							verticalLeftWalls.get(x).setEp(null);
						}
					}
					// IF x row has ended, end current wall and prepair for new wall
					if (x == width - 1) {
						// IF there is a current wall being built add final end point and add to list of final walls
						if (bottomLine.getSp() != null) {
							bottomLine.setEp(new Point((x+1)*wallLength, y*wallLength)); // get final end point
							finalWalls.add(new Lineseg(bottomLine)); // add to final wall list
							bottomLine.setSp(null); // reset wall for new one
							bottomLine.setEp(null);
						}
						if (topLine.getSp() != null) {
							topLine.setEp(new Point((x+1)*wallLength, y*wallLength + wallLength)); // get final end point
							finalWalls.add(new Lineseg(topLine)); // add to final wall list
							topLine.setSp(null); // reset wall for new one
							topLine.setEp(null);
						}
					}
					// IF y column has ended, end current wall and add to list of final walls
					if (y == height - 1) {
						//
						if (verticalRightWalls.get(x).getSp() != null) {
							verticalRightWalls.get(x).setEp(new Point((x)*wallLength + wallLength,(y+1)*wallLength)); // get final end point
							finalWalls.add(new Lineseg(verticalRightWalls.get(x))); // add to final wall list
						}
						if (verticalLeftWalls.get(x).getSp() != null) {
							verticalLeftWalls.get(x).setEp(new Point((x)*wallLength,(y+1)*wallLength)); // get final end point
							finalWalls.add(new Lineseg(verticalLeftWalls.get(x))); // add to final wall list
						}
					}
					
				} else { // Current index is not a wall
					
					if (bottomLine.getSp() != null) { // add to wall list and prepair bottomLine
						bottomLine.setEp(new Point(x*wallLength, y*wallLength));
						finalWalls.add(new Lineseg(bottomLine));
						bottomLine.setSp(null);
						bottomLine.setEp(null);
					}
					if (topLine.getSp() != null) { // add to wall list and prepair bottomLine
						topLine.setEp(new Point(x*wallLength, y*wallLength + wallLength));
						finalWalls.add(new Lineseg(topLine));
						topLine.setSp(null);
						topLine.setEp(null);
					}
					// vertical lines below
					if (verticalLeftWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
						verticalLeftWalls.get(x).setEp(new Point(x*wallLength, y*wallLength));
						finalWalls.add(new Lineseg(verticalLeftWalls.get(x)));
						verticalLeftWalls.get(x).setSp(null);
						verticalLeftWalls.get(x).setEp(null);
					}
					if (verticalRightWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
						verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength, y*wallLength));
						finalWalls.add(new Lineseg(verticalRightWalls.get(x)));
						verticalRightWalls.get(x).setSp(null);
						verticalRightWalls.get(x).setEp(null);
					}
				}
				
				
			}
		}
		return finalWalls;
	}
	
}
