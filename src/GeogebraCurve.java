import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GeogebraCurve extends JPanel {

    private int width, height;
    private Point origin, bp, bu;
    private double r;

    public GeogebraCurve(int width, int height, Point bp, Point bu, double r) {
        this.width = width;
        this.height = height;
        this.origin = new Point(width/2, height/2);
        this.bp = bp;
        this.bu = bu;
        this.r = r;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        for(double t = 0; t <= 2*Math.PI; t += 0.01) {
            double x = r * Math.cos(t) * bp.getX() + r * Math.sin(t) * bu.getX() + origin.getX();
            double y = r * Math.cos(t) * bp.getY() + r * Math.sin(t) * bu.getY() + origin.getY();
            g.fillOval((int) x, (int) y, 5, 5);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        Point bp = new Point(200, 0);
        Point bu = new Point(0, 100);
        double r = 50;
        GeogebraCurve curve = new GeogebraCurve(frame.getWidth(), frame.getHeight(), bp, bu, r);
        frame.add(curve);
        frame.setVisible(true);
    }
}
