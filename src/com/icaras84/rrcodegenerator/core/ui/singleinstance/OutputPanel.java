package com.icaras84.rrcodegenerator.core.ui.singleinstance;

import com.icaras84.rrcodegenerator.core.logic.OutputPanelLogic;
import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class OutputPanel{

    public enum TAB_SELECTION{
        VIEWPORT("Viewport", 0),
        CODE_OUT("Code", 1);

        private int idx;
        private String tabName;

        TAB_SELECTION(String tabName, int idx){
            this.tabName = tabName;
            this.idx = idx;
        }
    }

    public static int panelWidth, panelHeight;
    public static int visiblePanelWidth, visiblePanelHeight;
    public static int canvasWidth, canvasHeight;

    private static JTabbedPane mainPanel;

    private static JPanel canvasPanel;
    private static Canvas mainCanvas;

    private static JPanel codeGenPanel;
    private static JLabel codeGenOutput;
    private static String codeGenStringOutput;

    private static JPanel enabledTrajectoriesPanel;

    private static JPanel settingsPanel;

    private static JToolBar inputBar;
    private static JButton queueRender;
    private static JButton generateCode;
    private static JButton copyGeneratedCode;

    public static int mouseX, mouseY;

    private OutputPanel(){}

    public static void init(){
        mainPanel = new JTabbedPane();
        mainPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resize();
                MainWindow.resize(); //centralize resizing
            }
        });

        initCanvasPanel();
        initCodeGenOutput();
        initToolbar();

        mainPanel.addTab(TAB_SELECTION.VIEWPORT.tabName, canvasPanel);
        mainPanel.addTab(TAB_SELECTION.CODE_OUT.tabName, codeGenPanel);

        GeneralUtils.insertToolbarsIntoTabbedPane(mainPanel, inputBar, null);

        resize();
        MainWindow.submitResizeOperation(OutputPanel::resize);
    }

    private static void initCanvasPanel(){
        canvasPanel = new JPanel();
        canvasPanel.setLayout(new BorderLayout());

        mainCanvas = new Canvas(
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration()
        );
        canvasPanel.add(mainCanvas, BorderLayout.CENTER);
        mainCanvas.setBackground(Color.BLACK);
        canvasPanel.setBackground(Color.BLACK);

        mainCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    private static void initCodeGenOutput(){
        codeGenPanel = new JPanel();
        codeGenOutput = new JLabel();

        codeGenPanel.add(codeGenOutput);
    }

    private static void initToolbar(){
        /*
        generateCode = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/minimap_dark.png")));
        generateCode.addActionListener(OutputPanelLogic::generateCode);
        generateCode.setToolTipText("Generate trajectory code."); //icon might not be clear
        generateCode.setSize(16, 16);

         */

        copyGeneratedCode = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/copy_dark.png")));
        copyGeneratedCode.addActionListener(OutputPanelLogic::copyGeneratedCodeButton);
        copyGeneratedCode.setToolTipText("Copy generated trajectory code.");
        copyGeneratedCode.setSize(16, 16);

        queueRender = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/editSource_dark.png")));
        queueRender.addActionListener(OutputPanelLogic::queueRender);
        queueRender.setToolTipText("Render enabled trajectories.");
        queueRender.setSize(16, 16);

        inputBar = new JToolBar();
        inputBar.setSize(56, 16);
        inputBar.setPreferredSize(inputBar.getSize());
        inputBar.setMinimumSize(inputBar.getSize());
        inputBar.setMaximumSize(inputBar.getSize());
        inputBar.setBorder(null);
        inputBar.setFloatable(false);
        inputBar.setRollover(false);

        //inputBar.add(generateCode);
        inputBar.add(copyGeneratedCode);
        inputBar.add(queueRender);
    }

    public static String getCodeGenStringOutput() {
        return codeGenStringOutput;
    }

    public static void setCodeGenStringOutput(String codeGenStringOutput) {
        OutputPanel.codeGenStringOutput = codeGenStringOutput;
    }

    public static void resize(){
        panelWidth = MainWindow.width - (TrajectoryEditorPanel.PANEL_WIDTH * 2);
        panelHeight = MainWindow.height;
        visiblePanelWidth = mainPanel.getVisibleRect().width;
        //visiblePanelHeight = panelHeight - GeneralUtils.JTabHeight;
        visiblePanelHeight = mainPanel.getVisibleRect().height;

        canvasWidth = visiblePanelWidth;
        canvasHeight = visiblePanelHeight;

        mainPanel.setSize(panelWidth, panelHeight);
        mainPanel.setPreferredSize(mainPanel.getSize());
    }

    public static void createBuffers(){
        mainCanvas.createBufferStrategy(2);
    }

    public static JTabbedPane getPanel(){
        return mainPanel;
    }

    public static TAB_SELECTION getTabSelection(){
        int tabIdx = mainPanel.getSelectedIndex();
        TAB_SELECTION output = null;
        if (tabIdx == TAB_SELECTION.VIEWPORT.idx){
            output = TAB_SELECTION.VIEWPORT;
        } else if (tabIdx == TAB_SELECTION.CODE_OUT.idx){
            output = TAB_SELECTION.CODE_OUT;
        }
        return output;
    }

    public static Canvas getMainCanvas(){
        return mainCanvas;
    }
}
