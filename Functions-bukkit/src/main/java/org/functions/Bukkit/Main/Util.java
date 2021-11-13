package org.functions.Bukkit.Main;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Util {
    public static int parseInteger(Object text) {
        return Integer.parseInt(text.toString());
    }
    public static double parseDouble(Object text) {
        return Double.parseDouble(text.toString());
    }
    public static String parseString(Object text) {
        return text.toString();
    }

    public static double parseDoubleUpFromNumber(Object d, int limit) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            temp.append("0");
        }
        return parseDoubleUp(d, temp.toString());
    }
    public static double parseDoubleUp(Object d,String limit) {
        /*String temp = parseString(d);
        if (temp.length() == 1) {
            return parseInteger(temp);
        }
        int t = temp.length() - 1;
            System.out.println(t);
            if (parseInteger(temp.substring(t)) >= 5) {
                System.out.println(temp.substring(0,temp.length()-1));
                temp = parseString(parseInteger(temp.substring(0,temp.length()-1)) + (parseInteger(temp.substring(t)) + 5));
            }
        return parseInteger(temp);
        double t = parseDouble(temp.substring(temp.length() - 1));
        if (t >= 5.0) {
            return Math.ceil(parseDouble(d));
        } else {
            return Math.floor(parseDouble(d));
        }*/
        DecimalFormat df = new DecimalFormat("#." + limit);
        return parseDouble(df.format(parseDouble(d)));
    }
}
