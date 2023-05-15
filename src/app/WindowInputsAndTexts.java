package app;

import utils.ApplicationTime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WindowInputsAndTexts extends Animation {
    static JButton start = new JButton("Start");
    private static void createControlFrame(ApplicationTime thread) {
        // Create a new frame
        JFrame controlFrame = new JFrame("Mathematik und Simulation");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a JPanel as the new drawing surface
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 4, 2, 2)); // manages the layout of elements in the panel (buttons, labels,
        // other panels, etc.)
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(3, 4, 2, 2));
        controlFrame.add(panel);
        controlFrame.add(scrollPanel);
        controlFrame.setVisible(true);


        // set up second panel
        JLabel scrollLabel = new JLabel("Adjust alpha");
        JSlider scrollBar = new JSlider(0, 360, (int) GraficContentPanel.alpha);
        scrollBar.addChangeListener(e -> {
            GraficContentPanel.setAlpha(scrollBar.getValue());
        });

        JLabel pos1X = new JLabel("Pos1 X (e.g:90)");
        JTextField pos1XInput = new JTextField("0");
        JLabel pos1Y = new JLabel("Pos1 Y");
        JTextField pos1YInput = new JTextField("0");

        JLabel pos2Y = new JLabel("Pos2 Y (e.g:45)");
        JTextField pos2YInput = new JTextField("0");
        JLabel pos2X = new JLabel("Pos2 X");
        JTextField pos2XInput = new JTextField("0");



        JLabel startInstructions = new JLabel("Press Start to begin the animation");




        scrollPanel.add(scrollLabel);
        scrollPanel.add(scrollBar);


        scrollPanel.add(start);
        scrollPanel.add(startInstructions);

        scrollPanel.add(pos1X);
        scrollPanel.add(pos1XInput);

        scrollPanel.add(pos2X);
        scrollPanel.add(pos1YInput);




        scrollPanel.add(pos1Y);
        scrollPanel.add(pos2XInput);


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
            GraficContentPanel.startTimer();
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
