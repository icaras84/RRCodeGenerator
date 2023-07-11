package com.icaras84.rrcodegenerator.core.utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {

    public static final int JTabHeight = 24; //measured default

    public static final Pattern realNumberRegexPattern = Pattern.compile("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?( ?)(€?)$");

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
        return new JFormattedTextField(numFormat);
    }
}
