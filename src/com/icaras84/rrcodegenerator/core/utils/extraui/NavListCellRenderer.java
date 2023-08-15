package com.icaras84.rrcodegenerator.core.utils.extraui;

import javax.swing.*;
import java.awt.*;

public class NavListCellRenderer extends DefaultListCellRenderer {

    public static final Color altColor1 = new Color(0x383c4a);
    public static final Color altColor2 = new Color(0x303440);
    public static final Color selectedColor = new Color(0xf57900);
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = new JLabel(value.toString());
        label.setOpaque(true);
        if (!isSelected) {
            if (index % 2 == 0) {
                label.setBackground(altColor1);
            } else {
                label.setBackground(altColor2);
            }
        } else {
            label.setBackground(selectedColor);
        }

        return label;
    }
}
