import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SphericalGridDrawer extends JPanel {

    private static final long serialVersionUID = 1L;

    // The radius of the sphere
    private double radius = 100.0;

    // The number of longitudinal and latitudinal lines to draw
    private int numLongitudes = 18;
    private int numLatitudes = 18;

    // The current rotation angles around the x and y axes
    private double rotateX = 0.0;
    private double rotateY = 0.0;

    // The previous mouse position for computing the rotation
    private Point prevMousePos = null;

    public SphericalGridDrawer() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                prevMousePos = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                prevMousePos = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (prevMousePos != null) {
                    int dx = e.getX() - prevMousePos.x;
                    int dy = e.getY() - prevMousePos.y;
                    rotateX -= dy * 0.01;
                    rotateY += dx * 0.01;
                    prevMousePos = e.getPoint();
                    repaint();
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Set the background color
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());



        // Set the color of the lines
        g2d.setColor(Color.LIGHT_GRAY);

        // Compute the transformation matrix for the 3D projection
        double cosX = Math.cos(rotateX);
        double sinX = Math.sin(rotateX);
        double cosY = Math.cos(rotateY);
        double sinY = Math.sin(rotateY);
        double[][] transform = {
                {cosY, 0, sinY},
                {sinX * sinY, cosX, -sinX * cosY},
                {-cosX * sinY, sinX, cosX * cosY}
        };

        // Draw the longitudinal lines
        // the longitudinal lines
        for (int i = 0; i < numLongitudes; i++) {
            double theta = i * 2 * Math.PI / numLongitudes;
            for (int j = 0; j < numLatitudes; j++) {
                double phi = j * Math.PI / (numLatitudes + 1);
                double[][] p1 = {
                        {radius * Math.sin(phi) * Math.cos(theta)},
                        {radius * Math.cos(phi)},
                        {radius * Math.sin(phi) * Math.sin(theta)}
                };

                double[][] p2 = {
                        {radius * Math.sin(phi + Math.PI / (numLatitudes + 1)) * Math.cos(theta)},
                        {radius * Math.cos(phi + Math.PI / (numLatitudes + 1))},
                        {radius * Math.sin(phi + Math.PI / (numLatitudes + 1)) * Math.sin(theta)}
                };
                double[][] q1 = matrixMultiply(transform, p1);
                double[][] q2 = matrixMultiply(transform, p2);
                drawLine(g2d, q1, q2);
            }
        }


        // the latitudinal lines
        for (int i = 0; i < numLatitudes; i++) {
            double phi = (i + 1) * Math.PI / (numLatitudes +1);
            for (int j = 0; j < numLongitudes; j++) {
                double theta = j * 2 * Math.PI / numLongitudes;
                double[][] p1 = {{radius * Math.sin(phi) * Math.cos(theta)},
                                 {radius * Math.cos(phi)},
                                 {radius * Math.sin(phi) * Math.sin(theta)}
                };

                double[][] p2 = {{radius * Math.sin(phi) * Math.cos(theta + 2 * Math.PI / numLongitudes)},
                                 {radius * Math.cos(phi)},
                                 {radius * Math.sin(phi) * Math.sin(theta + 2 * Math.PI / numLongitudes)}
                };

                double[][] q1 = matrixMultiply(transform, p1);
                double[][] q2 = matrixMultiply(transform, p2);
                drawLine(g2d, q1, q2);
            }
        }
    }

    // Helper method to draw a line between two 3D points
    private void drawLine(Graphics2D g2d, double[][] p1, double[][] p2) {
        int x1 = (int) (p1[0][0] / (1 - p1[2][0] / (2 * radius)) + getWidth() / 2);
        int y1 = (int) (p1[1][0] / (1 - p1[2][0] / (2 * radius)) + getHeight() / 2);
        int x2 = (int) (p2[0][0] / (1 - p2[2][0] / (2 * radius)) + getWidth() / 2);
        int y2 = (int) (p2[1][0] / (1 - p2[2][0] / (2 * radius)) + getHeight() / 2);
        g2d.drawLine(x1, y1, x2, y2);
    }

    // Helper method to multiply two matrices
    private double[][] matrixMultiply(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        int p = b[0].length;
        double[][] c = new double[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Spherical Grid Drawer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SphericalGridDrawer(), frame.getHeight());
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }}

