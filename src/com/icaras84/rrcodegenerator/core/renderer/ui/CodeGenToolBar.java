package com.icaras84.rrcodegenerator.core.renderer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@Deprecated
public class CodeGenToolBar {

    public static final int TOOLBAR_HEIGHT = 30;

    public enum TARGET{
        TRAJECTORY,
        POSE,
        EMPTY
    }

    private static JToolBar toolBar;

    private static JComboBox<TARGET> target;

    private static JButton create, destroy;

    public static void createUI(){
        toolBar = new JToolBar();
        toolBar.setLayout(new GridBagLayout());

        toolBar.setFocusable(false);

        createSubComponents();

        toolBar.add(target);
        toolBar.add(create);
        toolBar.addSeparator();
        toolBar.add(destroy);

        toolBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                CodeGenMainWindow.resizeUIComponents();
                super.componentResized(e);
            }
        });
    }

    private static void createSubComponents(){

        target = new JComboBox<>(new TARGET[]{
                TARGET.EMPTY,
                TARGET.TRAJECTORY,
                TARGET.POSE
        });

        target.setFocusable(false);

        target.setSize(200, target.getHeight());


        create = new JButton("Create");
        create.setFocusable(false);

        destroy = new JButton("Destroy");
        destroy.setFocusable(false);
    }

    public static JToolBar getRootPanel(){
        return toolBar;
    }

    public static TARGET getTargetItem(){
        return (TARGET) target.getSelectedItem();
    }

    public static void resize(int width, int height){
        toolBar.setSize(width, height);
    }
}
