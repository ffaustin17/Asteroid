//GUI tester of the whole program. The program ends when the close button is clicked

import javax.swing.*;
import java.awt.*;

public class Tester {
    public static void main(String[] args) {


        JFrame frame = new JFrame("Fabrice Faustin's Asteroid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    }





}
