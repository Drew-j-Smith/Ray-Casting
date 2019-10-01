import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public class RenderPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener {
    private Player caster;
    //private point temp;
    private boolean[] keyCodes = new boolean[1000];
    private boolean paused = false;
    private BufferedImage img = null;

    public RenderPanel(Color backColor) {
        setBackground(backColor);

        //caster = new rayCaster();
        caster = new Player();
        caster.setCenter(new Point(500,500));
        caster.setRenderingWalls(true);
        caster.setRenderingRays(false);
        List<Lineseg> walls = caster.getWalls();
        String map = "";
        map += "  ########";
        map += "#        #";
        map += "# ###### #";
        map += "# #      #";
        map += "# # #### #";
        map += "# # #    #";
        map += "###  # # #";
        map += "#   #  # #";
        map += "# # # #  #";
        map += "##### ####";
    	LevelEditor level = new LevelEditor(map, 10, 10, 20);
    	try {
			walls.addAll(level.mapToLineseg());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
    	System.out.println(walls.size());
    	caster.setCenter(new Point(0,0));
        /*
        walls.add(new Lineseg(new Point(100,0), new Point(100,500)));
        walls.add(new Lineseg(new Point(0,100), new Point(500,100)));
        walls.add(new Lineseg(new Point(50, 500), new Point(500,50)));
        walls.add(new Lineseg(new Point(0, 0), new Point(0,600)));
        walls.add(new Lineseg(new Point(0, 0), new Point(800,0)));
        walls.add(new Lineseg(new Point(800, 0), new Point(800,800)));
        //*/
        caster.setNumRays(getWidth());
        caster.setPanelSize(new Point(getWidth(), getHeight()));

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        super.setFocusable(true);


        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setPaused(paused);
                caster.setNumRays(getWidth());
                caster.setPanelSize(new Point(e.getComponent().getSize().getWidth(), e.getComponent().getSize().getHeight()));
                repaint();
            }
        });


        new Thread(new Runnable() {
            public void run() {
                while (true) {

                    //double t = Timestamp.valueOf(LocalDateTime.now()).getTime();
                    if (!paused)
                        caster.updatePosition(keyCodes);
                    if (keyCodes[KeyEvent.VK_ESCAPE])
                        System.exit(0);

                    repaint();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(Timestamp.valueOf(LocalDateTime.now()).getTime() - t);
                }
            }
        }).start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        caster.castRays(g, img);
        caster.getCenter().draw(g);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        //temp = new point(e.getX(),e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        //List<lineseg> walls = caster.getWalls();
        //walls.add(new lineseg(temp, new point(e.getX(), e.getY())));
        //repaint();
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);

    }

    public void mouseMoved(MouseEvent e){
        if (!paused) {
            caster.setxAngle(caster.getxAngle() + (e.getX() - 100) / 10.);
            caster.setyAngle(caster.getyAngle() + (e.getY() - 100) / -5.);
            try {
                Robot robot = new Robot();
                robot.mouseMove(100 + (int)getParent().getLocationOnScreen().getX(), 100 + (int)getParent().getLocationOnScreen().getY());
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < 1000) keyCodes[e.getKeyCode()] = true;
        if (e.getKeyCode() == KeyEvent.VK_E) setPaused(!paused);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < 1000) keyCodes[e.getKeyCode()] = false;
    }

    public Player getCaster() {
        return caster;
    }

    public void setCaster(Player caster) {
        this.caster = caster;
    }

    public boolean[] getKeyCodes() {
        return keyCodes;
    }

    public void setKeyCodes(boolean[] keyCodes) {
        this.keyCodes = keyCodes;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (!paused) {
            // Transparent 16 x 16 pixel cursor image.
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            // Create a new blank cursor.
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new java.awt.Point(0, 0), "blank cursor");
            // Set the blank cursor to the JFrame.
            getParent().setCursor(blankCursor);

            try {
                Robot robot = new Robot();
                robot.mouseMove(100 + (int) getParent().getLocationOnScreen().getX(), 100 + (int) getParent().getLocationOnScreen().getY());
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }
        else{
            getParent().setCursor(Cursor.getDefaultCursor());
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
