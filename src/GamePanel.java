import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

//this is the main panel of the application. Everything will happen here.
public class GamePanel extends JPanel {

    final int PANEL_WIDTH = 1000, PANEL_HEIGHT = 800;  //specify our game's window/panel dimension
    final int DELAY = 25; //we will be delaying(refreshing) the screen every 25 milliseconds
    int tempY; //random variable holding the starting ordinate of relevant ImageIcon fields.

    ArrayList<Asteroid> roids;  //List of asteroids to display. We intend to only but efficiently use 6 asteroids to minimize memory use.

    Spaceship spaceship;  //the spaceship which the player will be able to freely move up, left,down, right.
    GameOver game_over;  //game over image to indicate that the player has lost
    ArrayList<Laser> laser; //a spaceship can fire (limited number) multiple lasers, i.e there should be multiple lasers on the screen at once
    GamePanel here;  //pointer/shallow copy to the current object

    private int laser_counter; //index that helps us access specific lasers from their array
    private boolean crashed_once = false;  //determines if the game over screen should display



    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        here = this;

        roids= new ArrayList();

        tempY = (int)(Math.random() * PANEL_HEIGHT);
        roids.add(new Asteroid(this, PANEL_WIDTH, tempY));


        int counter = 0;
        int asteroid_distance = 100;

        while(counter < 6) {
            do{
                tempY = (int) (Math.random() * PANEL_HEIGHT);
            }while(!(spaceIsLegal(tempY)));

            roids.add(new Asteroid(this, PANEL_WIDTH + asteroid_distance, tempY));

            asteroid_distance += 50;
            counter++;

        }


        spaceship = new Spaceship(this, 0, 500);

        //there can be a maximum a three lasers travelling on the screen
        laser = new ArrayList<Laser>();
        laser.add(new Laser(here, spaceship));
        laser.add(new Laser(here, spaceship));
        laser.add(new Laser(here, spaceship));

        game_over = new GameOver(this);

        //create a timer with a specified delay and an embedded anonymous action listener. With this implementation,
        //the timer will perform the same computation every 25 milliseconds(the delay). In this case it will update the positions
        //of the various icons inside of the panel and refresh the panel accordingly, create a sense of animation and helping
        //with the visualization of the different logics.
        Timer t = new Timer(DELAY, new
                ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        for(Laser beam: laser) {
                            if (beam.getLaser_fired()) {
                                beam.translate(5, 0);
                            }
                        }

                        for (Asteroid roid : roids) {
                            roid.translate(-5, 0);
                            here.repaint(); //the repaint method forces the current panel to call the paint method. Note that we overrode the paint method in this class implementation. Check below for the implementation.
                        }


                        handleLaserCollision();

                    }
                });
        t.start();




        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                switch (key){
                    case KeyEvent.VK_DOWN:
                        spaceship.translate(0, 10);
                        break;
                    case KeyEvent.VK_UP:
                        spaceship.translate(0, -10);
                        break;
                    case KeyEvent.VK_LEFT:
                        spaceship.translate(-10, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        spaceship.translate(10,0);
                        break;
                    case KeyEvent.VK_SPACE:  //the spaceship shoots lasers
                        laser.get(laser_counter).setLaser_fired(true);
                        laser_counter++;
                        if(laser_counter == 2)
                            laser_counter = 0;

                        break;
                    default:
                        spaceship.translate(0,0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }


    //check to see if one of our lasers travelling in the screen has 'collided' with an asteroid (if the two images have any incidences)
    //if that is the case, the concerned laser along with their incident asteroid are both reset to their initial positions to give a sense of
    //destruction of said asteroid as well as exhaustion of the concerned laser.
    private void handleLaserCollision(){
        for(Laser beam : laser){
            if(beam.getLaser_fired()) {
                int laser_tip = beam.getX() + beam.getWidth();
                int laser_vertical_pos = beam.getY();

                for (Asteroid roid : roids) {
                    if (laser_vertical_pos >= roid.getY() && laser_vertical_pos <= roid.getY() + roid.getHeight()) {
                        if (laser_tip >= roid.getX() && laser_tip <= roid.getX() + (int) (roid.getWidth() / 2)) {
                            roid.translate(-2500, 0);
                            beam.translate(1500, 0);
                        }
                    }

                }
            }
        }
    }


    //check to see if one of our spaceship travelling in the screen has 'collided' with an asteroid (if the two images have any incidences)
    //note: current algorithm needs some modification for better accurracy.
    private boolean handleCrashWithMeteorite(){
        for (Asteroid roid : roids) {
            if (spaceship.getY() >= roid.getY() && spaceship.getY() <= roid.getY() + roid.getHeight()) {
                if ((spaceship.getX() + spaceship.getWidth() ) >= roid.getX() && spaceship.getX() + spaceship.getWidth() <= roid.getX() + (int) (roid.getWidth() / 2)) {

                    return true;
                }
            }

        }

        return false;
    }


    //overriding of the JPanel paint method to also include all of the relevant components inside of the panel.
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        super.paintComponent(g); //we call the parent's method to clear the panel

        //this is accessed only if the game is not over. We draw the different components currently in the panel/game except for the game over screen/icon
        if(!handleCrashWithMeteorite() && !crashed_once) {
            //draw/update the asteroids in the panel
            for (Asteroid roid : roids)
                roid.draw(g2);

            //draw/update the spaceship
            spaceship.draw(g2);

            //draw/update the lasers
            for (Laser beam : laser) {
                if (beam.getLaser_fired()) {
                    beam.draw(g2);
                }
            }
        }
        else { //the player has crashed an asteroid, therefore the game is lost. Display the game over screen.
            crashed_once = true;
            game_over.draw(g2);

            //note that in this implementation, we are continuously drawing the game over image over the same position for every delay, which
            //honestly is a waste of resources. We should look into finding a way to make the timer stop to make the game over screen be the final
            //image/screen of the program until it is closed.
        }







    }

    private boolean spaceIsLegal(int y){
        int upperBound;
        int lowerBound;
        for(Asteroid roid: roids)
        {
            lowerBound = roid.getY();
            upperBound = roid.getY() + roid.getWidth();

            if(y >= lowerBound && y <= upperBound)
                return false;
        }
        return true;
    }


}
