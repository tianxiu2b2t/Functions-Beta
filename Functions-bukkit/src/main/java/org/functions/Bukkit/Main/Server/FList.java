package org.functions.Bukkit.Main.Server;

import org.functions.Bukkit.Main.Configuration;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FList {
    static Configuration configuration = Functions.instance.getConfiguration();
    public static boolean isPlayerInBanned(String name) {
        List<String> blacked = configuration.getList().getStringList("Blacked");
        if (blacked.size() == 0) return false;
        for (String s : blacked) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isPlayerNotInBanned(String name) {
        return !isPlayerInBanned(name);
    }
    public static boolean isPlayerInWhite(String name) {
        List<String> whited = configuration.getList().getStringList("Whited");
        if (whited.size() == 0) return false;
        for (String s : whited) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isPlayerNotInWhite(String name) {
        return !isPlayerInWhite(name);
    }
    public static boolean IsPlayerCanJoin(String name) {
        if (configuration.getSettings().getBoolean("Whited")) {
            if (isPlayerNotInBanned(name)) return isPlayerInWhite(name);
        }
        return isPlayerNotInBanned(name);
    }
}
