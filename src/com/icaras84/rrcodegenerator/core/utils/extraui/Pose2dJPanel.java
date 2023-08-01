package com.icaras84.rrcodegenerator.core.utils.extraui;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class Pose2dJPanel extends JPanel{

    public static final int WIDTH = 150, HEIGHT = 90;

    private JFormattedTextField xTextBox, yTextBox, headingTextBox;
    private String editorTitle;
    private JLabel xLabel, yLabel, headingLabel, editorTitleLabel;
    private double poseX, poseY, poseHeading;

    public Pose2dJPanel(){
        this("Pose2d Editor");
    }

    public Pose2dJPanel(String editorTitle){
        super();
        setLayout(new GridBagLayout());
        setSize(WIDTH, HEIGHT);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.editorTitle = editorTitle;
        initUI();
    }

    private void initUI(){
        createUI();
        arrangeUI();
    }

    private void createUI() {
        xTextBox = GeneralUtils.createRealNumberTextField();
        yTextBox = GeneralUtils.createRealNumberTextField();
        headingTextBox = GeneralUtils.createRealNumberTextField();


        xTextBox.setText("0");
        yTextBox.setText("0");
        headingTextBox.setText("0");

        xTextBox.addPropertyChangeListener("value", this::retrieveX);
        yTextBox.addPropertyChangeListener("value", this::retrieveY);
        headingTextBox.addPropertyChangeListener("value", this::retrieveHeading);

        xLabel = new JLabel("X:");
        yLabel = new JLabel("Y:");
        headingLabel = new JLabel("θ (DEG):");

        editorTitleLabel = new JLabel(editorTitle);

        xLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        yLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        headingLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
    }

    private void arrangeUI(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1/3d;

        constraints.gridwidth = 16;
        add(editorTitleLabel, constraints);
        constraints.gridwidth = 1;

        constraints.gridx = 0; //label positioning
        constraints.gridy = 1;
        add(xLabel, constraints);
        constraints.gridy = 2;
        add(yLabel, constraints);
        constraints.gridy = 3;
        add(headingLabel, constraints);

        constraints.gridx = 1; //TextBox positioning
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 15;
        constraints.gridy = 1;
        add(xTextBox, constraints);
        constraints.gridy = 2;
        add(yTextBox, constraints);
        constraints.gridy = 3;
        add(headingTextBox, constraints);
    }

    public void setEditorTitle(String nEditorTitle){
        editorTitle = nEditorTitle;
        editorTitleLabel.setText(editorTitle);
    }

    public String getEditorTitle(){
        return editorTitle;
    }

    public Pose2d getPose2d(){
        return new Pose2d(poseX, poseY, Math.toRadians(poseHeading));
    }

    private void retrieveX(PropertyChangeEvent e){
        poseX = ((Number) xTextBox.getValue()).doubleValue();
    }

    private void retrieveY(PropertyChangeEvent e){
        poseY = ((Number) yTextBox.getValue()).doubleValue();
    }

    private void retrieveHeading(PropertyChangeEvent e){
        poseHeading = ((Number) headingTextBox.getValue()).doubleValue();
    }

    public String toString(){
        return "X: " + poseX + "\nY: " + poseY + "\nHeading (DEG): " + poseHeading;
    }
}