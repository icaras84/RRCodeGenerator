package com.icaras84.rrcodegenerator.core.ui.singleinstance;

import com.icaras84.rrcodegenerator.core.logic.NavigationPanelLogic;
import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.utils.extraui.JListDnDHandler;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationPanel {

    private static JPanel mainPanel;
    private static JToolBar toolBar;
    private static JScrollPane listPanel;
    private static DefaultListModel<TrajectoryEditorPanel> listModel;
    private static JList<TrajectoryEditorPanel> trajectories;
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

        trajectories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<?> list = (JList<?>)e.getSource();
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && list.getName().equals(trajectories.getName())) {
                    NavigationPanelLogic.handleMouseSelection((JList<TrajectoryEditorPanel>) list, list.locationToIndex(e.getPoint()));
                }
            }
        });

        listPanel = new JScrollPane(trajectories);

        toolBar = new JToolBar();

        trajListLabel = new JLabel("Trajectories");
        trajListLabel.setHorizontalTextPosition(SwingConstants.LEFT);

        listModel.addElement(new TrajectoryEditorPanel());
        listModel.addElement(new TrajectoryEditorPanel());
        listModel.addElement(new TrajectoryEditorPanel());

        resize();
        MainWindow.submitResizeOperation(NavigationPanel::resize);
        NavigationPanelLogic.init();
        arrange();
    }

    public static void arrange(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(toolBar);
        mainPanel.add(trajListLabel);
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
