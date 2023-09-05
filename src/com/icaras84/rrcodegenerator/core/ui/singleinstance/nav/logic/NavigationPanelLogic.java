package com.icaras84.rrcodegenerator.core.ui.singleinstance.nav.logic;

import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.utils.info.TrajectoryInfo;

import javax.swing.*;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class NavigationPanelLogic {



    public static class TrajectoryItems{
        public TrajectoryEditorPanel editor;
        public TrajectoryInfo validBuild;
        public int idx;

        public TrajectoryItems(TrajectoryEditorPanel editorPanel, TrajectoryInfo validBuild, int idx) {
            this.editor = editorPanel;
            this.validBuild = validBuild;
            this.idx = idx;
        }
    }

    @SuppressWarnings("all")
    private static ConcurrentHashMap<TrajectoryInfo, TrajectoryItems> trackedTrajectoryInfo;
    private static volatile TrajectoryItems currentInfo;

    public static void init(){
        trackedTrajectoryInfo = new ConcurrentHashMap<>();
        currentInfo = null;
    }

    public static void handleMouseSelection(JList<TrajectoryInfo> srcList, int idx){
        if (idx > -1) {
            currentInfo = trackedTrajectoryInfo.get(srcList.getModel().getElementAt(idx));
            TrajectoryEditorPanel trajEditor = trackedTrajectoryInfo.get(srcList.getModel().getElementAt(idx)).editor;
            TrajectoryEditorPanel.swapEditors(trajEditor);
        }
    }

    public static void createTrajectory(DefaultListModel<TrajectoryInfo> listModel){
        TrajectoryEditorPanel editorPanel = new TrajectoryEditorPanel();
        listModel.addElement(editorPanel.getInfo());
        int idx = listModel.indexOf(editorPanel.getInfo());
        trackedTrajectoryInfo.put(editorPanel.getInfo(), new TrajectoryItems(editorPanel, null, idx));

        updateTrajectoryIndices(listModel);
    }

    public static void loadTrajectory(DefaultListModel<TrajectoryInfo> listModel, TrajectoryInfo traj){
        TrajectoryEditorPanel editorPanel = new TrajectoryEditorPanel();
        editorPanel.load(traj);
        listModel.addElement(traj);
        int idx = listModel.indexOf(traj);
        trackedTrajectoryInfo.put(traj, new TrajectoryItems(editorPanel, null, idx));

        updateTrajectoryIndices(listModel);
    }

    public static void createTrajectories(DefaultListModel<TrajectoryInfo> listModel, int count){
        for (int i = 0; i < count; i++) {
            createTrajectory(listModel);
        }
    }

    public static void updateTrajectoryIndices(DefaultListModel<TrajectoryInfo> listModel){
        trackedTrajectoryInfo.keySet().forEach(
                (trajectory) -> trackedTrajectoryInfo.get(trajectory).idx = listModel.indexOf(trajectory)
        );
    }

    public static void deleteTrajectories(DefaultListModel<TrajectoryInfo> listModel, int[] indices){
        if (!(indices == null || indices.length == 0)) {
            for (Map.Entry<TrajectoryInfo, TrajectoryItems> entry : trackedTrajectoryInfo.entrySet()) {
                TrajectoryInfo trajectory = entry.getKey();
                int arrIdx = entry.getValue().idx;
                for (int index : indices) {
                    if (index == arrIdx) {
                        trackedTrajectoryInfo.remove(trajectory);
                        break;
                    }
                }
            }

            //delete elements in reverse to minimize cascading index changes of a list
            for (int i = 0; i < indices.length && !listModel.isEmpty(); i++) listModel.removeElementAt(indices[indices.length - i - 1]);

            updateTrajectoryIndices(listModel);
        }
    }

    public Vector<TrajectoryInfo> getAllTrajectoryInformation(){
        return new Vector<>(trackedTrajectoryInfo.keySet());
    }

    public static TrajectoryItems getCurrentTrajectoryItems() {
        return currentInfo;
    }
}
