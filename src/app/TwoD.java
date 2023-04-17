package app;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.ApplicationTime;

public class TwoD extends JFrame {


    public TwoD() {
        setTitle("2D Koordinatensystem");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0);
        g2d.drawLine(0, -getHeight() / 2, 0, getHeight() / 2);
        g2d.drawLine(0, 0, 0, 100);
        g2d.drawLine(0, 0, 100, 0);
        g2d.drawLine(0, 0, 0, -100);
        g2d.drawLine(0, 0, -100, 0);
    }

    public static void main(String[] args) {
        new TwoD();
    }
}
