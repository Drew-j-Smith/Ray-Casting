import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) {

        JFrame theGUI = new JFrame();
        RenderPanel panel = new RenderPanel(Color.black);
        theGUI.add(panel);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src//wolf3d.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.setImg(img);

        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theGUI.setSize(800,600);
        theGUI.setVisible(true);

    }
}
