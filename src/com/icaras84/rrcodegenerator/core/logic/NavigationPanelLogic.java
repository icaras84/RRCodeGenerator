package com.icaras84.rrcodegenerator.core.logic;

import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;

import javax.swing.*;
import java.util.HashMap;

public class NavigationPanelLogic {

    public static HashMap<TrajectoryEditorPanel, String> trackedTrajectories;

    public static void init(){
        trackedTrajectories = new HashMap<>();
    }

    public static void handleMouseSelection(JList<TrajectoryEditorPanel> srcList, int idx){
        System.out.println(idx);
        TrajectoryEditorPanel trajEditor = srcList.getModel().getElementAt(idx);
        TrajectoryEditorPanel.swapEditors(trajEditor);
    }

    public static void addTrajectory(TrajectoryEditorPanel editorPanel, String initialName){
        trackedTrajectories.put(editorPanel, initialName);
    }

    public static void editTrajectoryEditorName(TrajectoryEditorPanel editorPanel, String name){
        trackedTrajectories.replace(editorPanel, name);
    }

    public static void removeTrajectory(TrajectoryEditorPanel editorPanel){
        trackedTrajectories.remove(editorPanel);
    }
}
