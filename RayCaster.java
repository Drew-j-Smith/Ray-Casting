import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RayCaster {

    private double fov = 120;
    private int numRays = 800;
    private int range = 10000;
    private boolean renderingRays = false; //top down view
    private boolean renderingWalls = false; //top down view
    private boolean renderingTextures = true; //3d view
    private boolean renderingLines = false; //3d view
    private boolean renderingOutline = true; //outlines walls
    private double xAngle = -90;
    private double yAngle = 0;
    private Point panelSize;
    private double wallHeight = 4000;
    private double renderPlaneAdjacentDist = 1;
    private double renderHeight = 1200;
  
    private List<Lineseg> walls;
    private Ray[] rays;
    int arrIntersections[] = new int[numRays];
    private Point center; // position

    public RayCaster(){
      
        walls = new ArrayList<Lineseg>();
        rays = new Ray[numRays];
        for (int i = 0; i < numRays; i++) {
            rays[i] = new Ray(range, renderingRays);
        }
        center = new Point();
    }

    public void castRays(Graphics g, BufferedImage img) {
      
        g.setColor(new Color(255,255,255,15));
        for (int i = 0; i < numRays; i++) {
            double angleAdded;
            double centralAngle = fov /2;
            int distanceFromCenter = (int)(numRays / 2. - i);
            double renderPlaneOppDist = distanceFromCenter * fov /45. * renderPlaneAdjacentDist / numRays;
            angleAdded = centralAngle - Math.toDegrees(Math.atan( renderPlaneOppDist / renderPlaneAdjacentDist));
            while (xAngle + angleAdded >= 270)
                xAngle = xAngle - 360;
            while (xAngle + angleAdded <= -90)
                xAngle = xAngle + 360;
            arrIntersections[i] = rays[i].cast(center, xAngle + angleAdded, walls, g);

        }

        if (renderingWalls) {
            g.setColor(new Color(255,255,255,255));
            for (Lineseg l : walls) {
                l.draw(g);
            }
        }

        double picturePosition = 1;
        double yOffset = yAngle/90 * getPanelSize().getY();
        int beginningOfWall = -1;

        for (int i = 0; i < numRays; i++) {
            if (rays[i].getDistance() < range - 1) {

                double fixedDistance = getFixedDistance(i);
                double distanceAboveCenter = getDistanceAboveCenter(i);
                double distanceBelowCenter = getDistanceBelowCenter(i);


                if (renderingLines) {
                    int shade = (int) (255. / (Math.pow(fixedDistance, .4) + 1));
                    g.setColor(new Color(
                            shade,
                            shade,
                            shade));
                    g.drawLine(i, (int) (getPanelSize().getY() + yOffset) / 2 - (int) distanceAboveCenter,
                            i, (int) (getPanelSize().getY() + yOffset) / 2 + (int) distanceBelowCenter);
                }

                while (picturePosition > 64)
                    picturePosition = picturePosition - 64;



                if (i == 0 || arrIntersections[i] != arrIntersections[i - 1]) {
                    if (renderingTextures) {


                        picturePosition = 1;
                        Lineseg wallToSp = new Lineseg(rays[i].getEp(), walls.get(arrIntersections[i]).getSp());
                        if (i != numRays - 1 && arrIntersections[i] == arrIntersections[i + 1]) { //used to find if the sp or wp is on the left side of the screen
                            Lineseg wallToEp = new Lineseg(rays[i].getEp(), walls.get(arrIntersections[i]).getEp());

                            Lineseg nextWallToSp = new Lineseg(rays[i + 1].getEp(), walls.get(arrIntersections[i]).getSp());
                            if (wallToSp.getDistance() > nextWallToSp.getDistance())
                                wallToSp = wallToEp;
                        }
                        double changeInPicPos = (wallToSp.getDistance() * 5.) % 64.;
                        g.drawImage(img, i, (int) (getPanelSize().getY() + yOffset) / 2 - (int) distanceAboveCenter,
                                i + 1, (int) (getPanelSize().getY() + yOffset) / 2 + (int) distanceBelowCenter,
                                (int) picturePosition, 1, (int) (picturePosition + changeInPicPos) + 1, 64, null);
                        picturePosition = picturePosition + changeInPicPos;
                    }

                    if (renderingOutline) {
                        g.setColor(new Color(255, 0, 0));
                        g.drawLine(i, (int) (getPanelSize().getY() + yOffset) / 2 - (int) distanceAboveCenter,
                                i, (int) (getPanelSize().getY() + yOffset) / 2 + (int) distanceBelowCenter);
                        if (beginningOfWall != -1){
                            renderOutline(i-1, beginningOfWall, g);
                        }
                        beginningOfWall = i;

                    }
                    //if(arrIntersections[i-1] != -1)
                    //g.drawLine();
                } else {
                    if(renderingTextures) {

                        Lineseg temp = new Lineseg(rays[i].getEp(), rays[i - 1].getEp());
                        double temp2 = 64;
                        temp2 = (temp.getDistance() * 5.) % 64;
                        g.drawImage(img, i, (int) (getPanelSize().getY() + yOffset) / 2 - (int) distanceAboveCenter,
                                i + 1, (int) (getPanelSize().getY() + yOffset) / 2 + (int) distanceBelowCenter,
                                (int) picturePosition, 1, (int) (picturePosition + temp2) + 1, 64, null);
                        picturePosition = picturePosition + temp2;
                    }
                }
            }
            else if (i != 0 && arrIntersections[i] != arrIntersections[i - 1] && renderingOutline && arrIntersections[i-1] != -1) {
                renderOutline(i-1, beginningOfWall, g);
                beginningOfWall = -1;
            }
        }
        if (renderingOutline && numRays != 0 && arrIntersections[numRays - 1] != -1) {
            renderOutline(numRays - 1, beginningOfWall, g);
        }
    }



    private void renderOutline(int rayIndex, int beginningOfWall, Graphics g){
        double yOffset = yAngle/90 * getPanelSize().getY();
        g.setColor(new Color(255, 0, 0));
        g.drawLine(rayIndex, (int) (getPanelSize().getY() + yOffset) / 2 - (int) getDistanceAboveCenter(rayIndex),
                rayIndex, (int) (getPanelSize().getY() + yOffset) / 2 + (int) getDistanceBelowCenter(rayIndex));
        g.setColor(new Color(0, 255, 0));
        g.drawLine(beginningOfWall, (int) (getPanelSize().getY() + yOffset) / 2 - (int) getDistanceAboveCenter(beginningOfWall),
                rayIndex, (int) (getPanelSize().getY() + yOffset) / 2 - (int) getDistanceAboveCenter(rayIndex));
        g.setColor(new Color(0, 0, 255));
        g.drawLine(beginningOfWall, (int) (getPanelSize().getY() + yOffset) / 2 + (int) getDistanceBelowCenter(beginningOfWall),
                rayIndex, (int) (getPanelSize().getY() + yOffset) / 2 + (int) getDistanceBelowCenter(rayIndex));
    }


    private double getFixedDistance(int rayIndex){
        return rays[rayIndex].getDistance() * Math.cos(Math.toRadians(rays[rayIndex].getAngle() - getCentralAngle()));
    }

    private double getDistanceAboveCenter(int rayIndex){
        //2d perspective with y as height as x as distance
        Point center = new Point(0,-wallHeight/2. + renderHeight);
        Point renderPlane = new Point(renderPlaneAdjacentDist, wallHeight/2.);
        return new Lineseg(center , renderPlane).getDistance() * renderPlaneAdjacentDist/getFixedDistance(rayIndex);
    }

    private double getDistanceBelowCenter(int rayIndex){
        //2d perspective with y as height as x as distance
        Point center = new Point(0,-wallHeight/2. + renderHeight);
        Point renderPlane = new Point(renderPlaneAdjacentDist, -wallHeight/2.);
        return new Lineseg(center, renderPlane).getDistance() * renderPlaneAdjacentDist/getFixedDistance(rayIndex);
    }

    public double getCentralAngle(){
        return xAngle + numRays / 2. * fov / (double) (numRays - 1);
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public List<Lineseg> getWalls() {
        return walls;
    }

    public void setWalls(List<Lineseg> walls) {
        this.walls = walls;
    }

    public Ray[] getRays() {
        return rays;
    }

    public void setRays(Ray[] rays) {
        this.rays = rays;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public int getNumRays() {
        return numRays;
    }

    public void setNumRays(int numRays) {
        this.numRays = numRays;
        rays = new Ray[numRays];
        for (int i = 0; i < numRays; i++) {
            rays[i] = new Ray(range, renderingRays);
        }
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
        for (int i = 0; i < numRays; i++) {
            rays[i].setRange(range);
        }
    }

    public boolean isRenderingRays() {
        return renderingRays;
    }

    public void setRenderingRays(boolean renderingRays) {
        this.renderingRays = renderingRays;
        for (int i = 0; i < numRays; i++) {
            rays[i].setRender(renderingRays);
        }
    }

    public boolean isRenderingWalls() {
        return renderingWalls;
    }

    public void setRenderingWalls(boolean renderingWalls) {
        this.renderingWalls = renderingWalls;
    }

    public double getxAngle() {
        return xAngle;
    }

    public void setxAngle(double xAngle) {
        this.xAngle = xAngle;
    }

    public Point getPanelSize() {
        return panelSize;
    }

    public void setPanelSize(Point panelSize) {
        this.panelSize = panelSize;
    }

    public double getWallHeight() {
        return wallHeight;
    }

    public void setWallHeight(double wallHeight) {
        this.wallHeight = wallHeight;
    }

    public double getRenderPlaneAdjacentDist() {
        return renderPlaneAdjacentDist;
    }

    public void setRenderPlaneAdjacentDist(double renderPlaneAdjacentDist) {
        this.renderPlaneAdjacentDist = renderPlaneAdjacentDist;
    }

    public double getyAngle() {
        return yAngle;
    }

    public void setyAngle(double yAngle) {
        this.yAngle = yAngle;
    }

    public double getRenderHeight() {
        return renderHeight;
    }

    public void setRenderHeight(double renderHeight) {
        this.renderHeight = renderHeight;
    }

    public boolean isRenderingTextures() {
        return renderingTextures;
    }

    public void setRenderingTextures(boolean renderingTextures) {
        this.renderingTextures = renderingTextures;
    }

    public boolean isRenderingLines() {
        return renderingLines;
    }

    public void setRenderingLines(boolean renderingLines) {
        this.renderingLines = renderingLines;
    }

    public boolean isRenderingOutline() {
        return renderingOutline;
    }

    public void setRenderingOutline(boolean renderingOutline) {
        this.renderingOutline = renderingOutline;
    }

    public int[] getArrIntersections() {
        return arrIntersections;
    }

    public void setArrIntersections(int[] arrIntersections) {
        this.arrIntersections = arrIntersections;
    }
}
