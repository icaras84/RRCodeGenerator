package com.icaras84.rrcodegenerator.core.utils.extraui;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;

public class Pose2dJPanel extends JPanel{

    public static final int WIDTH = 200, HEIGHT = 90;

    private static final NumberFormatter numFormat;

    static { //set it up no matter what
        NumberFormat realNumFormat = NumberFormat.getIntegerInstance();
        realNumFormat.setMaximumFractionDigits(4);
        numFormat = new NumberFormatter(realNumFormat);
        numFormat.setValueClass(Double.class);
        numFormat.setAllowsInvalid(false); //say 'no' to letters
        numFormat.setCommitsOnValidEdit(true);
    }

    private JFormattedTextField xTextBox, yTextBox, headingTextBox;
    private JLabel xLabel, yLabel, headingLabel;
    private double poseX, poseY, poseHeading;

    public Pose2dJPanel(){
        super();
        setLayout(new GridBagLayout());
        setSize(WIDTH, HEIGHT);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        initUI();
    }

    private void initUI(){
        createUI();
        arrangeUI();
    }

    private void createUI() {
        xTextBox = new JFormattedTextField(numFormat);
        xTextBox.setSize(175, 100);
        yTextBox = new JFormattedTextField(numFormat);
        yTextBox.setSize(175, 100);
        headingTextBox = new JFormattedTextField(numFormat);

        xTextBox.setText("0");
        yTextBox.setText("0");
        headingTextBox.setText("0");


        xTextBox.addPropertyChangeListener("value", this::retrieveX);
        yTextBox.addPropertyChangeListener("value", this::retrieveY);
        headingTextBox.addPropertyChangeListener("value", this::retrieveHeading);

        xLabel = new JLabel("X:");
        yLabel = new JLabel("Y:");
        headingLabel = new JLabel("Heading (DEG):");

        xLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        yLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        headingLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
    }

    private void arrangeUI(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1/3d;

        constraints.gridx = 0; //label positioning
        constraints.gridy = 0;
        add(xLabel, constraints);
        constraints.gridy = 1;
        add(yLabel, constraints);
        constraints.gridy = 2;
        add(headingLabel, constraints);

        constraints.gridx = 1; //TextBox positioning
        constraints.gridy = 0;
        add(xTextBox, constraints);
        constraints.gridy = 1;
        add(yTextBox, constraints);
        constraints.gridy = 2;
        add(headingTextBox, constraints);
    }

    public Pose2d getPose2d(){
        return new Pose2d(poseX, poseY, Math.toRadians(poseHeading));
    }

    private void retrieveX(PropertyChangeEvent e){
        poseX = ((Number) xTextBox.getValue()).doubleValue();
        System.out.println(this);
    }

    private void retrieveY(PropertyChangeEvent e){
        poseY = ((Number) yTextBox.getValue()).doubleValue();
        System.out.println(this);
    }

    private void retrieveHeading(PropertyChangeEvent e){
        poseHeading = ((Number) headingTextBox.getValue()).doubleValue();
        System.out.println(this);
    }

    @Override
    public String toString(){
        return "X: " + poseX + "\nY: " + poseY + "\nHeading (DEG): " + poseHeading;
    }
}
