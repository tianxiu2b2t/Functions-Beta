package org.functions.Bukkit.Main.functions;

import org.bukkit.Location;
import org.bukkit.World;
import org.functions.Bukkit.Main.Configuration;
import org.functions.Bukkit.Main.Functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Home {
    static YamlUsers yamlUsers = Functions.instance.yamlUsers();
    public static boolean isHome(UUID uuid, String name) {
        for (String s : getHomes(uuid)) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public static String getHome(UUID uuid, String name) {
        for (String s : getHomes(uuid)) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return name;
    }
    public static String getHomeName(UUID uuid, String name) {
        for (String s : getHomes(uuid)) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return name;
    }
    public static List<String> getHomes(UUID uuid) {
        if (yamlUsers.configurations.get(uuid).get("Homes")==null) {
            yamlUsers.set(uuid,"Homes",yamlUsers.configurations.get(uuid).createSection("Homes"));
        }
        return new ArrayList<>(yamlUsers.configurations.get(uuid).getConfigurationSection("Homes").getKeys(false));
    }
    public static Location getHomePos(UUID uuid, String name) {
        return Functions.instance.getAPI().changeStringToLocation(getHomePosString(uuid,name));
    }
    public static String getHomePosString(UUID uuid,String name) {
        return yamlUsers.configurations.get(uuid).getString("Homes." + getHome(uuid,name));
    }
    public static void setHomePos(UUID uuid,String name, World world, double x, double y, double z, float yaw, float pitch) {
        setHomePos(uuid,name,new Location(world,Utils.autoCon(x),Utils.autoCon(y),Utils.autoCon(z),yaw,pitch));
    }
    public static void setHomePos(UUID uuid,String name, World world, double x, double y, double z) {
        setHomePos(uuid,name,new Location(world,x,y,z));
    }
    public static void setHomePos(UUID uuid,String name,Location pos) {
        if (pos!=null) yamlUsers.set(uuid,"Homes." + name,Functions.instance.getAPI().changeLocationToString(pos));
        if (pos==null) yamlUsers.set(uuid,"Homes." + name,null);
    }
    public static void removeHome(UUID uuid, String name) {
        setHomePos(uuid,getHome(uuid,name),null);
    }
}
