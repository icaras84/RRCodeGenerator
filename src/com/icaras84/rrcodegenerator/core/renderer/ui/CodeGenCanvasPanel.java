package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CodeGenCanvasPanel {

    private static JTabbedPane mainPanel;

    private static JPanel canvasPanel;
    private static Canvas canvas;

    private static JPanel generationOutputPanel;
    private static JToolBar outputOptions;
    private static JPanel outputPreview;
    private static JScrollPane outputPreviewScrollPane;
    private static JLabel outputPreviewText;

    private static JButton generate, copyText;

    public static void createUI(){
        mainPanel = new JTabbedPane();
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                CodeGenMainWindow.resizeUIComponents();
                super.componentResized(e);
            }
        });

        createCanvasPanel();
        createGenerationOutputPanel();

        mainPanel.addTab("Canvas", canvasPanel);
        mainPanel.addTab("Generator Output", generationOutputPanel);
    }

    private static void createCanvasPanel(){
        canvasPanel = new JPanel();
        canvasPanel.setLayout(new GridBagLayout());
        canvasPanel.setSize(mainPanel.getSize());
        canvasPanel.setPreferredSize(mainPanel.getPreferredSize());

        canvas = new Canvas();
        canvas.setSize(canvasPanel.getSize());
        canvas.setPreferredSize(canvasPanel.getPreferredSize());
        canvas.setBackground(Color.BLACK);

        canvasPanel.add(canvas);
    }

    private static void createGenerationOutputPanel(){
        generationOutputPanel = new JPanel(new BorderLayout());
        generationOutputPanel.setSize(mainPanel.getSize());
        generationOutputPanel.setPreferredSize(mainPanel.getPreferredSize());

        outputOptions = new JToolBar();
        outputOptions.setLayout(new GridBagLayout());
        outputOptions.setSize(mainPanel.getWidth(), CodeGenToolBar.TOOLBAR_HEIGHT);

        generate = new JButton("Generate");
        copyText = new JButton("Copy Text");
        copyText.addActionListener(e -> copyOutput());

        outputOptions.add(generate);
        outputOptions.add(copyText);

        outputPreviewText = new JLabel();

        outputPreview = new JPanel(new BorderLayout());
        outputPreview.add(outputPreviewText, BorderLayout.PAGE_START);

        outputPreviewScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputPreviewScrollPane.setSize(mainPanel.getWidth(), mainPanel.getHeight() - outputOptions.getHeight());
        outputPreviewScrollPane.setViewportView(outputPreview);
        outputPreviewScrollPane.setBackground(Color.ORANGE);
        outputPreviewScrollPane.setVisible(true);

        generationOutputPanel.add(outputOptions, BorderLayout.NORTH);
        generationOutputPanel.add(outputPreviewScrollPane, BorderLayout.CENTER);

        setOutputPreview("[Code generation output goes here...]");
    }

    public static void createBufferStrategy(int numOfBuffers){
        canvas.createBufferStrategy(numOfBuffers);
    }

    public static Canvas getCanvas(){
        return canvas;
    }

    public static int getCanvasWidth(){
        return canvas.getWidth();
    }

    public static int getCanvasHeight(){
        return canvas.getHeight();
    }

    private static void copyOutput(){
        GeneralUtils.copyStringToClipboard(outputPreviewText.getText());
    }

    public static void setOutputPreview(String previewText) {
        outputPreviewText.setText(previewText);
    }

    public static JTabbedPane getRootPanel(){
        return mainPanel;
    }

    public static void setVisibility(boolean visibility) {
        canvasPanel.setVisible(visibility);
        generationOutputPanel.setVisible(visibility);
        mainPanel.setVisible(visibility);
    }

    public static void resize(int width, int height){
        mainPanel.setSize(width, height);
        mainPanel.setPreferredSize(mainPanel.getSize());

        canvasPanel.setSize(mainPanel.getSize());
        generationOutputPanel.setSize(mainPanel.getSize());

        int square = Math.min(mainPanel.getWidth(), mainPanel.getHeight());
        canvas.setSize(square, square);
        canvas.setPreferredSize(canvas.getSize());

        outputOptions.setSize(mainPanel.getWidth(), CodeGenToolBar.TOOLBAR_HEIGHT);
        outputPreviewScrollPane.setSize(mainPanel.getWidth(), mainPanel.getHeight() - outputOptions.getHeight());
    }
}
