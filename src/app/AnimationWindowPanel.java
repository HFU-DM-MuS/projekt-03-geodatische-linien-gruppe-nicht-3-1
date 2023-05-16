package app;
import utils.ApplicationTime;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimationWindowPanel extends Animation {
    static JButton start = new JButton("Start");
    static JLabel distanceP1P2 = new JLabel();
    private static void createControlFrame(ApplicationTime thread) {

        // Create a new frame
        JFrame controlFrame = new JFrame("Mathematik und Simulation");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create a control panel where all the components will be put
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 2, 2));
        controlFrame.add(panel);
        controlFrame.setVisible(true);


        // Create slider to adjust alpha
        JLabel adjustAlpha = new JLabel("Adjust alpha");
        JSlider adjustAlphaSlider = new JSlider(0, 360, (int) Math.toDegrees(GraphicContent.alpha));
        adjustAlphaSlider.addChangeListener(e -> {
            GraphicContent.setAlpha(adjustAlphaSlider.getValue());
        });

        // Create slider to adjust S1
        JLabel adjustS1 = new JLabel("Adjust S1");
        JSlider adjustS1Slider = new JSlider(0, 100, (int) GraphicContent.s1ScaleFactor*100);
        adjustS1Slider.addChangeListener(e -> {
            GraphicContent.setS1ScaleFactor(adjustS1Slider.getValue());
        });

        //Create Input for Pos1XY
        JLabel pos1X = new JLabel("Pos1 X (e.g:90)");
        JTextField pos1XInput = new JTextField("0");
        JLabel pos1Y = new JLabel("Pos1 Y");
        JTextField pos1YInput = new JTextField("0");

        //Create Input for Pos2XY
        JLabel pos2Y = new JLabel("Pos2 Y (e.g:45)");
        JTextField pos2YInput = new JTextField("0");
        JLabel pos2X = new JLabel("Pos2 X");
        JTextField pos2XInput = new JTextField("0");

        JLabel distance = new JLabel("Distance between Pos1 and Pos2");

        JLabel startInstructions = new JLabel("Press Start to begin the animation");

        //Add everything to the panel

        panel.add(adjustAlpha);
        panel.add(adjustAlphaSlider);


        panel.add(start);
        panel.add(startInstructions);

        panel.add(adjustS1);
        panel.add(adjustS1Slider);

        panel.add(distance);
        panel.add(distanceP1P2);

        panel.add(pos1X);
        panel.add(pos1XInput);

        panel.add(pos2X);
        panel.add(pos2XInput);

        panel.add(pos1Y);
        panel.add(pos1YInput);

        panel.add(pos2Y);
        panel.add(pos2YInput);

        controlFrame.pack();

        //When start is pressed, get the values inside the text fields
        start.addActionListener(e ->{
            double pos1XDouble = Double.parseDouble(pos1XInput.getText());
            double pos1YDouble = Double.parseDouble(pos1YInput.getText());
            double pos2XDouble = Double.parseDouble(pos2XInput.getText());
            double pos2YDouble = Double.parseDouble(pos2YInput.getText());
            GraphicContent.setPos1XY(pos1XDouble, pos1YDouble);
            GraphicContent.setPos2XY(pos2XDouble, pos2YDouble);
            GraphicContent.startAnimation();
        });

    }


    @Override
    protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
        // a list of all frames (windows) that will be shown
        ArrayList<JFrame> frames = new ArrayList<>();

        // Create main frame (window)
        JFrame frame = new JFrame("Mathematik und Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new GraphicContent(applicationTimeThread);
        frame.add(panel);
        frame.pack(); // adjusts size of the JFrame to fit the size of it's components
        frame.setVisible(true);

        frames.add(frame);
        createControlFrame(applicationTimeThread);
        return frames;
    }

}
