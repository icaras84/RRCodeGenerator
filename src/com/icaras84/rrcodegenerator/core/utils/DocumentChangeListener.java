package com.icaras84.rrcodegenerator.core.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface DocumentChangeListener extends DocumentListener {

    default void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    default void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    void changedUpdate(DocumentEvent e);
}
