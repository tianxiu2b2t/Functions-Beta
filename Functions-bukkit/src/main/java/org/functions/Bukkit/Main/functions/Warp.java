package org.functions.Bukkit.Main.functions;

import org.bukkit.Location;
import org.bukkit.World;
import org.functions.Bukkit.Main.Configuration;
import org.functions.Bukkit.Main.Functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Warp {
    static Configuration configuration = Functions.instance.getConfiguration();
    public static boolean isWarp(String name) {
        for (String s : getWarps()) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public static String getWarp(String name) {
        for (String s : getWarps()) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return name;
    }
    public static String getWarpName(String name) {
        for (String s : getWarps()) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return name;
    }
    public static List<String> getWarps() {
        return new ArrayList<>(configuration.getWarps().getConfigurationSection("").getKeys(false));
    }
    public static Location getWarpPos(String name) {
        return Functions.instance.getAPI().changeStringToLocation(getWarpPosString(name));
    }
    public static String getWarpPosString(String name) {
        return configuration.getWarps().getString(getWarp(name));
    }
    public static void setWarpPos(String name, World world, double x, double y, double z,float yaw, float pitch) {
        setWarpPos(name,new Location(world,Utils.autoCon(x),Utils.autoCon(y),Utils.autoCon(z),yaw,pitch));
    }
    public static void setWarpPos(String name, World world, double x, double y, double z) {
        setWarpPos(name,new Location(world,x,y,z));
    }
    public static void setWarpPos(String name,Location pos) {
        if (pos!=null) configuration.getWarps().set(name,Functions.instance.getAPI().changeLocationToString(pos));
        if (pos==null) configuration.getWarps().set(name,null);
        try {
            configuration.getWarps().save(configuration.Warps_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void removeWarp(String name) {
        setWarpPos(getWarp(name),null);
    }
}
