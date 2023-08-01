package com.icaras84.rrcodegenerator.core.utils;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {

    public static final int JTabHeight = 25; //measured default

    public static final int JScrollBarVerticalWidth = 15;

    public static final Pattern realNumberRegexPattern = Pattern.compile("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?( ?)(€?)$");

    public static final String ALPHABET_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    public static final String ALPHABET_UPPERCASE = ALPHABET_LOWERCASE.toUpperCase();

    private static final NumberFormatter numFormat;

    static { //set it up no matter what
        NumberFormat realNumFormat = NumberFormat.getNumberInstance();
        numFormat = new NumberFormatter(realNumFormat);
        numFormat.setValueClass(Double.class);
        numFormat.setAllowsInvalid(true);
        numFormat.setCommitsOnValidEdit(true);
    }

    private GeneralUtils(){}

    public static boolean isRealNumber(String str){
        Matcher matcher = realNumberRegexPattern.matcher(str);
        return matcher.matches();
    }

    public static String wrapParameters(Object... args){
        StringBuilder output = new StringBuilder("(");
        if (args.length > 0){
            output.append(args[0].toString());
        }
        for (int i = 1; i < args.length; i++) {
            output.append(", ").append(args[i].toString());
        }
        output.append(")");
        return output.toString();
    }

    public static void changeTitleBar(JFrame frame){
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
    }

    public static void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {

            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), orig.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

    public static void copyStringToClipboard(String src){
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(src),
                        null
                );
    }

    public static JFormattedTextField createRealNumberTextField(){
        JFormattedTextField output = new JFormattedTextField(numFormat);
        output.setText("0");
        return output;
    }

    public static JFormattedTextField createNonNegativeNumberTextField() {
        NumberFormat nonNegativeNumFormat = NumberFormat.getNumberInstance();
        NumberFormatter localNumFormat = new NumberFormatter(nonNegativeNumFormat);
        localNumFormat.setValueClass(Double.class);
        localNumFormat.setAllowsInvalid(true);
        localNumFormat.setCommitsOnValidEdit(true);
        localNumFormat.setMinimum(0.0d);
        JFormattedTextField output = new JFormattedTextField(localNumFormat);
        output.setText("0");
        return output;
    }
    public static void insertToolbarsIntoTabbedPane(JTabbedPane pane, JToolBar leftBar, JToolBar rightBar) {
        pane.putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, leftBar);
        pane.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, rightBar);
    }

    public static Image getImage(String filepath){
        return Toolkit.getDefaultToolkit().getImage(GeneralUtils.class.getResource(filepath));
    }

    public static boolean isSymbol(char c){
        return !(Character.isLetter(c) || Character.isDigit(c) || Character.isWhitespace(c));
    }
}
