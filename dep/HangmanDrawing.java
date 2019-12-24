import java.awt.Color;
import javax.swing.*;
import java.awt.Graphics;
import java.util.Random;
public class HangmanDrawing extends JFrame{
    private static final long serialVersionUID = 42l;

    public HangmanDrawing(){
        super("Hangman");
        this.setSize(400,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        paint(getGraphics());
        /*If the component is null, or the GraphicsConfiguration
          associated with this component is null, the window is placed
          in the center of the screen. The center point can be obtained with
           the GraphicsEnvironment.getCenterPoint method.
        */
    }

    public void paint(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(0,0,400,600);
        g.setColor(Color.BLACK);
        g.drawOval(100,100,200,200);
        g.drawLine(200,300,200,475);

        g.drawLine(200,475,125,550);
        g.drawLine(200,475,275,550);

        g.drawLine(200,475-150,125,550-150);
        g.drawLine(200,475-150,275,550-150);


        g.drawLine(50,600,50,50);

        g.drawLine(25,75,200-12,75);

        g.drawOval(175+12,50+12,25,25);
        g.drawLine(200,75+12,200,100);



    }
    public static void main(String[] args) {
        new HangmanDrawing();
    }
}
