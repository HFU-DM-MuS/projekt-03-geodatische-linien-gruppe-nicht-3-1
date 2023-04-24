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


        double M[][] = {{-s1 * Math.sin(alpha), s2, 0, _0_Constants.WINDOW_WIDTH / 2},
                {-s1 * Math.cos(alpha), 0, -s3, _0_Constants.WINDOW_HEIGHT / 2}};
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
        g.drawLine(originX, originY, (int) yAxisEndPoint[0], (int) yAxisEndPoint[1]);
        //draw E'3
        g.setColor(Color.BLUE);
        g.drawLine(originX, originY, (int) zAxisEndPoint[0], (int) zAxisEndPoint[1]);
        //draw E'1
        g.setColor(Color.RED);
        g.drawLine(originX, originY, (int) xAxisEndPoint[0], (int) xAxisEndPoint[1]);

        // Draw the longitudinal lines
        g2d.setStroke(new BasicStroke(1.0f));
        g.setColor(Color.GRAY);
        for (double i = 0; i <= 1; i += 0.001) {
            double theta = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {

                double phi = j * Math.PI;
                double Kugel[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double longitud[] = multMatVec(M, Kugel);
                g2d.drawOval((int) longitud[0] , (int) longitud[1] , 1, 1);
            }
        }
        // the latitudinal lines

        for (double i = 0; i <= 1; i += 0.001) {
            double phi = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {
                double theta = j * 2 * Math.PI;

                double Kugel[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double Latitud[] = multMatVec(M, Kugel);
                g2d.drawOval((int) Latitud[0] , (int) Latitud[1] , 1, 1);
            }
        }



        g2d.setColor(Color.GREEN);

        double Pos1Horizontal_angle = Math.toRadians(0);
        double Pos1Vertical_angle = Math.toRadians(90);
        double xPos1 =Math.cos(Pos1Vertical_angle) * Math.cos(Pos1Horizontal_angle);
        double yPos1 =Math.cos(Pos1Vertical_angle) * Math.sin(Pos1Horizontal_angle);
        double zPos1 = Math.sin(Pos1Vertical_angle);
        double rPos1= Math.sqrt(Math.pow(xPos1,2)+Math.pow(yPos1,2) + Math.pow(zPos1,2));

        double[] Pos1Cartesian = {
                rPos1*xPos1,
                rPos1*yPos1,
                rPos1*zPos1,
                1
        };

        double[] Pos1 = multMatVec(M,Pos1Cartesian);

        g2d.drawOval((int) Pos1[0] , (int) Pos1[1] , 5, 5);

        g2d.setColor(Color.cyan);

        double Pos2Horizontal_angle = Math.toRadians(0);
        double Pos2Vertical_angle = Math.toRadians(0);
        double xPos2 =Math.cos(Pos2Vertical_angle) * Math.cos(Pos2Horizontal_angle);
        double yPos2 =Math.cos(Pos2Vertical_angle) * Math.sin(Pos2Horizontal_angle);
        double zPos2 = Math.sin(Pos2Vertical_angle);
        double rPos2= Math.sqrt(Math.pow(xPos2,2)+Math.pow(yPos2,2) + Math.pow(zPos2,2));

        double[] Pos2Cartesian = {
               rPos2 * xPos2,
               rPos2 * yPos2,
               rPos2 * zPos2,
                1
        };

        double[] Pos2 = multMatVec(M,Pos2Cartesian);

        g2d.drawOval((int) Pos2[0] , (int) Pos2[1] , 5, 5);


        double[] VectorO_Pos1 = {10, 12};
        double[] VectorO_Pos2 = {10, 12};
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


