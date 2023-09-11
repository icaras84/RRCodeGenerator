package com.icaras84.rrcodegenerator.core.ui.multiinstance;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.window.MainWindow;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.info.TrajectoryInfo;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

public class TrajectoryEditorPanel extends JPanel {

    private static final int toolbarHeight = 30;
    private static final int trajInitPanelHeight = Pose2dJPanel.HEIGHT + 40;
    public static final int PANEL_WIDTH = EndPoseEditorPanel.PANEL_WIDTH + GeneralUtils.JScrollBarVerticalWidth;

    private TrajectoryInfo info;
    private Vector<EndPoseEditorPanel> endPoseEditors;

    private JToolBar toolBar;
    private JButton addNewEditor;

    private JFormattedTextField trajNameEditor;
    private Pose2dJPanel startPoseEditor;

    private JPanel tangentPanel;
    private JLabel tangentLabel;
    private JFormattedTextField tangentEditor;

    private JScrollPane scrolledView;
    private JPanel editorPane;

    public TrajectoryEditorPanel(){
        super();
        info = new TrajectoryInfo();
        init();


        MainWindow.submitResizeOperation(this::resize);
    }

    public void init()  {
        endPoseEditors = new Vector<>();

        toolBar = new JToolBar();
        addNewEditor = new JButton("Add Segment Pose");
        addNewEditor.addActionListener(e -> addNewEndPoseEditor());
        toolBar.setSize(PANEL_WIDTH, toolbarHeight);
        toolBar.setMinimumSize(toolBar.getSize());
        toolBar.setMaximumSize(toolBar.getSize());
        toolBar.setPreferredSize(toolBar.getSize());

        startPoseEditor = new Pose2dJPanel("Trajectory Start Pose");
        startPoseEditor.setPreferredSize(startPoseEditor.getSize());
        startPoseEditor.setMinimumSize(startPoseEditor.getSize());
        startPoseEditor.setMaximumSize(startPoseEditor.getSize());
        startPoseEditor.setBorder(null);
        startPoseEditor.addTextBoxPropertyChangeListeners("value", this::setTrajPose);

        trajNameEditor = new JFormattedTextField();
        trajNameEditor.setValue(info.getTrajectoryName());
        trajNameEditor.setText(info.getTrajectoryName());
        trajNameEditor.addPropertyChangeListener("value", this::setTrajName);
        trajNameEditor.setSize(PANEL_WIDTH / 2, 20);
        trajNameEditor.setPreferredSize(trajNameEditor.getSize());

        tangentPanel = new JPanel();
        tangentPanel.setLayout(new GridBagLayout());
        //GeneralUtils.ensureJComponentSize(tangentPanel, PANEL_WIDTH, toolbarHeight);
        tangentLabel = new JLabel("Start Tangent: ");
        tangentEditor = GeneralUtils.createRealNumberTextField();
        tangentEditor.addPropertyChangeListener("value", this::setStartTangent);

        editorPane = new JPanel();
        editorPane.setLayout(new BoxLayout(editorPane, BoxLayout.Y_AXIS));

        scrolledView = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrolledView.setViewportView(editorPane);
        scrolledView.getVerticalScrollBar().setUnitIncrement(5);

        arrange();
        resize();
    }

    public void arrange(){

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        toolBar.add(trajNameEditor);
        toolBar.add(addNewEditor);
        this.add(toolBar);

        this.add(startPoseEditor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        tangentPanel.add(tangentLabel, gbc);
        gbc.gridx = 1;
        tangentPanel.add(tangentEditor, gbc);

        this.add(tangentPanel);

        this.add(scrolledView);

        this.add(Box.createVerticalGlue());
    }

    public void resize(){
        scrolledView.setSize(EndPoseEditorPanel.PANEL_WIDTH + GeneralUtils.JScrollBarVerticalWidth, MainWindow.height - (trajInitPanelHeight + toolbarHeight));
        scrolledView.setPreferredSize(scrolledView.getSize());
        scrolledView.setMaximumSize(scrolledView.getSize());
        this.setSize(EndPoseEditorPanel.PANEL_WIDTH + GeneralUtils.JScrollBarVerticalWidth, MainWindow.height);
        this.setPreferredSize(this.getSize());
    }

    public void load(TrajectoryInfo nTraj){
        startPoseEditor.setPose(new Pose2d(nTraj.getStartX(), nTraj.getStartY(), nTraj.getStartHeading()));
        tangentEditor.setValue(Math.toDegrees(nTraj.getStartTangent()));
        info = new TrajectoryInfo(nTraj);

        loadPoseEditors(info);
    }

    public void load2(TrajectoryInfo nTraj){
        startPoseEditor.setPose(new Pose2d(nTraj.getStartX(), nTraj.getStartY(), nTraj.getStartHeading()));
        tangentEditor.setValue(Math.toDegrees(nTraj.getStartTangent()));
        info = nTraj;

        loadPoseEditors(info);
    }

    private void loadPoseEditors(TrajectoryInfo trajectoryInfo){
        int len = trajectoryInfo.getEndPoseBufferLength();
        int formerLen = endPoseEditors.size();

        for (int i = 0; i < formerLen; i++) endPoseEditors.get(0).requestDelete();

        for (int i = 0; i < len; i++) addNewEndPoseEditor();

        for (int i = 0; i < len; i++) endPoseEditors.get(i).loadPose(trajectoryInfo.getEndPose(i));
    }

    public void addNewEndPoseEditor(){
        EndPoseEditorPanel endPoseEditorPanel = new EndPoseEditorPanel(this);
        endPoseEditors.add(endPoseEditorPanel);
        editorPane.add(endPoseEditorPanel);
        updateScrollPane();
    }

    private void setTrajPose(PropertyChangeEvent evt){
        info.setStartPose(startPoseEditor.getPose2d());
    }

    private void setStartTangent(PropertyChangeEvent evt){
        info.setStartTangent(Math.toRadians(((Number) tangentEditor.getValue()).doubleValue()));
    }

    private void setTrajName(PropertyChangeEvent evt){
        info.setTrajectoryName(cleanTrajName(trajNameEditor.getValue().toString()));
        trajNameEditor.setText(info.getTrajectoryName());
    }

    public static String cleanTrajName(String trajName){
        char[] output = trajName.trim().toCharArray();
        if (output.length == 0) return "_defaultName"; //there is a default keyword in java, so start with an underscore
        if (Character.isDigit(output[0])) output[0] = '_';
        for (int i = 0; i < output.length; i++) {
            if (GeneralUtils.isSymbol(output[i]) && output[i] != '_') output[i] = '_';
        }
        return new String(output);
    }

    public void subPanelRequestDelete(EndPoseEditorPanel editorPanel){
        editorPane.remove(editorPanel);
        endPoseEditors.remove(editorPanel);
        updateScrollPane();
    }

    private void updateScrollPane(){
        scrolledView.revalidate();
        scrolledView.repaint();
        editorPane.revalidate();
        editorPane.repaint();
        revalidate();
        repaint();
    }

    public Pose2d getStartPose(){
        return info.getStartPose();
    }

    public TrajectoryInfo getInfo(){
        return info;
    }

    private static JPanel reservedPanel;
    private static TrajectoryEditorPanel currentPanel;

    public static void initMain(){
        reservedPanel = new JPanel();

        resizeMain();
    }

    public static void resizeMain(){
        reservedPanel.setSize(EndPoseEditorPanel.PANEL_WIDTH + GeneralUtils.JScrollBarVerticalWidth, MainWindow.height);
        reservedPanel.setPreferredSize(reservedPanel.getSize());
    }

    public static void swapEditors(TrajectoryEditorPanel editorPanel){
        if (currentPanel != null) removeEditor();
        addEditor(editorPanel);
    }

    public static void removeEditor(){
        if (currentPanel != null){
            reservedPanel.remove(currentPanel);
            currentPanel = null;

            reservedPanel.revalidate();
            reservedPanel.repaint();
        }
    }

    public static void addEditor(TrajectoryEditorPanel nEditorPane){
        if (currentPanel == null){
            currentPanel = nEditorPane;
            reservedPanel.add(currentPanel);

            reservedPanel.revalidate();
            reservedPanel.repaint();

            nEditorPane.revalidate();
            nEditorPane.repaint();
        }
    }
    public static JPanel getReservedPanel(){
        return reservedPanel;
    }


}
