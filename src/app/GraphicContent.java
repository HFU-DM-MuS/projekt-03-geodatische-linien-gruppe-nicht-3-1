package app;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import utils.ApplicationTime;

class GraphicContent extends JPanel {
    public static void setAlpha(double a) {alpha = Math.toRadians(a);}
    public static void setPos1XY(double X, double Y) {
        Pos1X = Math.toRadians(X);
        Pos1Y = Math.toRadians(Y);
    }
    public static void setPos2XY(double X, double Y) {
        Pos2X = Math.toRadians(X);
        Pos2Y = Math.toRadians(Y);
    }
    public static void startAnimation() {tDelta = 0;}
    public static void setS1ScaleFactor(double x) {s1ScaleFactor = x/100;}
    private final ApplicationTime t;
    public GraphicContent(ApplicationTime thread) {
        this.t = thread;
    }
    // set this panel's preferred size for auto-sizing the container JFrame
    public Dimension getPreferredSize() {
        return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
    }
    double radius = 1;
    public int originX = _0_Constants.WINDOW_WIDTH / 2;
    public int originY = _0_Constants.WINDOW_HEIGHT / 2;
    public static double alpha = Math.toRadians(120);
    public static double Pos1X;
    public static double Pos1Y;
    public static double Pos2X;
    public static double Pos2Y;
    public static double tDelta = 0;
    public static double distance = 0;
    public static double s1ScaleFactor = 1;

    // drawing operations should be done in this method
    @Override
    protected void paintComponent(Graphics g){


        //--------------
        //Tripod
        //--------------

        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D) g;
        double scaleFactor = 200.0;
        double s1 = scaleFactor * (s1ScaleFactor / Math.sqrt(2.0));
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

        //--------------
        //Grid Sphere
        //--------------

        // Draw the longitudinal lines
        g2d.setStroke(new BasicStroke(1.0f));
        g.setColor(Color.lightGray);
        for (double i = 0; i <= 1; i += 0.001) {
            //i =: amount of dots per line
            double theta = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {
            //j=: amount of lines
                double phi = j * Math.PI;

                double[] sphere = {
                        radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double[] longitude = multMatVec(projectionMatrix, sphere);
                g2d.drawOval((int) longitude[0] , (int) longitude[1] , 1, 1);
            }
        }

        // Draw the latitudinal lines
        for (double i = 0; i <= 1; i += 0.001) {
            //i =: amount of dots per line
            double phi = i * 2 * Math.PI;
            for (double j = 0; j <= 1; j += 0.1) {
                //j=: amount of lines
                double theta = j * 2 * Math.PI;

                double[] sphere = {radius * Math.cos(theta) * Math.cos(phi),
                        radius * Math.cos(theta) * Math.sin(phi),
                        radius * Math.sin(theta),
                        1
                };
                double[] latitude = multMatVec(projectionMatrix, sphere);
                g2d.drawOval((int) latitude[0] , (int) latitude[1] , 1, 1);
            }
        }

        //The for loops calculate an specific point on the sphere and draw an Oval on that point multiple times
        //until reached the desired look

        //Make a point on a desired position of the sphere
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.setColor(Color.GREEN);

        double pos1Horizontal_angle = Pos1X; //Pos1X is defined by the user through the InputWindow
        double pos1Vertical_angle = Pos1Y; //Pos1Y is defined by the user through the InputWindow

        //Calculate the XYZ coords of Pos1 by transforming the Lat and Lon into 3d coords
        double xPos1 =Math.cos(pos1Vertical_angle) * Math.cos(pos1Horizontal_angle);
        double yPos1 =Math.cos(pos1Vertical_angle) * Math.sin(pos1Horizontal_angle);
        double zPos1 = Math.sin(pos1Vertical_angle);

        //calculate the distance from the center of the sphere to know where to draw the point
        double rPos1= Math.sqrt(Math.pow(xPos1,2)+Math.pow(yPos1,2) + Math.pow(zPos1,2));

        //transform the XYZ coords into homogeneous cartesian coords
        double[] pos1Cartesian = {
                rPos1*xPos1,
                rPos1*yPos1,
                rPos1*zPos1,
                1
        };

        //transforrm the homogenous cartesian coords into 2d coords
        double[] pos1 = multMatVec(projectionMatrix,pos1Cartesian);

        g2d.drawOval((int) pos1[0] , (int) pos1[1] , 5, 5);

        //Do the same for pos2
        g2d.setColor(Color.cyan);
        double pos2Horizontal_angle = Pos2X;
        double pos2Vertical_angle = Pos2Y;

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

        //----------------------------
        //Geodetic curve and animation
        //----------------------------

        //calculate the vectors from middle point to our desired positions
        double[] vectorO_Pos1 = subtractVectors(pos1Cartesian,originH);
        double[] vectorO_Pos2 = subtractVectors(pos2Cartesian,originH);
        double delta = Math.acos(cosineOfAngleBetweenVectors(vectorO_Pos1,vectorO_Pos2)) ; //calculate angle between both vectors

        int earthRadius = 6371; //km
        distance = Math.acos(Math.sin(Pos1X)*Math.sin(Pos2X)+Math.cos(Pos1X)*Math.cos(Pos2X)*Math.cos(Pos2Y-Pos1Y))*earthRadius; //calculate real distance
        AnimationWindowPanel.distanceP1P2.setText(String.valueOf((int) distance) + "Km");//overwrite distance in window

       g2d.setStroke(new BasicStroke(1.0f));

       //calculate unit vectors
       double[] unitVectorP =  normalizeVector(vectorO_Pos1);
       double[] unitVectorN = divVecWithNumber(crossProduct(vectorO_Pos1, vectorO_Pos2),crossProductMagnitude(vectorO_Pos1,vectorO_Pos2));
       double[] unitVectorU = divVecWithNumber(crossProduct(unitVectorN, unitVectorP),crossProductMagnitude(unitVectorN,unitVectorP));

       //define transformation Matrix using the unit vectors
       double[][] transMatrixD = {
               {unitVectorP[0],unitVectorU[0],unitVectorN[0]},
               {unitVectorP[1],unitVectorU[1],unitVectorN[1]},
               {unitVectorP[2],unitVectorU[2],unitVectorN[2]}
                                };

       //animate the flight path by using the fact Paint Component is getting called constantly
       tDelta += 0.01;
       double[] geodeticCurve =  {
                   radius * Math.cos(tDelta),
                   radius * Math.sin(tDelta),
                            0
       };

       //turn the geodetic curve to be positioned as desired
       double[] turnedCurveVector = multMatVec(transMatrixD,geodeticCurve);
       double[] homogenTCVector = {turnedCurveVector[0],turnedCurveVector[1],turnedCurveVector[2],1};
       double [] twoDHTCVector = multMatVec(projectionMatrix,homogenTCVector); //turn the homogeneous vector into a 2d vector
       g2d.setColor(Color.magenta);
       g2d.fillOval((int) twoDHTCVector[0] , (int) twoDHTCVector[1] , 10, 10);//draw an oval on the desired position
       if(tDelta >= delta) {
       //after tDelta reaches the value of delta, draw the line and stop tDelta from moving any further
       tDelta = delta;
       for(double i = 0; i<=delta;i+=0.1) {
            geodeticCurve = new double[]{
                    radius * Math.cos(i),
                    radius * Math.sin(i),
                    0
            };
            turnedCurveVector = multMatVec(transMatrixD, geodeticCurve);
            homogenTCVector = new double[]{turnedCurveVector[0], turnedCurveVector[1], turnedCurveVector[2], 1};
            twoDHTCVector = multMatVec(projectionMatrix, homogenTCVector);
            g2d.setColor(Color.magenta);
            g2d.fillOval((int) twoDHTCVector[0], (int) twoDHTCVector[1], 10, 10);
        }

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

        double[] result = new double[vector1.length];
        for (int i = 0; i <= vector1.length-1; i++) {
            result[i] = vector1[i] - vector2[i];
        }
        return result;
    }
    public static double cosineOfAngleBetweenVectors(double[] vector1, double[] vector2) {
        double dotProduct = 0;
        double magnitudeP = 0;
        double magnitudeQ = 0;
        for (int i = 0; i <= vector1.length-1; i++) {
            dotProduct += vector1[i] * vector2[i];
            magnitudeP += vector1[i] * vector1[i];
            magnitudeQ += vector2[i] * vector2[i];
        }
        magnitudeP = Math.sqrt(magnitudeP);
        magnitudeQ = Math.sqrt(magnitudeQ);
        return dotProduct / (magnitudeP * magnitudeQ);

    }
    public static double[] normalizeVector(double[] vector) {
        double magnitude = 0;
        for (int i = 0; i <= vector.length-1; i++) {
            magnitude += vector[i] * vector[i];
        }
        magnitude = Math.sqrt(magnitude);

        double[] normalized = new double[vector.length];
        for (int i = 0; i <= vector.length-1; i++) {
            normalized[i] = vector[i] / magnitude;
        }
        return normalized;
    }
    public static double[] crossProduct(double[] vector1, double[] vector2) {

        double[] result = new double[vector1.length];
        result[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        result[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        result[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return result;
    }
    public static double crossProductMagnitude(double[] vector1, double[] vector2) {

        double[] result = new double[vector1.length];
        result[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        result[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        result[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return Math.sqrt(result[0] * result[0] + result[1] * result[1] + result[2] * result[2]);

    }
    public static double[] divVecWithNumber(double[] vector, double number){

        double[] result = new double[vector.length];

        for(int i = 0; i <= vector.length-1; i++){

            result[i] = vector[i]/number;

        }

        return result;
    }


    }





