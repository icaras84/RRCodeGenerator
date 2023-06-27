package com.icaras84.rrcodegenerator.core.renderer.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CodeGenSideBar {

    private static JSplitPane mainPanel;

    private static JPanel navigationPanel;
    private static JTree navTree;
    private static DefaultMutableTreeNode navRoot;

    private static JPanel editorPanel;

    public static void createUI(){

        createEditorPanel();
        createNavPanel();

        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, editorPanel);
        mainPanel.setResizeWeight(3/8d);

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                CodeGenMainWindow.resizeUIComponents();
                super.componentResized(e);
            }
        });
    }

    private static void createEditorPanel(){
        editorPanel = new JPanel();
    }

    private static void createNavPanel(){
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        navRoot = new DefaultMutableTreeNode("Navigation Root");
        navTree = new JTree(new DefaultTreeModel(navRoot));
        navTree.setRowHeight(25);

        navigationPanel.add(navTree);

        navigationPanel.setMinimumSize(new Dimension());
    }

    public static JSplitPane getRootPanel(){
        return mainPanel;
    }

    public static void resize(int width, int height){
        mainPanel.setSize(width, height);
        mainPanel.setPreferredSize(mainPanel.getSize());

        navigationPanel.setSize(mainPanel.getDividerLocation(), height);
        navigationPanel.setPreferredSize(navigationPanel.getSize());

        editorPanel.setSize(width - mainPanel.getDividerLocation(), height);
        editorPanel.setPreferredSize(editorPanel.getSize());
    }
}
