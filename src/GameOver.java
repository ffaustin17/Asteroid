import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOver extends JPanel {
    JPanel canvas;
    ImageIcon game_over;
    int x, y;

    public GameOver(JPanel canvas){
        this.canvas = canvas;
        this.x = 300;
        this.y = 100;


        try {
            BufferedImage scalable_image = ImageIO.read(new File("game_over.png"));

            Image image = scalable_image.getScaledInstance(400, 400, Image.SCALE_DEFAULT);

            game_over = new ImageIcon(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        game_over.paintIcon(canvas, g2, x, y);
    }
}
