import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Asteroid implements MoveableShape {

    private ImageIcon meteor;
    private JPanel canvas;
    private int x, y;
    private int PANEL_WIDTH;
    private Size asteroid_size;
    private int score;

    public Asteroid (JPanel canvas, int x, int y) {
        this.canvas = canvas;

        Size sizes[] = Size.values();

        Random rand = new Random();
        int integer = rand.nextInt(2);

        asteroid_size = sizes[integer];

        try{
            BufferedImage scalable_image = ImageIO.read(new File("meteorite.png"));

            Image image = scalable_image.getScaledInstance(100,100, Image.SCALE_DEFAULT);

            switch(asteroid_size) {
                case NORMAL:
                    image = scalable_image.getScaledInstance(100,100, Image.SCALE_DEFAULT);
                    score = 100;
                    break;
                case LARGE:
                    image = scalable_image.getScaledInstance(200,200, Image.SCALE_DEFAULT);
                    score = 200;
                    break;
                default:
                    System.out.println("Switch error.");
                    break;
            }

            meteor = new ImageIcon(image);

            PANEL_WIDTH = x;

            this.x = x; //canvas.getWidth()/2;
            this.y = y; //(int)(Math.random() * canvas.getHeight());
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }
    @Override
    public void draw(Graphics2D g2) {
        meteor.paintIcon(canvas, g2, x, y);
    }

    @Override
    public void translate(int dx, int dy) {
        if(this.x < -200) //reinitialize the position of the asteroid once it is no longer visible to the player.
            this.x = PANEL_WIDTH;

        this.x += dx;
        this.y += dy;



    }

    public int getHeight() {
        return meteor.getIconHeight();
    }

    public int getWidth(){
        return meteor.getIconWidth();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
