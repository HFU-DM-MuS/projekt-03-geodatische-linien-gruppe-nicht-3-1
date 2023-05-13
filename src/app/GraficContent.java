package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

import utils.ApplicationTime;


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
        JLabel scrollLabel = new JLabel("Adjust alpha");
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
    public int originX = 0;
    public int originY = 0;
    public static double alpha = Math.toRadians(120);

    // drawing operations should be done in this method
    @Override
    protected void paintComponent(Graphics g) {
        originX = _0_Constants.WINDOW_WIDTH / 2;
        originY = _0_Constants.WINDOW_HEIGHT / 2;
        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D) g;

        double scaleFactor = 200.0;
        double s1 = scaleFactor * (1.0 / Math.sqrt(2.0));
        double s2 = scaleFactor * 1.0;
        double s3 = scaleFactor * 1.0;


        double[][] projectionMatrix = {
                {-s1 * Math.sin(alpha), s2, 0, originX},
                {-s1 * Math.cos(alpha), 0, -s3, originY}
                        };
        double[] e1H = {1.0, 0.0, 0.0, 1.0};
        double[] e2H = {0.0, 1.0, 0.0, 1.0};
        double[] e3H = {0.0, 0.0, 1.0, 1.0};
        double[] originH = {0.0, 0.0, 0.0, 1.0};


        //method multiplication of matrix-vector -> now we can project the Axes correctly
        double[] xAxisEndPoint = multMatVec(projectionMatrix, e1H);
        double[] yAxisEndPoint = multMatVec(projectionMatrix, e2H);
        double[] zAxisEndPoint = multMatVec(projectionMatrix, e3H);


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
                double sphere[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double longitud[] = multMatVec(projectionMatrix, sphere);
                g2d.drawOval((int) longitud[0] , (int) longitud[1] , 1, 1);
            }
        }
        // Draw the latitudinal lines

        for (double i = 0; i <= 1; i += 0.001) {
            double phi = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {
                double theta = j * 2 * Math.PI;

                double sphere[] = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double latitud[] = multMatVec(projectionMatrix, sphere);
                g2d.drawOval((int) latitud[0] , (int) latitud[1] , 1, 1);
            }
        }


        //Make a point on a desired position of the sphere
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.setColor(Color.GREEN);
        double pos1Horizontal_angle = Math.toRadians(0);
        double pos1Vertical_angle = Math.toRadians(90);

        double xPos1 =Math.cos(pos1Vertical_angle) * Math.cos(pos1Horizontal_angle);
        double yPos1 =Math.cos(pos1Vertical_angle) * Math.sin(pos1Horizontal_angle);
        double zPos1 = Math.sin(pos1Vertical_angle);

        double rPos1= Math.sqrt(Math.pow(xPos1,2)+Math.pow(yPos1,2) + Math.pow(zPos1,2));

        double[] pos1Cartesian = {
                rPos1*xPos1,
                rPos1*yPos1,
                rPos1*zPos1,
                1
        };

        double[] pos1 = multMatVec(projectionMatrix,pos1Cartesian);

        g2d.drawOval((int) pos1[0] , (int) pos1[1] , 5, 5);

        //Make a point on a desired position of the sphere
        g2d.setColor(Color.cyan);
        double pos2Horizontal_angle = Math.toRadians(45);
        double pos2Vertical_angle = Math.toRadians(45);

        double xPos2 =Math.cos(pos2Vertical_angle) * Math.cos(pos2Horizontal_angle);
        double yPos2 =Math.cos(pos2Vertical_angle) * Math.sin(pos2Horizontal_angle);
        double zPos2 = Math.sin(pos2Vertical_angle);

        double rPos2= Math.sqrt(Math.pow(xPos2,2)+Math.pow(yPos2,2) + Math.pow(zPos2,2));

        double[] pos2Cartesian = {
               rPos2 * xPos2,
               rPos2 * yPos2,
               rPos2 * zPos2,
                1
        };

        double[] pos2 = multMatVec(projectionMatrix,pos2Cartesian);
        g2d.drawOval((int) pos2[0] , (int) pos2[1] , 5, 5);


        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************DANGER ZONE*************************************************
        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************************************************************************
        //**************************************************************************************************


        double[] vectorO_Pos1 = subtractVectors(pos1,multMatVec(projectionMatrix,originH));
        double[] vectorO_Pos2 = subtractVectors(pos2,multMatVec(projectionMatrix,originH));
        double delta = Math.acos(cosineOfAngleBetweenVectors(vectorO_Pos1,vectorO_Pos2)) ;

        double distance = radius * delta;
        g2d.setStroke(new BasicStroke(1.0f));


       double[] unitVectorP =  normalizeVector(vectorO_Pos1);
       double[] unitVectorN = divVecWithNumber(crossProduct(vectorO_Pos1, vectorO_Pos2),crossProductMagnitude(vectorO_Pos1,vectorO_Pos2));
       double[] unitVectorU = divVecWithNumber(crossProduct(unitVectorN, unitVectorP),crossProductMagnitude(unitVectorN,unitVectorP));

      /* double[] testP = multMatVec(projectionMatrix,unitVectorP);
       double[] testN = multMatVec(projectionMatrix,unitVectorN);
       double[] testU = multMatVec(projectionMatrix,unitVectorU);*/


        /*double[][] transMatrixD = {
                {0,1,0},
                {1,0,0},
                {0,0,1}
        };*/
       double[][] transMatrixD = {
               {unitVectorP[0],unitVectorU[0],unitVectorN[0]},
               {unitVectorP[1],      unitVectorU[1]       ,unitVectorN[1]},
               {unitVectorP[2], unitVectorU[2],unitVectorN[2]}

       };

       for(double t = 0 ; t<= delta ; t+=0.01){

           double[] geodeticCurve =  {
                   radius * Math.cos(t) ,
                   radius * Math.sin(t),
                   1,
           };

           double[] testVector = multMatVec(transMatrixD,geodeticCurve);
           double[] test2Vector = {testVector[0],testVector[1],testVector[2],1};
           double [] testMitProj = multMatVec(projectionMatrix,test2Vector);

           //double[] test = multMatVec(projectionMatrix,geodeticCurve);

           //double [] testMitProj = multMatVec(transMatrixD,test);


           //double[]miau =  addVectors(multVecWithNumber(unitVectorP, (radius*Math.cos(t))),multVecWithNumber(unitVectorU, (radius*Math.sin(t))));

           g2d.drawOval((int) testMitProj[0]  , (int) testMitProj[1] , 5, 5);

       }


    }


    private double[] multMatVec(double[][] m, double[] v) {

        double[] ResultMatrix = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ResultMatrix[i] += m[i][j] * v[j];
            }
        }
        return ResultMatrix;

    }

    public static double[] subtractVectors(double[] vector1, double[] vector2) {

        double[] result = new double[3];
        result[2] = 1;
        for (int i = 0; i <= vector1.length-1; i++) {
            result[i] = vector1[i] - vector2[i];
        }
        return result;
    }
    public static double cosineOfAngleBetweenVectors(double[] p, double[] q) {
        double dotProduct = 0;
        double magnitudeP = 0;
        double magnitudeQ = 0;
        for (int i = 0; i <= p.length-1; i++) {
            dotProduct += p[i] * q[i];
            magnitudeP += p[i] * p[i];
            magnitudeQ += q[i] * q[i];
        }
        magnitudeP = Math.sqrt(magnitudeP);
        magnitudeQ = Math.sqrt(magnitudeQ);
        return dotProduct / (magnitudeP * magnitudeQ);

    }
    public static double[] normalizeVector(double[] p) {
        double magnitude = 0;
        for (int i = 0; i <= p.length-1; i++) {
            magnitude += p[i] * p[i];
        }
        magnitude = Math.sqrt(magnitude);

        double[] normalized = new double[p.length];
        for (int i = 0; i <= p.length-1; i++) {
            normalized[i] = p[i] / magnitude;
        }
        return normalized;
    }
    public static double[] crossProduct(double[] p, double[] q) {

        double[] result = new double[p.length];
        result[0] = p[1] * q[2] - p[2] * q[1];
        result[1] = p[2] * q[0] - p[0] * q[2];
        result[2] = p[0] * q[1] - p[1] * q[0];
        return result;
    }
    public static double crossProductMagnitude(double[] p, double[] q) {

        double[] result = new double[p.length];
        result[0] = p[1] * q[2] - p[2] * q[1];
        result[1] = p[2] * q[0] - p[0] * q[2];
        result[2] = p[0] * q[1] - p[1] * q[0];
        double magnitude = Math.sqrt(result[0] * result[0] + result[1] * result[1] + result[2] * result[2]);
        return magnitude;
    }
    public static double[] divVecWithNumber(double[] p, double q){

        double[] result = new double[p.length];

        for(int i = 0; i <= p.length-1; i++){

            result[i] = p[i]/q;

        }

        return result;
    }
    public static double[] multVecWithNumber(double[] p, double q){

        double[] result = new double[p.length];

        for(int i = 0; i <= p.length-1; i++){

            result[i] = p[i]*q;

        }

        return result;
    }
    public static double[] addVectors(double[] v1, double[] v2)
    {// Create a new array to store the result
        double[] result = new double[v1.length];

        // Iterate over the arrays and add their corresponding elements
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] + v2[i];
        }

        // Return the result
        return result;
    }
}




