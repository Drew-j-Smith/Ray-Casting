import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class LevelEditor {
	
	private String map;
	private String mapTexture;
	private int width, height;

	private double wallLength = 20;
	
	private final char wall = '#', wood = 'w', hitler = 'h', blue = 'b';
	BufferedImage img;
	
	boolean top, bottom, left, right;
	boolean sameTop, sameBottom, sameLeft, sameRight;
	Wall topLine;
	Wall bottomLine;
	List<Wall> verticalLeftWalls;
	List<Wall> verticalRightWalls;
	List<Wall> slantedWalls;
	
	public LevelEditor(String map, String mapTexture, int width, int height, double wallLength) {
		// width and height starting at one
		try {
			img = ImageIO.read(new File("src//wolf3d.png"));
		} catch (IOException e) {}
		this.map = map;
		this.mapTexture = mapTexture;
		this.width = width;
		this.height = height;
		this.wallLength = wallLength;
	}
	
	public List<Wall> mapToLineseg() throws Exception {
		
		if (mapTexture.length() != width * height || map.length() != width * height) throw new MapExceptions("\n-Expected map length: " + width*height + "\n-Map length: " + mapTexture.length() + "\n-Returned: null"); // TODO return if number of chars in string is less then area of map
		
		List<Wall> finalWalls = new ArrayList<Wall>();
		
		topLine = new Wall(img, null, null, 20, true);
		bottomLine = new Wall(img, null, null, 20, true);
		boolean renderTop = true, renderBottom = false;
		/*
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				final int i = x + (y * width);
				
					// horizontal walls ------------------------------------------------------------------
				renderTop = true;
				renderBottom = true;
					if (y != 0 && map.charAt(i-width) == wall) renderTop = false;
					if (y != height - 1 && map.charAt(i+width-1) == wall) renderBottom = false;
					
					if (x == 0) { // start a new wall at left edge
						if (map.charAt(x) == wall) {
							setConstraints(mapTexture.charAt(i));
							topLine.setSp(new Point(x*wallLength,y*wallLength));
							bottomLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
						}
					} else if (x != 0) {
						if (x != width - 1) {
							if (map.charAt(i) == wall) {
								if (map.charAt(i - 1) == wall) {
									if (mapTexture.charAt(i - 1) == mapTexture.charAt(i)) { // extend wall
										setConstraints(mapTexture.charAt(i));
										topLine.setEp(new Point((x+1)*wallLength,y*wallLength));
										bottomLine.setEp(new Point((x+1)*wallLength,y*wallLength + wallLength));
									} else if (mapTexture.charAt(i - 1) != mapTexture.charAt(i)) { // end wall, submit, and start new wall
										setConstraints(mapTexture.charAt(i - 1));
										topLine.setEp(new Point(x*wallLength,y*wallLength));
										bottomLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
										if (renderTop) finalWalls.add(new Wall(topLine));
										if (renderBottom) finalWalls.add(new Wall(bottomLine));
										setConstraints(mapTexture.charAt(i));
										topLine.setSp(new Point(x*wallLength,y*wallLength));
										bottomLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
									}
								} else if (map.charAt(i - 1) != wall) { // start new wall
									setConstraints(mapTexture.charAt(i));
									topLine.setSp(new Point(x*wallLength,y*wallLength));
									bottomLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
								}
							} else if (map.charAt(i) != wall) {
								if (map.charAt(i - 1) == wall) { // end wall, submit
									setConstraints(mapTexture.charAt(i - 1));
									topLine.setEp(new Point(x*wallLength,y*wallLength));
									bottomLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
									if (renderTop) finalWalls.add(new Wall(topLine));
									if (renderBottom) finalWalls.add(new Wall(bottomLine));
								} 
							}
						} else if (x == width - 1) {
							if (map.charAt(i) == wall) {
								if (map.charAt(i - 1) == wall) {
									if (mapTexture.charAt(i - 1) == mapTexture.charAt(i)) { // extend wall
										setConstraints(mapTexture.charAt(i));
										topLine.setEp(new Point((x+1)*wallLength,y*wallLength));
										bottomLine.setEp(new Point((x+1)*wallLength,y*wallLength + wallLength));
										if (renderTop) finalWalls.add(new Wall(topLine));
										if (renderBottom) finalWalls.add(new Wall(bottomLine));
									} else if (mapTexture.charAt(i - 1) != mapTexture.charAt(i)) { // end wall, submit, and start new wall and end wall
										setConstraints(mapTexture.charAt(i - 1));
										topLine.setEp(new Point(x*wallLength,y*wallLength));
										bottomLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
										if (renderTop) finalWalls.add(new Wall(topLine));
										if (renderBottom) finalWalls.add(new Wall(bottomLine));
										setConstraints(mapTexture.charAt(i));
										topLine.setSp(new Point(x*wallLength,y*wallLength));
										topLine.setEp(new Point((x+1)*wallLength,y*wallLength));
										bottomLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
										bottomLine.setEp(new Point((x+1)*wallLength,y*wallLength + wallLength));
										if (renderTop) finalWalls.add(new Wall(topLine));
										if (renderBottom) finalWalls.add(new Wall(bottomLine));
									}
								} else if (map.charAt(i - 1) != wall) { // start wall and end wall submit
									setConstraints(mapTexture.charAt(i));
									topLine.setSp(new Point(x*wallLength,y*wallLength));
									topLine.setEp(new Point((x+1)*wallLength,y*wallLength));
									bottomLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
									bottomLine.setEp(new Point((x+1)*wallLength,y*wallLength + wallLength));
									if (renderTop) finalWalls.add(new Wall(topLine));
									if (renderBottom) finalWalls.add(new Wall(bottomLine));
								}
							} else if (map.charAt(i) != wall) {
								if (map.charAt(i - 1) == wall) { // end wall and submit to final
									setConstraints(mapTexture.charAt(i - 1));
									topLine.setEp(new Point((x)*wallLength,y*wallLength));
									bottomLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
									if (renderTop) finalWalls.add(new Wall(topLine));
									if (renderBottom) finalWalls.add(new Wall(bottomLine));
								}
							}
						}
					}
			}
		}
		//*/
		///*
		topLine = new Wall(img,null,null,20,true);
		bottomLine = new Wall(img,null,null,20,true);
		
		verticalLeftWalls = new ArrayList<Wall>();
		verticalRightWalls = new ArrayList<Wall>();
		
		 //*	Bottom and top of walls are switched because the top left corner is (0,0)
		 //* 
		 //* 	         .-> bottom
		 //* 		     __
		 //*	left <- |  | -> right
		 //*	        '--'    
		 //*		     '-> top
		 //*
		 //*
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if (y == 0) { // initialize lists with null values
					verticalLeftWalls.add(new Wall(img,null,null,20,true));
					verticalRightWalls.add(new Wall(img,null,null,20,true));
				}
				
				final int i = x + (y * width);
				
				if (map.charAt(i) == wall) {
					
					sameTop = true;
					sameLeft = true;
					if (x != 0) {							
						if (mapTexture.charAt(i) != mapTexture.charAt(i - 1)) sameLeft = false;
					}
					if (y != 0) {
						if (mapTexture.charAt(i) != mapTexture.charAt(i - width)) sameTop = false;
					}
					
					top = true;		
					bottom = true;	
					right = true;
					left = true;
					// Check if right and left walls are to be drawn
					if (y == 0) { // bottom is true because it's in the top row
						if (map.charAt(i + width) != ' ') top = false;
					} else if (y == height - 1) {
						if (map.charAt(i - width) != ' ') bottom = false;
					} else {
						if (map.charAt(i + width) != ' ') top = false;
						if (map.charAt(i - width) != ' ') bottom = false;
					}
					// Check if top and bottom walls are to be drawn
					if (x == 0) {
						if (map.charAt(i+1) != ' ') right = false;
					} else if (x == width - 1) {
						if (map.charAt(i - 1) != ' ') left = false;
					} else {
						if (map.charAt(i - 1) == '#') left = false;
						if (map.charAt(i + 1) == '#') right = false;
					}
					
					this.setConstraints(mapTexture.charAt(i), x);
					// IF there should be a top wall
					if (top) {
						if (sameLeft) {
							this.setConstraints(mapTexture.charAt(i), x);
							if (topLine.getSp() == null) { // create new wall
								topLine.setSp(new Point(x*wallLength,y*wallLength + wallLength));
							} else { // update ending point of existing wall
								topLine.setEp(new Point(x*wallLength,y*wallLength + wallLength));
							} 
						} else {
							if (topLine.getSp() != null) {
								setConstraints(mapTexture.charAt(i-1), x);
								topLine.setEp(new Point(x*wallLength, (y)*wallLength + wallLength));
								finalWalls.add(new Wall(topLine));
								setConstraints(mapTexture.charAt(i), x);
								topLine.setSp(new Point(x*wallLength, y*wallLength + wallLength));
								topLine.setEp(null);
							} else {
								this.setConstraints(mapTexture.charAt(i), x);
								topLine.setSp(new Point(x*wallLength,(y)*wallLength + wallLength));
							}
						}
					} else {
							if (topLine.getSp() != null) { // add to final wall list and prepair for new wall
								setConstraints(mapTexture.charAt(i-1),x);
								topLine.setEp(new Point((x)*wallLength, y*wallLength + wallLength));
								finalWalls.add(new Wall(topLine));
								setConstraints(mapTexture.charAt(i), x);
								topLine.setSp(null);
								topLine.setEp(null);
						}
					}
					this.setConstraints(mapTexture.charAt(i), x);
					// IF there should be a bottom wall
					if (bottom) {
						if (sameLeft) {
							this.setConstraints(mapTexture.charAt(i), x);
						if (bottomLine.getSp() == null) { // create new line
							bottomLine.setSp(new Point(x*wallLength,y*wallLength));
						} else { // update ending point of existing wall
							bottomLine.setEp(new Point(x*wallLength,y*wallLength));
						}
						} else {
							if (bottomLine.getSp() != null) {
								setConstraints(mapTexture.charAt(i-1), x);
								bottomLine.setEp(new Point(x*wallLength, (y)*wallLength));
								finalWalls.add(new Wall(bottomLine));
								setConstraints(mapTexture.charAt(i), x);
								bottomLine.setSp(new Point(x*wallLength, y*wallLength));
								bottomLine.setEp(null);
							} else {
								this.setConstraints(mapTexture.charAt(i), x);
								bottomLine.setSp(new Point(x*wallLength,(y)*wallLength));
							}
						}
					} else {
						if (bottomLine.getSp() != null) { // add to final wall list and prepair for new wall
							setConstraints(mapTexture.charAt(i-1),x);
							bottomLine.setEp(new Point((x)*wallLength, y*wallLength));
							finalWalls.add(new Wall(bottomLine));
							setConstraints(mapTexture.charAt(i), x);
							bottomLine.setSp(null);
							bottomLine.setEp(null);
						}
					}
					this.setConstraints(mapTexture.charAt(i), x);
					// IF there should be a right wall
					if (right) {
						if (sameTop) {
							setConstraints(mapTexture.charAt(i), x);
							if (verticalRightWalls.get(x).getSp() == null) { // create new line
								verticalRightWalls.get(x).setSp(new Point(x*wallLength + wallLength,y*wallLength));
							} else { // add to line
								verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength,y*wallLength));
							}
						} else {
							if (verticalRightWalls.get(x).getSp() != null) {
								setConstraints(mapTexture.charAt(i-width), x);
								verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength, (y)*wallLength));
								finalWalls.add(new Wall(verticalRightWalls.get(x)));
								this.setConstraints(mapTexture.charAt(i), x);
								verticalRightWalls.get(x).setSp(new Point(x*wallLength + wallLength, y*wallLength));
								verticalRightWalls.get(x).setEp(null);
							} else {
								setConstraints(mapTexture.charAt(i-width), x);
								verticalRightWalls.get(x).setSp(new Point(x*wallLength + wallLength,(y)*wallLength));
							}
						}
					} else {
						if (verticalRightWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
							setConstraints(mapTexture.charAt(i-width),x);
							verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength, y*wallLength));
							finalWalls.add(new Wall(verticalRightWalls.get(x)));
							setConstraints(mapTexture.charAt(i), x);
							verticalRightWalls.get(x).setSp(null);
							verticalRightWalls.get(x).setEp(null);
						}
						
						
					}
					this.setConstraints(mapTexture.charAt(i), x);
					// IF there should be a left wall
					if (left) {
						if (sameTop) {
							this.setConstraints(mapTexture.charAt(i), x);
							if (verticalLeftWalls.get(x).getSp() == null) { // create new line
								verticalLeftWalls.get(x).setSp(new Point(x*wallLength,y*wallLength));
							} else { // add to line
								verticalLeftWalls.get(x).setEp(new Point(x*wallLength,y*wallLength));
							}
						} else {
							if (verticalLeftWalls.get(x).getSp() != null) {
								setConstraints(mapTexture.charAt(i-width), x);
								verticalLeftWalls.get(x).setEp(new Point(x*wallLength, (y)*wallLength));
								finalWalls.add(new Wall(verticalLeftWalls.get(x)));
								this.setConstraints(mapTexture.charAt(i), x);
								verticalLeftWalls.get(x).setSp(new Point(x*wallLength, y*wallLength));
								verticalLeftWalls.get(x).setEp(null);
							} else {
								this.setConstraints(mapTexture.charAt(i), x);
								verticalLeftWalls.get(x).setSp(new Point(x*wallLength,(y)*wallLength));
							}
						}
					} else {
						if (verticalLeftWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
							setConstraints(mapTexture.charAt(i - width),x);
							verticalLeftWalls.get(x).setEp(new Point(x*wallLength, y*wallLength));
							finalWalls.add(new Wall(verticalLeftWalls.get(x)));
							setConstraints(mapTexture.charAt(i), x);
							verticalLeftWalls.get(x).setSp(null);
							verticalLeftWalls.get(x).setEp(null);
						}
					}
					// IF x row has ended, end current wall and prepair for new wall
					if (x == width - 1) {
						// IF there is a current wall being built add final end point and add to list of final walls
						if (bottomLine.getSp() != null) {
							bottomLine.setEp(new Point((x+1)*wallLength, y*wallLength)); // get final end point
							finalWalls.add(new Wall(bottomLine)); // add to final wall list
							bottomLine.setSp(null); // reset wall for new one
							bottomLine.setEp(null);
						}
						if (topLine.getSp() != null) {
							topLine.setEp(new Point((x+1)*wallLength, y*wallLength + wallLength)); // get final end point
							finalWalls.add(new Wall(topLine)); // add to final wall list
							topLine.setSp(null); // reset wall for new one
							topLine.setEp(null);
						}
					}
					// IF y column has ended, end current wall and add to list of final walls
					if (y == height - 1) {
						//
						if (verticalRightWalls.get(x).getSp() != null) {
							verticalRightWalls.get(x).setEp(new Point((x)*wallLength + wallLength,(y+1)*wallLength)); // get final end point
							finalWalls.add(new Wall(verticalRightWalls.get(x))); // add to final wall list
						}
						if (verticalLeftWalls.get(x).getSp() != null) {
							verticalLeftWalls.get(x).setEp(new Point((x)*wallLength,(y+1)*wallLength)); // get final end point
							finalWalls.add(new Wall(verticalLeftWalls.get(x))); // add to final wall list
						}
					}
					
				} else { // Current index is not a wall
					
					if (bottomLine.getSp() != null) { // add to wall list and prepair bottomLine
						bottomLine.setEp(new Point(x*wallLength, y*wallLength));
						finalWalls.add(new Wall(bottomLine));
						bottomLine.setSp(null);
						bottomLine.setEp(null);
					}
					if (topLine.getSp() != null) { // add to wall list and prepair bottomLine
						topLine.setEp(new Point(x*wallLength, y*wallLength + wallLength));
						finalWalls.add(new Wall(topLine));
						topLine.setSp(null);
						topLine.setEp(null);
					}
					// vertical lines below
					if (verticalLeftWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
						verticalLeftWalls.get(x).setEp(new Point(x*wallLength, y*wallLength));
						finalWalls.add(new Wall(verticalLeftWalls.get(x)));
						verticalLeftWalls.get(x).setSp(null);
						verticalLeftWalls.get(x).setEp(null);
					}
					if (verticalRightWalls.get(x).getSp() != null) { // add to wall list and prepair bottomLine
						verticalRightWalls.get(x).setEp(new Point(x*wallLength + wallLength, y*wallLength));
						finalWalls.add(new Wall(verticalRightWalls.get(x)));
						verticalRightWalls.get(x).setSp(null);
						verticalRightWalls.get(x).setEp(null);
					}
				}
				if (map.charAt(i) == 'O') {
					CircleWall temp = new CircleWall(img,new Point(x*wallLength + (wallLength/2), y*wallLength+ (wallLength/2)), 6.2);
					setConstraints(temp, mapTexture.charAt(i));
					finalWalls.add(temp);
				}
				if (map.charAt(i) == 'o') {
					CircleWall temp = new CircleWall(img,new Point(x*wallLength + (wallLength/2), y*wallLength+ (wallLength/2)), 3.1);
					setConstraints(temp, mapTexture.charAt(i));
					temp.setHeight(10);
					finalWalls.add(temp);
				}
				
			}
		}
		//*/


		return finalWalls;
	}
	
	private void setConstraints(char prevTile, int x) {
		int x1=0,y1=0,x2=0,y2=0;
		switch (prevTile) {
		case wall:
			x1 = 0; y1 = 0; x2 = 64; y2 = 64; 
			break;
		case wood:
			x1 = 256; y1 = 192; x2 = 320; y2 = 256; 
			break;
		case hitler:
			x1 = 0; y1 = 64; x2 = 64; y2 = 128; 
			break;
		case blue:
			x1 = 128; y1 = 128; x2 = 192; y2 = 192; 
			break;
		default:
			topLine.setConstraints(0, 0, 64, 64);
			bottomLine.setConstraints(0, 0, 64, 64);
			verticalLeftWalls.get(x).setConstraints(0, 0, 64, 64);
			verticalRightWalls.get(x).setConstraints(0, 0, 64, 64);
			break;
		}
		topLine.setConstraints(x1, y1, x2, y2);
		bottomLine.setConstraints(x1, y1, x2, y2);
		verticalLeftWalls.get(x).setConstraints(x1, y1, x2, y2);
		verticalRightWalls.get(x).setConstraints(x1, y1, x2, y2);
	}
	
	private void setConstraints(char prevTile) {
		int x1=0,y1=0,x2=0,y2=0;
		switch (prevTile) {
		case wall:
			x1 = 0; y1 = 0; x2 = 64; y2 = 64; 
			break;
		case wood:
			x1 = 256; y1 = 192; x2 = 320; y2 = 256; 
			break;
		case hitler:
			x1 = 0; y1 = 64; x2 = 64; y2 = 128; 
			break;
		case blue:
			x1 = 128; y1 = 128; x2 = 192; y2 = 192; 
			break;
		default:
			x1 = 0; y1 = 0; x2 = 64; y2 = 64; 
			break;
		}
		topLine.setConstraints(x1, y1, x2, y2);
		bottomLine.setConstraints(x1, y1, x2, y2);
	}
	
	private void setConstraints(Wall wall, char tile) {
		int x1=0,y1=0,x2=0,y2=0;
		switch (tile) {
		case '#':
			x1 = 0; y1 = 0; x2 = 64; y2 = 64; 
			break;
		case 'w':
			x1 = 256; y1 = 192; x2 = 320; y2 = 256; 
			break;
		case 'h':
			x1 = 0; y1 = 64; x2 = 64; y2 = 128; 
			break;
		case 'b':
			x1 = 128; y1 = 128; x2 = 192; y2 = 192; 
			break;
		default:
			x1 = 0; y1 = 0; x2 = 64; y2 = 64; 
			break;
		}
		wall.setConstraints(x1, y1, x2, y2);
	}
}
