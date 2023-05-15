package app;

import utils.ApplicationTime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WindowInputsAndTexts extends Animation {
    static JButton start = new JButton("Start");
    static JLabel distanceP1P2 = new JLabel(String.valueOf(GraficContentPanel.getDistance()) + "Km");
    private static void createControlFrame(ApplicationTime thread) {
        // Create a new frame
        JFrame controlFrame = new JFrame("Mathematik und Simulation");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a JPanel as the new drawing surface
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 2, 2)); // manages the layout of elements in the panel (buttons, labels,
        // other panels, etc.)
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(4, 4, 2, 2));
        controlFrame.add(panel);
        controlFrame.add(scrollPanel);
        controlFrame.setVisible(true);


        // set up second panel
        JLabel adjustAlpha = new JLabel("Adjust alpha");
        JSlider adjustAlphaSlider = new JSlider(0, 360, (int) Math.toDegrees(GraficContentPanel.alpha));
        adjustAlphaSlider.addChangeListener(e -> {
            GraficContentPanel.setAlpha(adjustAlphaSlider.getValue());
        });

        JLabel adjustS1 = new JLabel("Adjust S1");
        JSlider adjustS1Slider = new JSlider(0, 100, (int) GraficContentPanel.s1ScaleFactor*100);
        adjustS1Slider.addChangeListener(e -> {
            GraficContentPanel.setS1ScaleFactor(adjustS1Slider.getValue());
        });

        JLabel pos1X = new JLabel("Pos1 X (e.g:90)");
        JTextField pos1XInput = new JTextField("0");
        JLabel pos1Y = new JLabel("Pos1 Y");
        JTextField pos1YInput = new JTextField("0");

        JLabel pos2Y = new JLabel("Pos2 Y (e.g:45)");
        JTextField pos2YInput = new JTextField("0");
        JLabel pos2X = new JLabel("Pos2 X");
        JTextField pos2XInput = new JTextField("0");

        JLabel distance = new JLabel("Distance between Pos1 and Pos2");


        JLabel startInstructions = new JLabel("Press Start to begin the animation");




        scrollPanel.add(adjustAlpha);
        scrollPanel.add(adjustAlphaSlider);


        scrollPanel.add(start);
        scrollPanel.add(startInstructions);

        scrollPanel.add(adjustS1);
        scrollPanel.add(adjustS1Slider);

        scrollPanel.add(distance);
        scrollPanel.add(distanceP1P2);

        scrollPanel.add(pos1X);
        scrollPanel.add(pos1XInput);

        scrollPanel.add(pos2X);
        scrollPanel.add(pos2XInput);





        scrollPanel.add(pos1Y);
        scrollPanel.add(pos1YInput);


        scrollPanel.add(pos2Y);
        scrollPanel.add(pos2YInput);

        controlFrame.pack();

        start.addActionListener(e ->{
            double pos1XDouble = Double.parseDouble(pos1XInput.getText());
            double pos1YDouble = Double.parseDouble(pos1YInput.getText());
            double pos2XDouble = Double.parseDouble(pos2XInput.getText());
            double pos2YDouble = Double.parseDouble(pos2YInput.getText());
            GraficContentPanel.setPos1XY(pos1XDouble, pos1YDouble);
            GraficContentPanel.setPos2XY(pos2XDouble, pos2YDouble);
            GraficContentPanel.startAnimation();
        });

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
