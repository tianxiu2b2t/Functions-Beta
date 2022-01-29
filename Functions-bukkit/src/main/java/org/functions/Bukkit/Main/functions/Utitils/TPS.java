package org.functions.Bukkit.Main.functions.Utitils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.functions.Bukkit.Main.Functions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class TPS {
    public static double[] recentTPS() {
        try {
            return (double[])Functions.instance.getClassesStorage().MinecraftServer.getMethod("getServer").invoke((Object)null).getClass().getField("recentTps").get(Functions.instance.getClassesStorage().MinecraftServer.getMethod("getServer").invoke((Object)null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[]{-20.0D,-20.0D,-20.0D};
    }
    public static double getTickPerSeconds() {
        double[] tps = recentTPS();
        return tps[0];
    }
    public static boolean isBetween(double max,double min) {
        return max >= getTickPerSeconds() && min <= getTickPerSeconds();
    }
    public static boolean isHigh(double min) {
        return min <= getTickPerSeconds();
    }
    public static boolean isLower(double max) {
        return max >= getTickPerSeconds();
    }
    public static String details_tps() {
        double[] tps = recentTPS();
        String[] tpsAvg = new String[tps.length];

        for(int i = 0; i < tps.length; ++i) {
            tpsAvg[i] = format(tps[i]);
        }
        return StringUtils.join(tpsAvg, ", ");
    }
    public static String getTPS() {
        return details_tps().split(", ")[0];
    }

    public static String format(double tps) {
        return (tps > 18.0D ? ChatColor.GREEN : (tps > 16.0D ? ChatColor.YELLOW : ChatColor.RED)).toString() + (tps > 21.0D ? "*" : "") + Math.min((double)Math.round(tps * 100.0D) / 100.0D, 20.0D);
    }
}
