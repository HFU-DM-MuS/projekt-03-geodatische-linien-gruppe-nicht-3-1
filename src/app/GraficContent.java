package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

import utils.ApplicationTime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;


public class GraficContent extends Animation {

    static JButton buttonContinue = new JButton();
    static JButton buttonStop = new JButton();
    static JButton buttonPause = new JButton();

    private static void createControlFrame(ApplicationTime thread) {
        // Create a new frame
        JFrame controlFrame = new JFrame("Mathematik und Simulation");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setLayout(new GridLayout(1, 2, 10, 0)); // manages the layout of panels in the frame

        // Add a JPanel as the new drawing surface
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 4, 5, 0)); // manages the layout of elements in the panel (buttons, labels,
        // other panels, etc.)
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(2, 2, 5, 5));
        controlFrame.add(panel);
        controlFrame.add(scrollPanel);
        controlFrame.setVisible(true);

        // set up first Panel
        buttonContinue = new JButton();
        buttonContinue.setBackground(Color.WHITE);
        buttonContinue.addActionListener(new ControlButtons2(buttonContinue, controlFrame, thread));
        buttonContinue.setText("Continue");

        buttonStop = new JButton();
        buttonStop.setBackground(Color.WHITE);
        buttonStop.addActionListener(new ControlButtons2(buttonStop, controlFrame, thread));
        buttonStop.setText("Stop (forever)");

        buttonPause = new JButton();
        buttonPause.setBackground(Color.WHITE);
        buttonPause.addActionListener(new ControlButtons2(buttonPause, controlFrame, thread));
        buttonPause.setText("Pause");

        panel.add(buttonContinue);
        panel.add(buttonStop);
        panel.add(buttonPause);

        // set up second panel
        JLabel scrollLabel = new JLabel("Adjust time scaling:");
        JLabel timeScalingLabel = new JLabel("Current scaling :");

        JLabel currentScaling = new JLabel(String.format("%d", (int) GraficContentPanel.alpha));
        JSlider scrollBar = new JSlider(0, 360, (int) GraficContentPanel.alpha);
        scrollBar.addChangeListener(e -> {
            GraficContentPanel.setAlpha(scrollBar.getValue());


        });


        scrollPanel.add(scrollLabel);
        scrollPanel.add(scrollBar);

        scrollPanel.add(timeScalingLabel);
        scrollPanel.add(currentScaling);
        controlFrame.pack();

    }


    @Override
    protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
        // a list of all frames (windows) that will be shown
        ArrayList<JFrame> frames = new ArrayList<>();

        // Create main frame (window)
        JFrame frame = new JFrame("Mathematik und Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new GraficContentPanel(applicationTimeThread);
        frame.add(panel);
        frame.pack(); // adjusts size of the JFrame to fit the size of it's components
        frame.setVisible(true);

        frames.add(frame);
        createControlFrame(applicationTimeThread);
        return frames;
    }

}

class ControlButtons2 implements ActionListener {
    JButton button;
    JFrame frame;
    ApplicationTime applicationTimeThread;

    public ControlButtons2(JButton button, JFrame frame, ApplicationTime applicationTimeThread) {
        this.button = button;
        this.frame = frame;
        this.applicationTimeThread = applicationTimeThread;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if (button.equals(GraficContent.buttonPause)) {
            System.out.println("Pause pressed");
        } else if (button.equals(GraficContent.buttonStop)) {

            System.out.println("Stop pressed, thread ended");
        } else if (button.equals(GraficContent.buttonContinue)) {

            GraficContentPanel.drawLine();
        }
    }
}


class GraficContentPanel extends JPanel {
    public static void setAlpha(double a) {

        alpha = Math.toRadians(a);

    }

    // panel has a single time tracking thread associated with it
    private final ApplicationTime t;

    private double time;

    public GraficContentPanel(ApplicationTime thread) {
        this.t = thread;
    }

    public static void drawLine() {


    }

    // set this panel's preferred size for auto-sizing the container JFrame
    public Dimension getPreferredSize() {
        return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
    }

    public static double Pos1[];
    public static double Pos2[];

    public static double Pos1Final[];
    public static double Pos2Final[];
    double radius = 1;
    double startX = 20;
    double startY = 20;
    double vX = 160;
    double vY = 20;
    double currentX = startX;
    double currentY = startY;
    int diameter = 50;
    public int originX = 0;
    public int originY = 0;
    public static double alpha = Math.toRadians(120);

    // drawing operations should be done in this method
    @Override
    protected void paintComponent(Graphics g) {
        originX = this.getWidth() / 2;
        originY = this.getHeight() / 2;
        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D) g;

        double scaleFactor = 200.0;
        double s1 = scaleFactor * (1.0 / Math.sqrt(2.0));
        double s2 = scaleFactor * 1.0;
        double s3 = scaleFactor * 1.0;


        double M[][] = {{-s1 * Math.sin(alpha), s2, 0, 1},
                {-s1 * Math.cos(alpha), 0, -s3, 1}};
        double E1h[] = {1.0, 0.0, 0.0, 1.0};
        double E2h[] = {0.0, 1.0, 0.0, 1.0};
        double E3h[] = {0.0, 0.0, 1.0, 1.0};
        double Originh[] = {0.0, 0.0, 0.0, 1.0};


        //method multiplication of matrix-vector
        double xAxisEndPoint[] = multMatVec(M, E1h);
        double yAxisEndPoint[] = multMatVec(M, E2h);
        double zAxisEndPoint[] = multMatVec(M, E3h);


        super.paintComponent(g);
        time = t.getTimeInSeconds();


        //Draw Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        //set Line Width
        g2d.setStroke(new BasicStroke(3.0f));
        //draw E'2
        g.setColor(Color.GREEN);
        g.drawLine(originX, originY, originX + (int) yAxisEndPoint[0], originY + (int) yAxisEndPoint[1]);
        //draw E'3
        g.setColor(Color.BLUE);
        g.drawLine(originX, originY, originX + (int) zAxisEndPoint[0], originY + (int) zAxisEndPoint[1]);
        //draw E'1
        g.setColor(Color.RED);
        g.drawLine(originX, originY, originX + (int) xAxisEndPoint[0], originY + (int) xAxisEndPoint[1]);

        // Draw the longitudinal lines
        g2d.setStroke(new BasicStroke(1.0f));
        g.setColor(Color.GRAY);
        for (double i = 0; i <= 1; i += 0.001) {
            double theta = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {

                double phi = j * Math.PI;
                double Kugel[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta)
                };
                double longitud[] = multMatVec(M, Kugel);
                g2d.drawOval((int) longitud[0] + originX, (int) longitud[1] + originY, 1, 1);
            }
        }
        // the latitudinal lines

        for (double i = 0; i <= 1; i += 0.001) {
            double phi = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {
                double theta = j * 2 * Math.PI;

                double Kugel[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta)
                };
                double Latitud[] = multMatVec(M, Kugel);
                g2d.drawOval((int) Latitud[0] + originX, (int) Latitud[1] + originY, 1, 1);
            }
        }


        Pos1 = new double[]{35, 90};
        Pos2 = new double[]{90, 35};
        Pos1Final = multMatVec(M, Pos1);
        Pos2Final = multMatVec(M, Pos2);
        g.setColor(Color.yellow);

        double Pos1Lat = Math.toRadians(38);
        double Pos1Lon = Math.toRadians(56);
        double Pos1M[] = {radius * Math.cos(Pos1Lon) * Math.cos(Pos1Lat),
                radius * Math.cos(Pos1Lon) * Math.sin(Pos1Lat),
                radius * Math.sin(Pos1Lon)
        };
        double Pos1Coord[] = multMatVec(M, Pos1M);
        g2d.drawOval((int) Pos1Coord[0] + originX, (int) Pos1Coord[1] + originY, 2, 2);

        g.setColor(Color.green);
        double Pos2Lat = Math.toRadians(0);
        double Pos2Lon = Math.toRadians(45);
        double Pos2M[] = {radius * Math.cos(Pos2Lon) * Math.cos(Pos2Lat),
                radius * Math.cos(Pos2Lon) * Math.sin(Pos2Lat),
                radius * Math.sin(Pos2Lon)
        };
        double Pos2Coord[] = multMatVec(M, Pos2M);
        g2d.drawOval((int) Pos2Coord[0] + originX, (int) Pos2Coord[1] + originY, 2, 2);

        g2d.drawLine((int) Pos1Coord[0] + originX, (int) Pos1Coord[1] + originY, (int) Pos2Coord[0] + originX, (int) Pos2Coord[1] + originY);
    }


    private double[] multMatVec(double[][] m, double[] e1h) {

        double[] ResultMatrix = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < e1h.length; j++) {
                ResultMatrix[i] += m[i][j] * e1h[j];
            }

        }
        return ResultMatrix;

    }
}


