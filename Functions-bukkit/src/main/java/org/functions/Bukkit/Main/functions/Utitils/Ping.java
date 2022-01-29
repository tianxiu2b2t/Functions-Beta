package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

public class Ping {
    public static int getPing(Player player) {
        try {
            return Functions.instance.getClassesStorage().PING.getInt(Functions.instance.getClassesStorage().getEntityPlayer(player));
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }
}
