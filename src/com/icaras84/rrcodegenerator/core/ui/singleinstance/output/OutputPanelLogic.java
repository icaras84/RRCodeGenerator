package com.icaras84.rrcodegenerator.core.ui.singleinstance.output;

import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanel;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import java.awt.event.ActionEvent;

public class OutputPanelLogic {

    public static boolean queuedRender;

    public static void copyGeneratedCodeButton(ActionEvent e){

    }

    public synchronized static void queueRender(ActionEvent e){
        queuedRender = true;
    }

    public static void generateCode(ActionEvent e){

    }

    public static void exportRenderAsImage(ActionEvent e){

    }
}
