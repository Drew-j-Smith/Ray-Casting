import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RayCaster {
    private List<Lineseg> walls;
    private Ray[] rays;
    private Point center; // position
    private Wall testingWall;

    private double pov = 120;
    private int numRays = 800;
    private int range = 10000;
    private boolean renderingRays = false; // render 2D rays
    private boolean renderingWalls = false; // render 2D walls
    private double xAngle = -90;
    private double yAngle = 0;
    private Point panelSize; // size of renderPanel
    private double wallHeight = 4000;
    private double renderPlaneAdjacentDist = 1;
    private double renderHeight = 1200;

    public RayCaster(){
        walls = new ArrayList<Lineseg>();
        rays = new Ray[numRays];
        for (int i = 0; i < numRays; i++) {
            rays[i] = new Ray(range, renderingRays);
        }
        center = new Point();
    }

    public void castRays(Graphics g, BufferedImage img) {
    	testingWall = new Wall(img);
    	testingWall.setImgMaxHeight(63);
    	testingWall.setImgMaxWidth(63);
    	testingWall.setImgMinHeight(0);	
    	testingWall.setImgMinWidth(0);
        g.setColor(new Color(255,255,255,15));
        for (int i = 0; i < numRays; i++) {
            double angleAdded;
            //angleAdded = i * pov / (double) (numRays - 1);
            double centralAngle = pov/2;
            int distanceFromCenter = (int)(numRays / 2. - i);
            double renderPlaneOppDist = distanceFromCenter * pov/45. * renderPlaneAdjacentDist / numRays;
            angleAdded = centralAngle - Math.toDegrees(Math.atan( renderPlaneOppDist / renderPlaneAdjacentDist));
            while (xAngle + angleAdded >= 270)
                xAngle = xAngle - 360;
            while (xAngle + angleAdded <= -90)
                xAngle = xAngle + 360;
            rays[i].cast(center, xAngle + angleAdded, walls, g);
        }

        double temp = 1; // TODO rename
        for (int i = 0; i < numRays; i++){
            if (rays[i].getDistance() < range - 1) {
                double fixedDistance = rays[i].getDistance() * Math.cos(Math.toRadians(rays[i].getAngle() - getCentralAngle()));
                //double adjustedWallHeight = wallHeight * panelSize.getY()/2 / fixedDistance;
                double distanceAboveCenter = new Lineseg(new Point(0,-wallHeight/2. + renderHeight), new Point(renderPlaneAdjacentDist, wallHeight/2.)).getDistance() * renderPlaneAdjacentDist/fixedDistance;
                double distanceBelowCenter = new Lineseg(new Point(0,-wallHeight/2. + renderHeight), new Point(renderPlaneAdjacentDist, -wallHeight/2.)).getDistance() * renderPlaneAdjacentDist/fixedDistance;
                double yOffset = yAngle/90 * getPanelSize().getY();

                int shade = (int) (255. / (Math.pow(fixedDistance, .4) + 1));
                g.setColor(new Color(
                		shade,
                		shade,
                		shade
                        ));


                //g.drawLine(i, (int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter,
                //        i, (int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter);
                //g.fillRect(i, (int)(getPanelSize().getY() - adjustedWallHeight)/2,
                //                1, (int)adjustedWallHeight);
                
                /*
                g.drawImage(img, i,(int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter,
                        i+1,(int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter,
                        (int)temp,1,(int)(temp+fixedDistance/64) + 1,64, null);
                 //*/
                if (temp > 64)
                    temp = 1;
                //testingWall.draw(i,(int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter,(int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter,g);
                testingWall.draw(i,(int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter,(int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter,(int)(temp+fixedDistance/testingWall.getImgMaxWidth()),g);
                /*
                g.drawLine(i,(int)(getPanelSize().getY() + yOffset)/2 - (int)distanceAboveCenter,
                        i+1,(int)(getPanelSize().getY() + yOffset)/2 + (int)distanceBelowCenter
                        );
                */
                temp = temp + fixedDistance/64;
                
                // TODO set background for mini map
                // TODO make slightly transparent
                // TODO scale mini map
                if (renderingWalls) {
                    g.setColor(new Color(255,255,255,255));
                    for (Lineseg l : walls) {
                        l.draw(g);
                    }
                }

                //System.out.println(temp);
            }
        }
    }

    public double getCentralAngle(){
        return xAngle + numRays / 2. * pov / (double) (numRays - 1);
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

    public double getPov() {
        return pov;
    }

    public void setPov(double pov) {
        this.pov = pov;
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
}
