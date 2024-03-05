import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Laser implements MoveableShape{

    private int x, y;

    private Spaceship originator_ship;

    private JPanel canvas;

    private ImageIcon laser;

    private boolean laser_fired;

    //determines if the laser is a projectile on the screen. Helps with establishing if we need to keep its starting y coordinate for the rest of its travel
    private boolean laser_in_motion = false;
    public Laser(JPanel canvas, Spaceship spaceship){
        this.laser_fired = false;
        this.canvas = canvas;
        this.originator_ship = spaceship;


        try {
            BufferedImage scalable_image = ImageIO.read(new File("laser.png")); //read the laser image file

            Image image = scalable_image.getScaledInstance(75, 50, Image.SCALE_DEFAULT); //resize the laser to our preferences

            laser = new ImageIcon(image);

            this.x = originator_ship.getX() + originator_ship.getWidth();  //the laser's starting x_coordinate must always be in front of the spaceship it originates from
            this.y = originator_ship.getY(); //similarly, the y_coordinate of the laser must be on the vertical line where the spaceship is located.

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        laser.paintIcon(canvas, g2, x, y);
    }

    @Override
    public void translate(int dx, int dy) {
        this.x += dx;

        if(laser_fired) {
            if(!laser_in_motion) {
                this.y = originator_ship.getY();
                x = originator_ship.getX() + originator_ship.getWidth();
            }


            laser_in_motion = true;

        }



        if(x> 1000){  //we reinitialize the laser once it is for sure no longer visible on the screen
            laser_fired = false;
            x = originator_ship.getX() + originator_ship.getWidth();
            laser_in_motion = false;
        }



    }

    public void setLaser_fired(boolean value){
        this.laser_fired = value;
    }

    public boolean getLaser_fired(){
        return laser_fired;
    }

    public int getWidth(){
        return laser.getIconWidth();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
