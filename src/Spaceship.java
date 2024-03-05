import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Spaceship implements MoveableShape {
    private int x, y;
    private ImageIcon spaceship;
    private JPanel canvas;

    private int PANEL_WIDTH;


    public Spaceship (JPanel canvas, int x, int y) {
        this.canvas = canvas;



        try {
            BufferedImage scalable_image = ImageIO.read(new File("spaceship.png"));

            Image image = scalable_image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);



            spaceship = new ImageIcon(image);

            PANEL_WIDTH = x;

            this.x = x; //canvas.getWidth()/2;
            this.y = y; //(int)(Math.random() * canvas.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
    public void draw(Graphics2D g2){
        spaceship.paintIcon(canvas, g2, x, y);
    }

    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public int getWidth(){
        return spaceship.getIconWidth();
    }
}
