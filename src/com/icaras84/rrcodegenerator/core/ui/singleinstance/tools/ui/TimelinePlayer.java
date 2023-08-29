package com.icaras84.rrcodegenerator.core.ui.singleinstance.tools.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimelinePlayer {

    /**
     * Required to properly update the UI of the progress bar of the timeline "scrubber"
     */
    public static class TimelineWorker extends SwingWorker<Object, Object> {

        public static int FPS = 30;

        @Override
        @SuppressWarnings("all")
        protected Object doInBackground() {
            int frameDelay = 1000 / FPS;
            Timer t = new Timer(frameDelay, e -> TimelinePlayer.update(frameDelay));

            t.setRepeats(true);
            t.start();

            while (!TimelinePlayer.isFinished()){}

            t.stop();

            return null;
        }
    }

    public enum PLAY_STATE {
        PAUSED,
        PLAYING
    }

    public static final ImageIcon icnPause = new FlatSVGIcon("window_icon/button_icons/intellij_icons/pause_dark.svg");
    public static final ImageIcon icnPlay = new FlatSVGIcon("window_icon/button_icons/intellij_icons/play_forward_dark.svg");
    public static final ImageIcon icnLast = new FlatSVGIcon("window_icon/button_icons/intellij_icons/play_last_dark.svg");
    public static final ImageIcon icnFirst = new FlatSVGIcon("window_icon/button_icons/intellij_icons/play_first_dark.svg");
    public static final ImageIcon icnLoopT = new FlatSVGIcon("window_icon/button_icons/intellij_icons/forceRefresh_dark.svg");
    public static final ImageIcon icnLoopF = new FlatSVGIcon("window_icon/button_icons/intellij_icons/refresh_dark.svg");

    public static int WIDTH = 300, HEIGHT = 70;

    private static JPanel mainPanel;
    private static JProgressBar scrubber;
    private static DefaultBoundedRangeModel rangeModel;

    private static volatile double currentTime;
    private static JLabel currentTimeLabel;
    private static JToolBar buttonPanel;
    private static JButton playerPlayButton, playerResetButton, playerFinalButton;
    private static JButton playerLoopButton;

    private static int maxMs = 10000;

    public static void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setSize(WIDTH, HEIGHT);

        rangeModel = new DefaultBoundedRangeModel();
        rangeModel.setMinimum(0);
        setMaxMs(maxMs);
        rangeModel.setMaximum(maxMs);


        currentTimeLabel = new JLabel(String.format("%.3f SEC", 0f));

        scrubber = new JProgressBar(rangeModel);
        scrubber.addChangeListener(TimelinePlayer::updateCurrentTime);
        scrubber.setSize(WIDTH - 20, 10);
        scrubber.setPreferredSize(scrubber.getSize());
        scrubber.setMinimumSize(scrubber.getSize());
        scrubber.setMaximumSize(scrubber.getSize());
        scrubber.setStringPainted(true);
        scrubber.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                calcMouseProgress(e);

                playerState = PLAY_STATE.PAUSED;
                playerPlayButton.setIcon(icnPlay);
                playerPlayButton.revalidate();
                playerPlayButton.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                calcMouseProgress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                calcMouseProgress(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                calcMouseProgress(e);
            }
        });

        buttonPanel = new JToolBar();
        buttonPanel.setSize(WIDTH, 30);
        buttonPanel.setPreferredSize(buttonPanel.getSize());
        buttonPanel.setMinimumSize(buttonPanel.getSize());
        buttonPanel.setMaximumSize(buttonPanel.getSize());

        playerPlayButton = new JButton(icnPlay);
        playerPlayButton.addActionListener(TimelinePlayer::playerPlayButtonUpdate);

        playerFinalButton = new JButton(icnLast);
        playerFinalButton.addActionListener(TimelinePlayer::playerFinishButtonUpdate);

        playerResetButton = new JButton(icnFirst);
        playerResetButton.addActionListener(TimelinePlayer::playerResetButtonUpdate);

        playerLoopButton = new JButton(icnLoopF);
        playerLoopButton.addActionListener(TimelinePlayer::playerLoopButtonUpdate);

        buttonPanel.add(playerResetButton);
        buttonPanel.add(playerPlayButton);
        buttonPanel.add(playerFinalButton);
        buttonPanel.add(playerLoopButton);
        buttonPanel.add(currentTimeLabel);

        arrangeUI();
    }

    private static void calcMouseProgress(MouseEvent e){
        int progressBarVal = (int)Math.round(((double)e.getX() / (double)scrubber.getWidth()) * scrubber.getMaximum());
        rangeModel.setValue(progressBarVal);

        scrubber.revalidate();
        scrubber.repaint();
    }

    private static void playerPlayButtonUpdate(ActionEvent e){
        if (playerState == PLAY_STATE.PAUSED){
            playerState = PLAY_STATE.PLAYING;
            playerPlayButton.setIcon(icnPause);
            playerPlayButton.revalidate();
            playerPlayButton.repaint();

            TimelineWorker worker = new TimelineWorker();
            worker.execute();
        } else if (playerState == PLAY_STATE.PLAYING){
            playerState = PLAY_STATE.PAUSED;
            playerPlayButton.setIcon(icnPlay);
            playerPlayButton.revalidate();
            playerPlayButton.repaint();
        }
    }

    private static void playerResetButtonUpdate(ActionEvent e){
        if (playerState == PLAY_STATE.PLAYING) {
            playerState = PLAY_STATE.PAUSED;
            playerPlayButton.setIcon(icnPlay);
            playerPlayButton.revalidate();
            playerPlayButton.repaint();
        }

        accumulatedTime = 0;
        rangeModel.setValue(0);
        scrubber.revalidate();
        scrubber.repaint();
    }

    private static void playerFinishButtonUpdate(ActionEvent e){
        if (playerState == PLAY_STATE.PLAYING) {
            playerState = PLAY_STATE.PAUSED;
            playerPlayButton.setIcon(icnPlay);
            playerPlayButton.revalidate();
            playerPlayButton.repaint();
        }

        accumulatedTime = maxMs;
        rangeModel.setValue(maxMs);
        scrubber.revalidate();
        scrubber.repaint();
    }

    private static void playerLoopButtonUpdate(ActionEvent e){
        loopState = !loopState;

        playerLoopButton.setIcon(loopState ? icnLoopT : icnLoopF);

        playerLoopButton.revalidate();
        playerLoopButton.repaint();
    }

    private volatile static long accumulatedTime = 0;
    private volatile static PLAY_STATE playerState = PLAY_STATE.PAUSED;
    private volatile static boolean loopState = false;

    public static void update(long deltaTimeMs){
        if (playerState == PLAY_STATE.PLAYING){
            accumulatedTime += deltaTimeMs;

            if (loopState) accumulatedTime %= maxMs;

            rangeModel.setValue(clampTime((int) accumulatedTime, maxMs));
            scrubber.setValue(rangeModel.getValue());

            if (!loopState && accumulatedTime >= maxMs){
                playerState = PLAY_STATE.PAUSED;
                playerPlayButton.setIcon(icnPlay);
                playerPlayButton.revalidate();
                playerPlayButton.repaint();
            }
        }
    }

    private static int clampTime(int val, int max){
        return Math.min(Math.max(val, 0), max);
    }

    private static void updateCurrentTime(ChangeEvent e){
        double ms = (double) rangeModel.getValue() / maxMs * maxMs; //translate normalized slider values into milliseconds
        currentTime = Math.floor(ms * 1E4) / (1E7); //prevent rounding errors and format milliseconds into seconds
        currentTimeLabel.setText(String.format("%.3f SEC", currentTime));
        currentTimeLabel.revalidate();
        currentTimeLabel.repaint();

        accumulatedTime = (long) ms;
    }

    private static void arrangeUI(){
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(scrubber);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
    }

    public static void setMaxMs(double maxTime){
        maxMs = (int) maxTime;
        rangeModel.setMaximum(maxMs);
    }

    public static double getCurrentTime(){
        return currentTime;
    }

    public synchronized static boolean isFinished(){
        return (maxMs == accumulatedTime) || (playerState == PLAY_STATE.PAUSED);
    }

    public static JPanel getPanel(){
        return mainPanel;
    }
}