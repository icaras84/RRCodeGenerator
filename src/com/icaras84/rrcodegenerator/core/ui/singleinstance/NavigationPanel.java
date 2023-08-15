package com.icaras84.rrcodegenerator.core.ui.singleinstance;

import com.icaras84.rrcodegenerator.core.logic.NavigationPanelLogic;
import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.TrajectoryInfo;
import com.icaras84.rrcodegenerator.core.utils.extraui.JListDnDHandler;
import com.icaras84.rrcodegenerator.core.utils.extraui.NavListCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationPanel {

    private static JPanel mainPanel;

    private static JToolBar toolBar;
    private static JButton createTraj, destroyTraj;
    private static JSpinner trajectoryCreationCounter;
    private static int[] selectedTrajectories;

    private static JScrollPane listPanel;
    private static DefaultListModel<TrajectoryInfo> listModel;
    private static JList<TrajectoryInfo> trajectories;
    private static JLabel trajListLabel;


    private NavigationPanel(){}

    public static void init(){
        mainPanel = new JPanel();

        listModel = new DefaultListModel<>();
        trajectories = new JList<>(listModel);
        trajectories.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        trajectories.setDragEnabled(true);
        trajectories.setDropMode(DropMode.INSERT);
        trajectories.setTransferHandler(new JListDnDHandler());
        trajectories.setName("NavigationPanelTrajectoryList");
        trajectories.setCellRenderer(new NavListCellRenderer());

        trajectories.addMouseListener(new MouseAdapter() {
            @Override
            @SuppressWarnings("all")
            public void mouseClicked(MouseEvent e) {
                JList<?> list = (JList<?>)e.getSource();

                selectedTrajectories = list.getSelectedIndices();

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && list.getName().equals(trajectories.getName())) {
                    NavigationPanelLogic.handleMouseSelection((JList<TrajectoryInfo>) list, list.locationToIndex(e.getPoint()));
                }
            }
        });

        listPanel = new JScrollPane(trajectories);

        toolBar = new JToolBar();

        createTraj = new JButton("Create Trajectory");
        createTraj.addActionListener(e -> NavigationPanelLogic.createTrajectories(listModel, (Integer) trajectoryCreationCounter.getValue()));

        destroyTraj = new JButton("Destroy Trajectory");
        destroyTraj.addActionListener(e -> NavigationPanelLogic.deleteTrajectories(listModel, selectedTrajectories));

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        spinnerNumberModel.setStepSize(1);
        spinnerNumberModel.setValue(1);
        spinnerNumberModel.setMinimum(1);
        trajectoryCreationCounter = new JSpinner(spinnerNumberModel);
        GeneralUtils.ensureJComponentSize(trajectoryCreationCounter, 60, 25);

        toolBar.add(trajectoryCreationCounter);
        toolBar.add(createTraj);
        toolBar.add(destroyTraj);

        trajListLabel = new JLabel("Trajectories");

        resize();
        MainWindow.submitResizeOperation(NavigationPanel::resize);
        NavigationPanelLogic.init();
        arrange();
    }

    public static void arrange(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(toolBar);
        //mainPanel.add(trajListLabel);
        mainPanel.add(listPanel);
    }

    public static void resize(){
        mainPanel.setSize(TrajectoryEditorPanel.PANEL_WIDTH, MainWindow.height);
        mainPanel.setMinimumSize(mainPanel.getSize());
        mainPanel.setMaximumSize(mainPanel.getSize());
        mainPanel.setPreferredSize(mainPanel.getSize());
    }

    public static JPanel getPanel(){
        return mainPanel;
    }


}
