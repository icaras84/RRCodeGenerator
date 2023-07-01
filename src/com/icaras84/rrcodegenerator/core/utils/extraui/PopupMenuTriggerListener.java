package com.icaras84.rrcodegenerator.core.utils.extraui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopupMenuTriggerListener implements MouseListener {

    private Component invoker;
    private JPopupMenu menu;

    public PopupMenuTriggerListener(Component invoker, JPopupMenu menu) {
        this.invoker = invoker;
        this.menu = menu;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        triggerPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        triggerPopup(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void triggerPopup(MouseEvent mouseEvent){
        if (mouseEvent.isPopupTrigger()) menu.show(invoker, mouseEvent.getX(), mouseEvent.getY());
    }
}
